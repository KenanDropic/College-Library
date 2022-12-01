package com.library.utils.dto.Loan;

import com.library.utils.annotations.Status;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UpdateLoanDto {
    @Nullable
    private Long userId;
    @Nullable
    private LocalDate dueDate;
    @Nullable
    private LocalDate returnedDate;
    @Nullable
    private boolean loanExtended;
    @Nullable
    @Status(type = "update")
    private String loanStatus;
}
