package com.library.utils.projections;


public interface UserLoansView {
    String getEmail();
    String getFullname();
    String getPhone();
    String getBorrow_Date();
    String getDue_Date();
    String getReturn_Date();
    char getLoan_Status();
    boolean getReturn_Obligation();
    String getSource_Title();
}
