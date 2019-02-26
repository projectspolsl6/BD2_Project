package com.bdii.BdiiProject.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Michal on 26.02.2019
 */
@Aspect
@Component
public class LoggingRestMessageAspect {

    protected final static Logger logger = LoggerFactory.getLogger(LoggingRestMessageAspect.class);

    @Autowired
    private ObjectMapper mapper;

    @Before(value = "execution(* *..*Controller.*(..))")
    private void restEndpointCalledRequest(JoinPoint joinPoint) {
        logger.info("Entering class : {} || Method : {} ", joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName());
        if (joinPoint.getArgs().length > 0) {
            try {
                String mappedJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(joinPoint.getArgs()[0]);
                logger.info("Object serialized: \n" + mappedJson);
            } catch (JsonProcessingException e) {
                logger.error("Cannot deserialize", e);
            }
        }
    }

    @AfterReturning(value = "execution(* *..*Controller.*(..)) && @annotation(org.springframework.web.bind.annotation.PostMapping)", returning = "result")
    private void restEndpointCalledResponse(JoinPoint joinPoint, Object result) {
        logger.info("Returning from class : {} || Method : {} ", joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName());
        try {
            String mappedObject = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
            logger.info("Object serialized: \n" + mappedObject);
        } catch (JsonProcessingException e) {
            logger.error("Cannot deserialize", e);
        }
    }


}
