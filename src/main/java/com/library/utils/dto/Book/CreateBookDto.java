package com.library.utils.dto.Book;

import com.library.entity.*;
import com.library.utils.annotations.Language;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class CreateBookDto {

    @NotNull(message = "Field cannot be empty")
    @Length(min = 3, max = 15)
    private String sourceTitle;

    @Length(min = 3, max = 35)
    private String sourceSubtitle;

    @Length(min = 3, max = 45)
    private String bosnianTitle;

    @Length(min = 3, max = 35)
    private String bosnianSubtitle;

    @NotNull(message = "Field cannot be empty")
    @Max(value = 8)
    @Min(value = 0)
    private Integer publicationOrdinalNumber;

    @NotNull(message = "Field cannot be empty")
    private List<Author> authors;

    @Nullable
    private List<CoAuthor> co_authors;

    @NotNull(message = "Field cannot be empty")
    @Language(type = "create")
    private String language;

    @NotNull(message = "Field cannot be empty")
    private Publisher publisher;

    @NotNull(message = "Field cannot be empty")
    private Pressman pressman;

    @NotNull(message = "Field cannot be empty")
    @Min(value = 1500)
    @Max(value = 2022)
    private Integer releaseYear;

    @NotNull(message = "Field cannot be empty")
    private CipCarrier cip_carrier;

    @NotNull(message = "Field cannot be empty")
    @Length(max = 15)
    private String cipNumber;

    @NotNull(message = "Field cannot be empty")
    @Length(max = 15)
    private String isbn;

    @NotNull(message = "Field cannot be empty")
    @Length(max = 15)
    private String COBISS;

    @NotNull(message = "Field cannot be empty")
    @Min(value = 0)
    private Integer inStock;

    @Length(max = 100)
    private String note;

    @NotNull(message = "Field cannot be empty")
    List<BookMeta> meta_data;

    @NotNull(message = "Field cannot be empty")
    List<BookProcurement> bookProcurements;
}
