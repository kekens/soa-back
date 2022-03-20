package com.kekens.soaback1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "labwork")
@XmlRootElement(name = "labwork")
@XmlAccessorType(XmlAccessType.FIELD)
public class LabWork implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlElement
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @XmlElement
    private String name; //Поле не может быть null, Строка не может быть пустой

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "coordinates_x")),
            @AttributeOverride(name = "y", column = @Column(name = "coordinates_y"))
    })
    @XmlElement
    private Coordinates coordinates; //Поле не может быть null

    @Column(updatable = false)
    @XmlElement
    private Date creationDate =
            Date.from(java.time.LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).atZone(ZoneId.systemDefault()).toInstant()); //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @XmlElement
    private Integer minimalPoint; //Поле может быть null, Значение поля должно быть больше 0

    @Enumerated(EnumType.STRING)
    @XmlElement
    private Difficulty difficulty; //Поле может быть null

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "discipline_id", referencedColumnName = "id")
    @XmlElement
    private Discipline discipline; //Поле не может быть null

}