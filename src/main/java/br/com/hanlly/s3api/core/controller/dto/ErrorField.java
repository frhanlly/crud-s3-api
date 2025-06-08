package br.com.hanlly.s3api.core.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorField {
    private String field;
    private String error;
}
