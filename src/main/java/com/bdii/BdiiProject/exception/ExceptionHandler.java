package com.bdii.BdiiProject.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Michal on 26.02.2019
 */

public class ExceptionHandler {

    protected final static Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @Autowired
    private ObjectMapper objectMapper;

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        logger.info("URL called: : {}", ((ServletWebRequest) request).getRequest().getRequestURL().toString());
        String mappedJson;
        Map<String,SingleError> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            logger.info("Parametr {} is not valid. Validation error: {}", fieldError.getField(), fieldError.getDefaultMessage());
            if(errorMap.get(fieldError.getField()) == null){
                errorMap.put(fieldError.getField(),new SingleError(fieldError.getField(), fieldError.getRejectedValue().toString(),fieldError.getDefaultMessage()));
            } else {
                SingleError sngl = errorMap.get(fieldError.getField());
                sngl.addValidationError(fieldError.getDefaultMessage());
            }
        });
        try {
            mappedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(ex.getBindingResult().getTarget());
            logger.info("Object passed: \n {}",mappedJson);
        } catch (JsonProcessingException e) {
            logger.info("Cannot deserialize object");
            e.printStackTrace();
        }
        List<SingleError> list = new ArrayList<>(errorMap.values());
        ResponseErrorMessage responseErrorMessage = new ResponseErrorMessage("xD",list);
        return new ResponseEntity(responseErrorMessage, HttpStatus.BAD_REQUEST);
    }
}
