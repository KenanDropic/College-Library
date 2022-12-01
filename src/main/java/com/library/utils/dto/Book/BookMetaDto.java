package com.library.utils.dto.Book;


import com.library.utils.annotations.BookType;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;

@Data
public class BookMetaDto {
    @BookType
    private String bookType;

    @Min(0)
    private Integer pageNumbers;

    @Length(max = 15)
    private String binding;

    @Length(max = 3)
    private String size;

    @Length(max = 5)
    private String shape;

    @Nullable
    @Length(max = 5)
    private String eForm = null;

    @Nullable
    @Length(max = 50)
    private String eLocation = null;
}
