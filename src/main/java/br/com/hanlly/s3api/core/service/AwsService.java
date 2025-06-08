package br.com.hanlly.s3api.core.service;

import br.com.hanlly.s3api.core.controller.dto.AwsS3ObjectDTO;
import br.com.hanlly.s3api.core.controller.dto.CustomResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
@Data
@AllArgsConstructor
public class AwsService {
    public static final String AWS_BUCKET = "my-api-bucket-hanlly";
    @Autowired
    S3Client s3Client;

    public PutObjectRequest createObjectInBucket(MultipartFile file) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(AWS_BUCKET)
                .key(file.getOriginalFilename())
                .contentType(file.getContentType())
                .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(
                    file.getInputStream(),
                    file.getSize()
            ));
        } catch (IOException exception){
            System.out.println(exception.getMessage());
        }

        return putObjectRequest;
    }

    public List<AwsS3ObjectDTO> getObjectInBucket(String fileName){
        List<AwsS3ObjectDTO> listFiles = new LinkedList<>();
        int size = fileName == null || fileName.isEmpty() ? 10 : 1;

        ListObjectsRequest objectsRequest = ListObjectsRequest
                .builder()
                .bucket(AWS_BUCKET)
                .maxKeys(size)
                .build();

        for(S3Object objectFromS3 : s3Client.listObjects(objectsRequest).contents() ){
            listFiles.add(
                    new AwsS3ObjectDTO(objectFromS3.key(), objectsRequest.bucket(), objectFromS3.size())
            );
        }

        return listFiles;

    }

    public void deleteObjectInBucket(String fileName){
        DeleteObjectRequest objectRequest = DeleteObjectRequest
                .builder()
                .bucket(AWS_BUCKET)
                .key(fileName)
                .build();


        s3Client.deleteObject(objectRequest);
    }





}
