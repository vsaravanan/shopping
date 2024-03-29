package com.saravan.mastercard.entity;

import org.springframework.http.HttpStatus;

public class ApiResp<T> {

    private HttpStatus status;
    private String message;
    private Object result;

    public ApiResp(HttpStatus status, String message, Object result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
