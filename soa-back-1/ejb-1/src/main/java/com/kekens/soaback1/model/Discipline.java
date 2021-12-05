package com.kekens.soaback1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "discipline")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Discipline implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name; //Поле не может быть null, Строка не может быть пустой

    private Long lectureHours; //Поле может быть null
}