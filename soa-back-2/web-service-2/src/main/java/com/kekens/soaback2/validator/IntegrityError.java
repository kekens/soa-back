package com.kekens.soaback2.validator;

import java.io.Serializable;

public class IntegrityError implements Serializable {
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
