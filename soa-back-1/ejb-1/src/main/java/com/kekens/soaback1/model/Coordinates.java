package com.kekens.soaback1.model;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class Coordinates implements Serializable {
    private long x;
    private int y; //Значение поля должно быть больше -895
}