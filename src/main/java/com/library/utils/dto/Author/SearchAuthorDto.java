package com.library.utils.dto.Author;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
public class SearchAuthorDto {
    @Nullable
    @Length(min = 3, max = 20)
    private String authorName;

    private String field = "author_id"; // id or author name
    private String direction = "ASC";
}
