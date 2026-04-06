package com.busticket.app.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private String message;
    private int statusCode;
    private String errorCode;
    private LocalDateTime timestamp;
}
