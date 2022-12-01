package com.library.utils.dto.Auth;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ResetPasswordEmailDto {
    @Email
    @NotNull
    @NotEmpty(message = "Field cannot be empty")
    private String email;
}
