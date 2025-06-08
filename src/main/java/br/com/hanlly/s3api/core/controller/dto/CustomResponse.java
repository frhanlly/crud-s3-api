package br.com.hanlly.s3api.core.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class CustomResponse {
    private String status;
    private String message;
    private LocalDateTime localDateTime;
    private List<ErrorField> errors;
    private Object data;

}
