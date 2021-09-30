package com.kekens.soa_lab_1.validator;

public class IntegrityError {
    private final int code;
    private final String message;

    public IntegrityError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("Error %d: %s", code, message);
    }
}
