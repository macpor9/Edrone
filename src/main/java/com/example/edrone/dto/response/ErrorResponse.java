package com.example.edrone.dto.response;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class ErrorResponse {
    private Timestamp timestamp;
    private int status;
    private String error;
}
