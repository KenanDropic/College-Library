package com.library.utils.dto.Book;

import com.library.utils.annotations.MinDate;
import com.library.utils.annotations.ValidateWriteOffOptions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WriteOffDto {
    @ValidateWriteOffOptions
    private String writeOffReason;
    @MinDate
    private LocalDate writeOffDate;
    @Length(min = 3, max = 30)
    private String writeOffDocument;
    @Length(min = 10, max = 100)
    private String writeOffNote;
}
