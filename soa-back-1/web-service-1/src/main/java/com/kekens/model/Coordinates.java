package com.kekens.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Coordinates implements Serializable {
    private long x;
    private int y; //Значение поля должно быть больше -895
}