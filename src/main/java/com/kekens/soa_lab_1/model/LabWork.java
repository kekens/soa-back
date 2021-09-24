package com.kekens.soa_lab_1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "labwork")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabWork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    private String name; //Поле не может быть null, Строка не может быть пустой

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "coordinates_x")),
            @AttributeOverride(name = "y", column = @Column(name = "coordinates_y"))
    })
    private Coordinates coordinates; //Поле не может быть null

    private java.time.ZonedDateTime creationDate = java.time.ZonedDateTime.now(); //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Integer minimalPoint; //Поле может быть null, Значение поля должно быть больше 0

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty; //Поле может быть null

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "discipline_id", referencedColumnName = "id")
    private Discipline discipline; //Поле не может быть null
}