package com.kekens.soa_lab_1.model;

import lombok.Data;

import javax.persistence.Embeddable;

@Embeddable
@Data
public class Coordinates {
    private long x;
    private int y; //Значение поля должно быть больше -895
}