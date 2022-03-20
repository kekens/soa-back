package com.kekens.soaback1.soap.fault;


import javax.xml.ws.WebFault;

@WebFault(name = "soapFault")
public class SoapFault extends Exception {

    private String message;
    private int code;

    public SoapFault(String message, int code) {
        super(message);
        this.code = code;
    }

    public SoapFault(String message) {
        super(message);
    }

}