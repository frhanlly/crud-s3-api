package br.com.hanlly.s3api.core.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KafkaEventMessage {
    private String bucketName;
    private String action;



}
