package com.kekens.soaback2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Discipline implements Serializable {
    private int id;

    private String name; //Поле не может быть null, Строка не может быть пустой

    private Long lectureHours; //Поле может быть null
}