package com.library.utils.dto.User;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class UserRoleDto {

    private Long id;
    private String role;

    public UserRoleDto(Long id,String role) {
        this.id = id;
        this.role = role;
    }
}
