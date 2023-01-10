package com.library.utils.dto.Book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchBookDto {
    @Nullable
    @Length(min = 3, max = 25)
    private String title;
    @Nullable
    @Length(min = 2, max = 18)
    private String author;
    @Nullable
    @Length(min = 2, max = 15)
    private String bookType;
    @Nullable
    @Length(min = 2, max = 5)
    private String status;

    private String field = "source_title";
    private String direction = "ASC";
}
