package com.library.utils.dto.Loan;

import com.library.utils.annotations.MinDate;
import com.library.utils.annotations.Status;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CreateLoanDto {

    @NotNull(message = "Field cannot be empty")
    Long user_id;

    @NotNull(message = "Field cannot be empty")
    Long book_id;

    @NotNull(message = "Field cannot be empty")
    private boolean returnObligation;

    @NotNull(message = "Field cannot be empty")
    @MinDate
    private LocalDate borrowDate;

    @NotNull(message = "Field cannot be empty")
    private LocalDate dueDate;

    @Nullable
    private LocalDate returnedDate;

    @Nullable
    private Boolean loanExtended;

    @NotNull(message = "Field cannot be empty")
    @Status(type = "create")
    private String loanStatus;
}
