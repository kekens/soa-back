package com.kekens.soaback1.validator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "integrity-error")
@XmlAccessorType(XmlAccessType.FIELD)
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
