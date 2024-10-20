package com.started.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ErrorMessage {

    private int statusCode;
    private Date date;
    private String msg;
    private String requestDetails;

}
