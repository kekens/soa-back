package com.kekens.validator.exception;

import com.kekens.validator.IntegrityError;

import java.util.List;

public class IncorrectDataException extends Exception {

    private final List<IntegrityError> errorList;

    public IncorrectDataException(List<IntegrityError> errorList) {
        this.errorList = errorList;
    }

    public List<IntegrityError> getErrorList() {
        return errorList;
    }

}
