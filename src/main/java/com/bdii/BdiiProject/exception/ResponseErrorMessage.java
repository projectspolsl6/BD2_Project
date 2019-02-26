package com.bdii.BdiiProject.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

/**
 * @author Michal on 26.02.2019
 */

public class ResponseErrorMessage {

    @JsonProperty("endpoint")
    private String endpoint;

    @JsonProperty("currentDate")
    private Date currentDate;

    @JsonProperty("errors")
    private List<SingleError> errors;

    public ResponseErrorMessage(String endpoint, List<SingleError> errors) {
        this.endpoint = endpoint;
        this.currentDate = new Date();
        this.errors = errors;
    }
}
