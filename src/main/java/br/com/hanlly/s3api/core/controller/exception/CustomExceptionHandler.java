package br.com.hanlly.s3api.core.controller.exception;

import br.com.hanlly.s3api.core.controller.dto.CustomResponse;
import br.com.hanlly.s3api.core.controller.dto.ErrorField;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public CustomResponse maxUploadSizeExceededExceptionHandler(MaxUploadSizeExceededException e){

        return new CustomResponse("Failed",
                "File too large. Please, send something under 10 MiB size",
                LocalDateTime.now(),
                List.of(new ErrorField("Body Payload", "File too large")),
                null
                );

    }



}
