package com.library.utils.projections;

import java.time.LocalDate;

public interface LoanView {
    String getSource_Title();

    Long getBook_Id();

    Integer getRelease_Year();

    String getLanguage();


    Long getUser_Id();

    String getFullname();

    String getEmail();

    Long getLoan_Id();

    LocalDate getBorrow_Date();

    LocalDate getDue_Date();

    String getLoan_Status();
}
