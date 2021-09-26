package com.kekens.soa_lab_1.util;

import com.kekens.soa_lab_1.model.Coordinates;
import com.kekens.soa_lab_1.model.Difficulty;
import com.kekens.soa_lab_1.model.Discipline;
import com.kekens.soa_lab_1.model.LabWork;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LabWorkFilterConfiguration {

    private final static String PARAM_NAME = "name";
    private final static String PARAM_COORDINATES_X = "x";
    private final static String PARAM_COORDINATES_Y = "y";
    private final static String PARAM_CREATION_DATE = "creationDate";
    private final static String PARAM_MINIMAL_POINT = "minimalPoint";
    private final static String PARAM_DIFFICULTY = "difficulty";
    private final static String PARAM_DISCIPLINE_NAME = "name";
    private final static String PARAM_DISCIPLINE_LECTURE_HOURS = "lectureHours";

    private String name;
    private Long coordinatesX;
    private Integer coordinatesY;
    private String creationDate;
    private Integer minimalPoint;
    private Difficulty difficulty;
    private String disciplineName;
    private Long disciplineLectureHours;

    public String[] sortingParams;
    public int pageSize;
    public int pageIndex;

    public LabWorkFilterConfiguration(String name, String coordinatesX, String coordinatesY,
                                      String creationDate, String minimalPoint, String difficulty,
                                      String disciplineName, String disciplineLectureHours,
                                      String[] sortingParams, String pageSize, String pageIndex) {
        this.name = name;
        this.coordinatesX = coordinatesX == null ? null : Long.parseLong(coordinatesX);
        this.coordinatesY = coordinatesY == null ? null : Integer.parseInt(coordinatesY);
        this.creationDate = creationDate;
        this.minimalPoint = minimalPoint == null ? null : Integer.parseInt(minimalPoint);
        this.difficulty = difficulty == null ? null : Difficulty.valueOf(difficulty);
        this.disciplineName = disciplineName;
        this.disciplineLectureHours = disciplineLectureHours == null ? null : Long.parseLong(disciplineLectureHours);
        this.sortingParams = sortingParams;
        this.pageSize = pageSize == null ? 10 : Integer.parseInt(pageSize);
        this.pageIndex = pageIndex == null ? 1 : Integer.parseInt(pageIndex);
    }

    public Predicate getPredicate(Root<LabWork> from, Join<LabWork, Coordinates> joinCoordinates,
                                  Join<LabWork, Discipline> joinDiscipline, CriteriaBuilder criteriaBuilder)
    {
        List<Predicate> predicateList = new ArrayList<>();

        if (name != null) {
            predicateList.add(criteriaBuilder.like(from.get(PARAM_NAME), getLike(name)));
        }

        if (coordinatesX != null) {
            predicateList.add(criteriaBuilder.equal(joinCoordinates.get(PARAM_COORDINATES_X), coordinatesX));
        }

        if (coordinatesY != null) {
            predicateList.add(criteriaBuilder.equal(joinCoordinates.get(PARAM_COORDINATES_Y), coordinatesY));
        }

        if (creationDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(creationDate, formatter);

            predicateList.add(criteriaBuilder.greaterThanOrEqualTo(from.get(PARAM_CREATION_DATE), date.atStartOfDay(ZoneId.systemDefault())));
            predicateList.add(criteriaBuilder.lessThanOrEqualTo(from.get(PARAM_CREATION_DATE), date.plusDays(1).atStartOfDay(ZoneId.systemDefault())));
        }

        if (minimalPoint != null) {
            predicateList.add(criteriaBuilder.equal(from.get(PARAM_MINIMAL_POINT), minimalPoint));
        }

        if (difficulty != null) {
            predicateList.add(criteriaBuilder.equal(from.get(PARAM_DIFFICULTY), difficulty));
        }

        if (disciplineName != null) {
            predicateList.add(criteriaBuilder.like(joinDiscipline.get(PARAM_DISCIPLINE_NAME), getLike(disciplineName)));
        }

        if (disciplineLectureHours != null) {
            predicateList.add(criteriaBuilder.equal(joinDiscipline.get(PARAM_DISCIPLINE_LECTURE_HOURS), disciplineLectureHours));
        }

        return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
    }

    private String getLike(String s) {
        return "%" + s + "%";
    }
}

