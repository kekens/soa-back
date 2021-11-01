package com.kekens.soa_back_2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Discipline {
    private int id;

    private String name; //Поле не может быть null, Строка не может быть пустой

    private Long lectureHours; //Поле может быть null
}