package com.bdii.BdiiProject.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Michal on 26.02.2019
 */

public class SingleError {

    @JsonProperty("paramater")
    private String parameter;

    @JsonProperty("validationErrors")
    private List<String> validationErrors;

    @JsonProperty("passedValue")
    private String passedValue;

    public SingleError(String parameter, String passedValue, String... errors) {
        this.parameter = parameter;
        this.validationErrors = new ArrayList<>();
        if(errors!=null){
            validationErrors.addAll(Arrays.asList(errors));
        }
        this.passedValue=passedValue;
    }

    public void addValidationError(String error){
        this.validationErrors.add(error);
    }
}
