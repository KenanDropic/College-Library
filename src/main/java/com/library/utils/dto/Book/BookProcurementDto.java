package com.library.utils.dto.Book;

import com.library.utils.annotations.ProcurementWay;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class BookProcurementDto {
    @ProcurementWay
    private String procurementWay;

    @Min(1500)
    @Max(2023)
    private Integer procurementYear;

    @Length(max = 50)
    private String procurementSource;
}
