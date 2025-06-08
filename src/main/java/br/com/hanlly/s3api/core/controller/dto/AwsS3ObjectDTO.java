package br.com.hanlly.s3api.core.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AwsS3ObjectDTO {
    private String fileName;
    private String bucketName;
    private Long size;

}
