package com.kekens.soa_lab_1.validator.exception;

import java.util.List;

public class IncorrectDataException extends Exception {

    private final List<String> errorList;

    public IncorrectDataException(List<String> errorList) {
        this.errorList = errorList;
    }

    public List<String> getErrorList() {
        return errorList;
    }

}
