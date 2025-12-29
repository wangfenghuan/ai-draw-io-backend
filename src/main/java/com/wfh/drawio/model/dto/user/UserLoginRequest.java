package com.wfh.drawio.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Data;

/**
 * 用户登录请求
 *
 * @author wangfenghuan
 * @from wangfenghuan
 */
@Data
@Schema(name = "UserLoginRequest", description = "用户登录请求")
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    @Schema(description = "用户账号", example = "admin")
    private String userAccount;

    @Schema(description = "用户密码", example = "********")
    private String userPassword;
}
