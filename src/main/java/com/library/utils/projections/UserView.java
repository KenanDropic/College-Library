package com.library.utils.projections;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;

public interface UserView {
    Long getUser_Id();
    String getFullname();
    String getEmail();
    Boolean getEmail_Confirmed();
    String getPhone();
    LocalDateTime getCreated_At();
    LocalDateTime getUpdated_At();
    JsonNode getRoles();
}
