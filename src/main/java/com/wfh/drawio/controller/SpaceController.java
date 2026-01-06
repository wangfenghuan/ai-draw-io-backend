package com.wfh.drawio.controller;

import com.wfh.drawio.annotation.AuthCheck;
import com.wfh.drawio.common.BaseResponse;
import com.wfh.drawio.common.DeleteRequest;
import com.wfh.drawio.common.ErrorCode;
import com.wfh.drawio.common.ResultUtils;
import com.wfh.drawio.constant.UserConstant;
import com.wfh.drawio.exception.BusinessException;
import com.wfh.drawio.exception.ThrowUtils;
import com.wfh.drawio.model.dto.space.SpaceUpdateRequest;
import com.wfh.drawio.model.entity.Space;
import com.wfh.drawio.model.entity.User;
import com.wfh.drawio.model.enums.SpaceLevelEnum;
import com.wfh.drawio.model.vo.SpaceLevel;
import com.wfh.drawio.service.SpaceService;
import com.wfh.drawio.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Title: SpaceController
 * @Author wangfenghuan
 * @Package com.wfh.drawio.controller
 * @Date 2026/1/6 09:50
 * @description:
 */
@RestController
@RequestMapping("/space")
public class SpaceController {

    @Resource
    private SpaceService spaceService;

    @Resource
    private UserService userService;

    /**
     * 更新空间信息
     * 管理员专用的空间信息更新接口
     *
     * @param spaceUpdateRequest 空间更新请求
     * @return 是否更新成功
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "更新空间信息（管理员专用）",
            description = """
                    管理员专用的空间信息更新接口。

                    **权限要求：**
                    - 仅限admin角色使用

                    **注意事项：**
                    - 如果修改了空间级别，会自动重新设置maxCount和maxSize
                    - 不会影响当前的totalSize和totalCount
                    """)
    public BaseResponse<Boolean> updateSpace(@RequestBody SpaceUpdateRequest spaceUpdateRequest) {
        if (spaceUpdateRequest == null || spaceUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 将实体类和 DTO 进行转换
        Space space = new Space();
        BeanUtils.copyProperties(spaceUpdateRequest, space);
        // 自动填充数据
        spaceService.fillSpaceBySpaceLevel(space);
        // 数据校验
        spaceService.validSpace(space, false);
        // 判断是否存在
        long id = spaceUpdateRequest.getId();
        Space oldSpace = spaceService.getById(id);
        ThrowUtils.throwIf(oldSpace == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = spaceService.updateById(space);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 删除空间
     * 删除空间及其内部的所有图表
     *
     * @param deleteRequest 删除请求（包含空间ID）
     * @param request HTTP请求
     * @return 是否删除成功
     */
    @PostMapping("/delete")
    @Operation(summary = "删除空间",
            description = """
                    删除指定的空间，并自动删除空间内的所有图表。

                    **功能说明：**
                    - 删除空间记录
                    - 级联删除空间内的所有图表
                    - 使用事务确保删除操作的原子性

                    **额度处理：**
                    - 删除空间不会释放额度（因为空间本身被删除了）
                    - 删除图表时也不会释放额度（因为关联的空间也被删除了）

                    **权限要求：**
                    - 需要登录
                    - 仅空间创建人或管理员可删除

                    **注意事项：**
                    - 删除操作不可逆，请谨慎操作
                    - 删除后空间内的所有图表都会被删除
                    - 对象存储中的文件不会自动删除（可通过定时任务清理）
                    """)
    public BaseResponse<Boolean> deleteSpace(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        User loginUser = userService.getLoginUser(request);

        // 判断是否存在
        Space oldSpace = spaceService.getById(id);
        ThrowUtils.throwIf(oldSpace == null, ErrorCode.NOT_FOUND_ERROR);

        // 仅空间创建人或管理员可删除
        if (!oldSpace.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 使用service层方法处理业务逻辑（包含关联删除图表）
        spaceService.deleteSpaceWithDiagrams(id);

        return ResultUtils.success(true);
    }

    /**
     * 查询空间级别列表
     * 获取所有可用的空间级别信息
     *
     * @return 空间级别列表
     */
    @GetMapping("/list/level")
    @Operation(summary = "查询空间级别列表",
            description = """
                    获取所有可用的空间级别信息，用于前端展示空间等级和对应的额度限制。

                    **返回内容：**
                    - value：级别值（0=普通版，1=专业版，2=旗舰版）
                    - text：级别名称（"普通版"、"专业版"、"旗舰版"）
                    - maxCount：最大图表数量
                    - maxSize：最大存储空间（字节）

                    **级别说明：**
                    - **普通版（value=0）：**
                      - 最大100个图表
                      - 最大100MB存储空间
                    - **专业版（value=1）：**
                      - 最大1000个图表
                      - 最大1000MB存储空间
                    - **旗舰版（value=2）：**
                      - 最大10000个图表
                      - 最大10000MB存储空间

                    **权限要求：**
                    - 无需登录，所有用户可查询
                    """)
    public BaseResponse<List<SpaceLevel>> listSpaceLevel() {
        List<SpaceLevel> spaceLevelList = Arrays.stream(SpaceLevelEnum.values())
                .map(spaceLevelEnum -> new SpaceLevel(
                        spaceLevelEnum.getValue(),
                        spaceLevelEnum.getText(),
                        spaceLevelEnum.getMaxCount(),
                        spaceLevelEnum.getMaxSize()))
                .collect(Collectors.toList());
        return ResultUtils.success(spaceLevelList);
    }


}
