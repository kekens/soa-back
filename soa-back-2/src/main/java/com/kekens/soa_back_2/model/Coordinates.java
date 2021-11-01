package com.kekens.soa_back_2.model;

import lombok.Data;

@Data
public class Coordinates {
    private long x;
    private int y; //Значение поля должно быть больше -895
}