package com.project.AuthSystem.config.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ExceptionResponse {

    private Date timestamp;
    private int statusCode;
    private String message;

//    public ExceptionResponse(Date timestamp, int statusCode, String message) {
//        this.timestamp = timestamp;
//        this.statusCode = statusCode;
//        this.message = message;
//    }
}
