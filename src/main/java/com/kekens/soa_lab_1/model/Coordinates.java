package com.kekens.soa_lab_1.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Coordinates {
    private long x;
    private int y; //Значение поля должно быть больше -895
}