package com.library.utils.dto.Auth;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateUserDto extends LoginUserDto {

    @SuppressWarnings("unused")
    public CreateUserDto(String first_name, String last_name, String phone,
                          String email, String password) {
        super(email,password);
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
    }

    @NotNull(message = "Field cannot be empty")
    @Length(max = 12,min = 3)
    private String first_name;

    @NotNull(message = "Field cannot be empty")
    @Length(max = 14,min = 4)
    private String last_name;

    @Pattern(regexp =  "^[+][0-9]{3}[\s-.]?6[1-5][\s-.]?[0-9]{3}[\s-.]?[0-9]{3,4}$",
            message = "Allowed number format examples: 1) +38761653789 2) +386 62 653 789 3) +384.64.653.7891 4) +387-63-653-789")
    private String phone;
}
