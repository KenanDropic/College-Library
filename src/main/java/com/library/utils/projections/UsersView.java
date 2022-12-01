package com.library.utils.projections;

import java.util.List;

public interface UsersView {
    Long getUser_Id();
    String getFullname();
    String getEmail();
    Boolean getEmail_Confirmed();
    String getPhone();
    List<String> getRoles();
    String getCreated_At();
    String getUpdated_At();
}
