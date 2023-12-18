package com.bsktpay.dfs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileMessageRequest {

    @JsonProperty("fileName")
    private String fileName;

    @JsonProperty("message")
    private String message;

    // No-arg constructor
    public FileMessageRequest() {}

    // Constructor with parameters
    public FileMessageRequest(String fileName, String message) {
        this.fileName = fileName;
        this.message = message;
    }

    // Getters
    public String getFileName() {
        return fileName;
    }

    public String getMessage() {
        return message;
    }

    // Setters
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // toString method for logging purposes (optional)
    @Override
    public String toString() {
        return "FileMessageRequest{" +
                "fileName='" + fileName + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
