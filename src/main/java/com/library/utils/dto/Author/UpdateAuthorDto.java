package com.library.utils.dto.Author;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
public class UpdateAuthorDto {
    @Nullable
    @Length(min = 3, max = 20)
    private String authorFirstName;
    @Nullable
    @Length(min = 3, max = 25)
    private String authorLastName;
}
