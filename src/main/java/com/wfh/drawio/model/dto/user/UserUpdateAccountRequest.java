package com.wfh.drawio.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name = "UserUpdateAccountRequest", description = "用户更新账户请求")
public class UserUpdateAccountRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "新用户账号 (邮箱)，也是接收验证码的邮箱", required = true)
    private String userAccount;

    @Schema(description = "邮箱验证码", required = true)
    private String emailCode;

    @Schema(description = "新密码 (如需修改密码)")
    private String newPassword;

    @Schema(description = "确认新密码 (如需修改密码)")
    private String checkPassword;
}
