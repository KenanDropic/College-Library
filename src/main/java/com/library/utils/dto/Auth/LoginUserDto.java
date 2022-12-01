package com.library.utils.dto.Auth;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class LoginUserDto {
    @Email
    @NotNull(message = "Field cannot be empty")
    private String email;

    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,}$",
            message = "Must contain at least 6 characters,uppercase,lowercase letter,special character and number" )
    @NotNull(message = "Field cannot be empty")
    private String password;

    protected LoginUserDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
