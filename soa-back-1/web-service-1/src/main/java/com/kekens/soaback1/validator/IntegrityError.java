package com.kekens.soaback1.validator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "integrity-error")
@XmlAccessorType(XmlAccessType.FIELD)
public class IntegrityError implements Serializable {

    @XmlElement
    private final int code;
    @XmlElement
    private final String message;

    public IntegrityError() {
        this.code = -1;
        this.message = null;
    }

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
        return String.format("Error %d: %s\n", code, message);
    }
}
