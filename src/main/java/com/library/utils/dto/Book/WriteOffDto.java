package com.library.utils.dto.Book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WriteOffDto {
    @Length(min = 5, max = 30)
    private String writeOffReason;
    @Length(min = 4, max = 4)
    private Integer writeOffYear;
    @Length(min = 3, max = 30)
    private String writeOffDocument;
    @Length(min = 10, max = 100)
    private String writeOffNote;
}
