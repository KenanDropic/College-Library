package com.library.utils.dto.Auth;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class ResetPasswordDto {

    @NotNull(message = "Field cannot be empty")
    private String oldPassword;
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,}$",
            message = "Must contain at least 6 characters,uppercase,lowercase letter,special character and number")
    @NotNull(message = "Field cannot be empty")
    private String newPassword;
    @NotNull(message = "Field cannot be empty")
    private String token;
}
