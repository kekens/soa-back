package com.kekens.soaback1.validator.exception;

import com.kekens.soaback1.validator.IntegrityError;

import java.util.List;

public class IncorrectDataException extends Exception {

    private final List<IntegrityError> errorList;

    public IncorrectDataException(List<IntegrityError> errorList) {
        this.errorList = errorList;
    }

    public List<IntegrityError> getErrorList() {
        return errorList;
    }

    @Override
    public String toString() {
        StringBuilder errorString = new StringBuilder();
        errorList.forEach(errorString::append);
        return errorString.toString();
    }
}
