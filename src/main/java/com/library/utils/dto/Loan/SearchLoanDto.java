package com.library.utils.dto.Loan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchLoanDto {
    @Nullable
    @Length(min = 3, max = 25)
    private String title;
    @Nullable
    @Length(min = 3, max = 25)
    private String member;
    @Nullable
    private String fromDate;
    @Nullable
    @Length(min = 3, max = 15)
    private String toDate;
    @Nullable
    private String status;

    private String field = "source_title";
    private String direction = "ASC";
}
