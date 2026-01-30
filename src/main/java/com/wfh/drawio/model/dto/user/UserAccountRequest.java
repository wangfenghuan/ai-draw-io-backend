package com.wfh.drawio.model.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fenghuanwang
 */
@Data
@Schema(name = "UserAccountRequest", description = "用户账户请求")
public class UserAccountRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户账号 (邮箱)", required = true)
    @NotEmpty(message = "用户账号不能为空")
    @Email(message = "用户账号必须是有效的邮箱地址")
    private String userAccount;
}
