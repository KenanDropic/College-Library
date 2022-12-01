package com.library.utils.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
public class ResponseMessage<T> {
    private boolean success;
    private String message;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<String> accessToken;
    private T data;

    public ResponseMessage(boolean success, String message, String accessToken) {
        this.success = success;
        this.message = message;
        this.accessToken = Optional.ofNullable(accessToken);
    }

    public ResponseMessage(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ResponseMessage(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public ResponseMessage(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
