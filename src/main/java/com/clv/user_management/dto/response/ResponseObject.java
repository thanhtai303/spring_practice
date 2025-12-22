package com.clv.user_management.dto.response;

import lombok.Data;

@Data
public class ResponseObject<T> {
    private String status;
    private String message;
    private T data;

    public ResponseObject(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
