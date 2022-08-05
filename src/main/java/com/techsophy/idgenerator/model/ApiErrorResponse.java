package com.techsophy.idgenerator.model;

import lombok.Value;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Value
public class ApiErrorResponse
{
    Instant timestamp;
    String message;
    String error;
    HttpStatus status;
    String path;
}
