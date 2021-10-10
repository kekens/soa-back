package com.kekens.soa_lab_1.util;

import com.kekens.soa_lab_1.model.Coordinates;
import com.kekens.soa_lab_1.model.Difficulty;
import com.kekens.soa_lab_1.model.Discipline;
import com.kekens.soa_lab_1.model.LabWork;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LabWorkFilterConfiguration {

    private final static String PARAM_NAME = "name";
    private final static String PARAM_COORDINATES_X = "x";
    private final static String PARAM_COORDINATES_Y = "y";
    private final static String PARAM_CREATION_DATE = "creationDate";
    private final static String PARAM_MINIMAL_POINT = "minimalPoint";
    private final static String PARAM_DIFFICULTY = "difficulty";
    private final static String PARAM_DISCIPLINE_NAME = "name";
    private final static String PARAM_DISCIPLINE_LECTURE_HOURS = "lectureHours";

    private String[] nameStrArray;
    private String[] coordinatesXStrArray;
    private String[] coordinatesYStrArray;
    private String[] creationDateStrArray;
    private String[] minimalPointStrArray;
    private String[] difficultyStrArray;
    private String[] disciplineNameStrArray;
    private String[] disciplineLectureHoursStrArray;

    public String[] sortingParams;
    public int pageSize;
    public int pageIndex;

    public LabWorkFilterConfiguration(Map<String, String[]> parameterMap) {
        this.nameStrArray = parameterMap.get("name");
        this.coordinatesXStrArray = parameterMap.get("coordinates_x");
        this.coordinatesYStrArray = parameterMap.get("coordinates_y");
        this.creationDateStrArray = parameterMap.get("creationDate");
        this.minimalPointStrArray = parameterMap.get("minimalPoint");
        this.difficultyStrArray = parameterMap.get("difficulty");
        this.disciplineNameStrArray = parameterMap.get("disciplineName");
        this.disciplineLectureHoursStrArray = parameterMap.get("disciplineLectureHours");
        this.sortingParams = parameterMap.get("sort");
        this.pageSize = parameterMap.get("count") == null ? 1000 : Integer.parseInt(parameterMap.get("count")[0]);
        this.pageIndex = parameterMap.get("page") == null ? 1 : Integer.parseInt(parameterMap.get("page")[0]);
    }

    public List<Order> setOrder(Root<LabWork> from, Join<LabWork, Coordinates> joinCoordinates,
                                Join<LabWork, Discipline> joinDiscipline, CriteriaBuilder criteriaBuilder)
    {
        List<Order> orderList = new ArrayList<>();

        if (sortingParams != null) {

            for (String sortParam : sortingParams) {
                String[] args = sortParam.split(":");

                if ((args[0].startsWith("coordinates_")) || (args[0].startsWith("discipline_"))) {
                    boolean isCoordinates = args[0].startsWith("coordinates_");
                    Join<?,?> join = isCoordinates ? joinCoordinates : joinDiscipline;
                    args[0] = isCoordinates ? args[0].replaceAll("coordinates_", "") : args[0].replaceAll("discipline_", "");

                    if ((args.length == 1) || ((args.length == 2) && (args[1].equals("asc")))) {
                        orderList.add(criteriaBuilder.asc(join.get(args[0])));
                    } else if (args.length == 2) {
                        orderList.add(criteriaBuilder.desc(join.get(args[0])));
                    }
                } else {
                    if ((args.length == 1) || ((args.length == 2) && (args[1].equals("asc")))) {
                        orderList.add(criteriaBuilder.asc(from.get(sortParam)));
                    } else if (args.length == 2) {
                        orderList.add(criteriaBuilder.desc(from.get(args[0])));
                    }
                }
            }

        }

        return orderList;
    }

    public Predicate getPredicate(Root<LabWork> from, Join<LabWork, Coordinates> joinCoordinates,
                                  Join<LabWork, Discipline> joinDiscipline, CriteriaBuilder criteriaBuilder)
    {
        List<Predicate> predicateList = new ArrayList<>();

        addStringPredicates(from, criteriaBuilder, predicateList, nameStrArray, PARAM_NAME);
        addNumberPredicates(joinCoordinates, criteriaBuilder, predicateList, coordinatesXStrArray, PARAM_COORDINATES_X);
        addNumberPredicates(joinCoordinates, criteriaBuilder, predicateList, coordinatesYStrArray, PARAM_COORDINATES_Y);
        addNumberPredicates(from, criteriaBuilder, predicateList, minimalPointStrArray, PARAM_MINIMAL_POINT);
        addStringPredicates(joinDiscipline, criteriaBuilder, predicateList, disciplineNameStrArray, PARAM_DISCIPLINE_NAME);
        addNumberPredicates(joinDiscipline, criteriaBuilder, predicateList, disciplineLectureHoursStrArray, PARAM_DISCIPLINE_LECTURE_HOURS);

        if (difficultyStrArray != null) {
            for (String diffStr: difficultyStrArray) {
                String[] args = diffStr.split(":");

                if (args.length == 1) {
                    predicateList.add(criteriaBuilder.equal(from.get(PARAM_DIFFICULTY), Difficulty.valueOf(diffStr)));
                } else {
                    // TODO Filtering by less and more for Difficulty
//                    date = LocalDate.parse(args[1], formatter);
//                    ZonedDateTime zonedDateTime = date.atStartOfDay(ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS);
//                    switch (args[0]) {
//                        case ">":
//                            predicateList.add(criteriaBuilder.greaterThanOrEqualTo(from.get(PARAM_CREATION_DATE), zonedDateTime.plusDays(1)));
//                            break;
//                        case "<":
//                            predicateList.add(criteriaBuilder.lessThan(from.get(PARAM_CREATION_DATE), zonedDateTime));
//                            break;
//                    }
                }
            }


        }

        if (creationDateStrArray != null) {
            for (String dateStr: creationDateStrArray) {
                String[] args = dateStr.split(":");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date;

                if (args.length == 1) {
                    date = LocalDate.parse(dateStr, formatter);
                    predicateList.add(criteriaBuilder.greaterThanOrEqualTo(from.get(PARAM_CREATION_DATE), date.atStartOfDay(ZoneId.systemDefault())));
                    predicateList.add(criteriaBuilder.lessThanOrEqualTo(from.get(PARAM_CREATION_DATE), date.plusDays(1).atStartOfDay(ZoneId.systemDefault())));
                } else {
                    date = LocalDate.parse(args[1], formatter);
                    ZonedDateTime zonedDateTime = date.atStartOfDay(ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS);
                    switch (args[0]) {
                        case ">":
                            predicateList.add(criteriaBuilder.greaterThanOrEqualTo(from.get(PARAM_CREATION_DATE), zonedDateTime.plusDays(1)));
                            break;
                        case "<":
                            predicateList.add(criteriaBuilder.lessThan(from.get(PARAM_CREATION_DATE), zonedDateTime));
                            break;
                        case ">=":
                            predicateList.add(criteriaBuilder.greaterThanOrEqualTo(from.get(PARAM_CREATION_DATE), zonedDateTime));
                            break;
                        case "<=":
                            predicateList.add(criteriaBuilder.lessThan(from.get(PARAM_CREATION_DATE), zonedDateTime.plusDays(1)));
                            break;
                    }
                }
            }

        }

        return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
    }

    private <T extends From<?,?>> void addNumberPredicates(T object, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList,
                                         String[] strArray, String param) {
        if (strArray != null) {
            for (String numStr : strArray) {
                String[] args = numStr.split(":");

                Long num;

                if (args.length == 1) {
                    num = Long.parseLong(numStr);
                    predicateList.add(criteriaBuilder.equal(object.get(param), num));
                } else {
                    num = Long.parseLong(args[1]);
                    switch (args[0]) {
                        case ">=":
                            predicateList.add(criteriaBuilder.greaterThanOrEqualTo(object.get(param), num));
                            break;
                        case "<=":
                            predicateList.add(criteriaBuilder.lessThanOrEqualTo(object.get(param), num));
                            break;
                        case ">":
                            predicateList.add(criteriaBuilder.greaterThan(object.get(param), num));
                            break;
                        case "<":
                            predicateList.add(criteriaBuilder.lessThan(object.get(param), num));
                            break;
                    }
                }
            }
        }
    }

    private <T extends From<?,?>> void addStringPredicates(T object, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList,
                                     String[] strArray, String param) {
        if (strArray != null) {
            for (String str : strArray) {
                String[] args = str.split(":");

                if (args.length == 1) {
                    predicateList.add(criteriaBuilder.like(criteriaBuilder.upper(object.get(param)), getLike(str)));
                } else {
                    switch (args[0]) {
                        case ">=":
                            predicateList.add(criteriaBuilder.greaterThanOrEqualTo(object.get(param), args[1]));
                            break;
                        case "<=":
                            predicateList.add(criteriaBuilder.lessThanOrEqualTo(object.get(param), args[1]));
                            break;
                        case ">":
                            predicateList.add(criteriaBuilder.greaterThan(object.get(param), args[1]));
                            break;
                        case "<":
                            predicateList.add(criteriaBuilder.lessThan(object.get(param), args[1]));
                            break;
                    }
                }
            }
        }
    }

    private String getLike(String s) {
        return "%" + s.toUpperCase() + "%";
    }
}

