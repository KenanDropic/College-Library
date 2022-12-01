package com.library.utils.dto.Book;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
public class UpdateCoAuthorDto {

    @Nullable
    @Length(min = 3, max = 20)
    private String coAuthorFirstName;
    @Nullable
    @Length(min = 3, max = 25)
    private String coAuthorLastName;
}
