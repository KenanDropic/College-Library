package com.library.utils.projections;

import java.time.LocalDate;

public interface LoansView {
    String getSource_Title();

    Long getBook_Id();

    Long getUser_Id();

    String getFullname();

    Long getLoan_Id();

    LocalDate getBorrow_Date();

    LocalDate getDue_Date();

    String getLoan_Status();
}
