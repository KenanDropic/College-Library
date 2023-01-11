package com.library.utils.dto.Loan;

import com.library.utils.annotations.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLoanDto {
    @Nullable
    private Long userId;
    @Nullable
    private LocalDate dueDate;
    @Nullable
    private LocalDate returnedDate;
    @Nullable
    private Boolean loanExtended;
    @Nullable
    @Status(type = "update")
    private String loanStatus;
}
