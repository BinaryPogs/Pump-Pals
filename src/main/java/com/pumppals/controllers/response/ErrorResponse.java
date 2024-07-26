package com.pumppals.controllers.response;

import java.util.Collections;
import java.util.List;

public class ErrorResponse {
    private List<String> errors;

    public ErrorResponse(String error) {
        this.errors = Collections.singletonList(error);
    }

    public ErrorResponse(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
