package com.library.utils.dto.Book;

import com.library.utils.annotations.Language;
import com.library.utils.annotations.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookDto {
    @Nullable
    @Length(min = 5, max = 45)
    private String sourceTitle;

    @Nullable
    @Length(min = 5, max = 45)
    private String sourceSubtitle;

    @Nullable
    @Length(min = 5, max = 45)
    private String bosnianTitle;

    @Nullable
    @Length(min = 5, max = 45)
    private String bosnianSubtitle;

    @Nullable
    @Min(0)
    @Max(10)
    private Integer publicationOrdinalNumber;

    @Nullable
    @Language(type = "update")
    private String language;

    // private Publisher publisher;
    // private Pressman pressman;
    // private CipCarrier cipCarrier;

    @Nullable
    //@ValidateReleaseYear(type = "update")
    private String releaseYear;


    @Nullable
    @Length(min = 5, max = 15)
    private String cipNumber;

    @Nullable
    @Length(min = 10, max = 18)
    private String isbn;

    @Nullable
    @Length(min = 8, max = 25)
    private String cobiss;

    @Nullable
    @Status(type = "update")
    private String status;

    @Nullable
    private Integer inStock;

    @Nullable
    @Length(max = 100)
    private String note;

    @Nullable
    @Valid
    private UpdateMetaDto meta_data;

    /* List<BookProcurement> bookProcurements;
       private BookWriteOff bookWriteOff; */
}
