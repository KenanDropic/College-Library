package com.library.utils.dto.Author;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CreateAuthorDto {
    @Length(min = 3, max = 15)
    private String authorFirstName;
    @Length(min = 3, max = 15)
    private String authorLastName;
}
