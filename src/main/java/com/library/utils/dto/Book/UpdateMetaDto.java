package com.library.utils.dto.Book;

import com.library.utils.annotations.BookType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMetaDto {
    @Nullable
    @BookType
    private String bookType;

    @Nullable
    @Min(0)
    private Integer pageNumbers;

    @Nullable
    @Length(max = 15)
    private String binding;

    @Nullable
    @Length(max = 3)
    private String size;

    @Nullable
    @Length(max = 5)
    private String shape;

    @Nullable
    @Length(max = 4)
    private String eForm;

    @Nullable
    @Length(max = 50)
    private String eLocation;
}
