package com.bsktpay.dfs.dto;

public class ResponseDTO {

    private String message;
    private int status;
    private Object data;

    public ResponseDTO() {
    }

    public ResponseDTO(String message, int status, Object data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    // toString() method for logging (optional)
    @Override
    public String toString() {
        return "ResponseDTO{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", data=" + data +
                '}';
    }
}
