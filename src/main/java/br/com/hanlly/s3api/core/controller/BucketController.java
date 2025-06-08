package br.com.hanlly.s3api.core.controller;

import br.com.hanlly.s3api.core.service.AwsService;
import br.com.hanlly.s3api.core.controller.dto.AwsS3ObjectDTO;
import br.com.hanlly.s3api.core.controller.dto.CustomResponse;
import br.com.hanlly.s3api.core.controller.dto.KafkaEventMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/files")
public class BucketController {

    @Value("${spring.kafka.topics.bucket-topic}")
    private String bucketTopic;
    @Value("${spring.kafka.topics.object-topic}")
    public String objectTopic;

    @Value("${spring.}")
    public String AWS_BUCKET;
    public static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public AwsService awsService;

    @Autowired
    S3Client s3Client;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomResponse> saveFile(@RequestParam("file") MultipartFile file){

        PutObjectRequest objectRequest = awsService.createObjectInBucket(file);

        //write event to kafka as follows
        KafkaEventMessage message = new KafkaEventMessage(
                objectRequest.bucket(),
                "object with the name ".concat(file.getOriginalFilename()).concat(" was created!")
        );

        try{
            String kafkaMessage = mapper.writeValueAsString(message);
            kafkaTemplate.send(bucketTopic, kafkaMessage);
        } catch (JsonProcessingException exception){
            System.out.println(exception.getMessage());
        }

        CustomResponse responseBody = new CustomResponse(
                "sucessfull",
                "File uploaded to Bucket!",
                LocalDateTime.now(),
                List.of(),
                null

        );

        return new ResponseEntity<CustomResponse>(responseBody, HttpStatus.CREATED);



    }




    @GetMapping("")
    public ResponseEntity<CustomResponse> getFilesInBucket(@RequestParam(name = "name", required = false) String fileName){
        //get all first 10 files
        List<AwsS3ObjectDTO> listFiles = awsService.getObjectInBucket(fileName);


        KafkaEventMessage message = new KafkaEventMessage(
                AWS_BUCKET,
                "Listando objeto(s) do bucket ".concat(AWS_BUCKET)
        );

        try{
            String kafkaMessage = mapper.writeValueAsString(message);
            kafkaTemplate.send(bucketTopic, kafkaMessage);
        } catch (JsonProcessingException exception){
            System.out.println(exception.getMessage());
        }


        CustomResponse customResponse = new CustomResponse(
                "Sucess",
                "Arquivos listados com sucesso",
                LocalDateTime.now(),
                List.of(),
                listFiles
        );

        return new ResponseEntity<>(
                customResponse,
                HttpStatus.OK
        );


    }



    @DeleteMapping("/{fileName}")
    public ResponseEntity<CustomResponse> deleteObject(@PathVariable(name = "fileName") String fileName){
        awsService.deleteObjectInBucket(fileName);


        KafkaEventMessage message = new KafkaEventMessage(
                AWS_BUCKET,
                "Object with name ".concat(fileName).concat(" was deleted!")
        );

        try{
            String kafkaMessage = mapper.writeValueAsString(message);
            kafkaTemplate.send(bucketTopic, kafkaMessage);
        } catch (JsonProcessingException exception){
            System.out.println(exception.getMessage());
        }

        CustomResponse customResponse = new CustomResponse(
                "Success",
                "Object Deleted!",
                LocalDateTime.now(),
                List.of(),
                null
        );



        return new ResponseEntity<>(customResponse, HttpStatus.NO_CONTENT);
    }








}
