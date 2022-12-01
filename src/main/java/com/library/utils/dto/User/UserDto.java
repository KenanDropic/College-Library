package com.library.utils.dto.User;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@EqualsAndHashCode
public class UserDto {
    public UserDto(Long id, String first_name, String last_name, String email,
                   Boolean emailConfirmed, List<String> roles,
                   String phone, @Nullable LocalDateTime created_at,
                   LocalDateTime updated_at) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.emailConfirmed = emailConfirmed;
        this.roles = roles;
        this.phone = phone;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    private Long id;
    private String first_name;
    private String last_name;

    private String email;
    private Boolean emailConfirmed;
    private List<String> roles;
    //private ArrayList<String> roles;
    private String phone;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
