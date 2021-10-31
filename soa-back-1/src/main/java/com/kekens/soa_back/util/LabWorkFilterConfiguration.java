package com.kekens.soa_back.util;

import com.kekens.soa_back.model.Coordinates;
import com.kekens.soa_back.model.Difficulty;
import com.kekens.soa_back.model.Discipline;
import com.kekens.soa_back.model.LabWork;
import com.kekens.soa_back.validator.IntegrityError;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
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

    private final String[] nameStrArray;
    private final String[] coordinatesXStrArray;
    private final String[] coordinatesYStrArray;
    private final String[] creationDateStrArray;
    private final String[] minimalPointStrArray;
    private final String[] difficultyStrArray;
    private final String[] disciplineNameStrArray;
    private final String[] disciplineLectureHoursStrArray;

    public String[] sortingParams;
    public String pageSize;
    public String pageIndex;

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
        this.pageSize = parameterMap.get("count") != null ? parameterMap.get("count")[0] : null;
        this.pageIndex = parameterMap.get("page") != null ? parameterMap.get("page")[0] : null;
    }

    public List<IntegrityError> validateFilterConfiguration() {
        List<IntegrityError> errorList = new ArrayList<>();

        // Number filter validation
        errorList.addAll(validateNumberFilter(this.coordinatesXStrArray, PARAM_COORDINATES_X));
        errorList.addAll(validateNumberFilter(this.coordinatesYStrArray, PARAM_COORDINATES_Y));
        errorList.addAll(validateNumberFilter(this.minimalPointStrArray, PARAM_MINIMAL_POINT));
        errorList.addAll(validateNumberFilter(this.disciplineLectureHoursStrArray, PARAM_DISCIPLINE_LECTURE_HOURS));

        // Difficulty filter validation
        if (this.difficultyStrArray != null) {
            for (String dif : this.difficultyStrArray) {
                String[] args = dif.split(":");
                String difStr = args.length == 1 ? dif : args[1];

                try {
                    Difficulty.valueOf(difStr);
                } catch (IllegalArgumentException e) {
                    errorList.add(new IntegrityError(400, String.format("Incorrect value '%s' of 'difficulty' param", difStr)));
                }
            }
        }

        // Creation date filter validation
        if (this.creationDateStrArray != null) {
            for (String date : this.creationDateStrArray) {
                String[] args = date.split(":");
                String dateStr = args.length == 1 ? date : args[1];

                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate.parse(dateStr, formatter);
                } catch (DateTimeParseException e) {
                    errorList.add(new IntegrityError(400, String.format("Incorrect value '%s' of 'creationDate' param with pattern 'yyyy-MM-dd'", dateStr)));
                }
            }
        }

        // Paging filter validation
        if (this.pageSize != null) {
            try {
                int size = Integer.parseInt(this.pageSize);

                if (size < 1) {
                    errorList.add(new IntegrityError(400, String.format("Incorrect value '%s' of count of rows per page", this.pageSize)));
                }
            } catch (NumberFormatException e) {
                errorList.add(new IntegrityError(400, String.format("Incorrect value '%s' of count of rows per page", this.pageSize)));
            }
        } else {
            this.pageSize = "30";
        }

        if (this.pageIndex != null) {
            try {
                int index = Integer.parseInt(this.pageIndex);

                if (index < 1) {
                    errorList.add(new IntegrityError(400, String.format("Incorrect value '%s' of page index", this.pageIndex)));
                }
            } catch (NumberFormatException e) {
                errorList.add(new IntegrityError(400, String.format("Incorrect value '%s' of page index", this.pageIndex)));
            }
        } else {
            this.pageIndex = "1";
        }

        // Sorting params validation
        final List<String> listSortingParams = new ArrayList<>(Arrays.asList(PARAM_NAME, "coordinates_x", "coordinates_y", PARAM_DIFFICULTY,
                PARAM_CREATION_DATE, PARAM_MINIMAL_POINT, "discipline_name", "discipline_lectureHours"));

        if (this.sortingParams != null) {
            for (String sort: this.sortingParams) {
                String[] args = sort.split(":");
                String sortStr = args.length == 1 ? sort : args[0];

                if (!listSortingParams.contains(sortStr)) {
                    errorList.add(new IntegrityError(400, "Incorrect value '" + sortStr + "' of sorting param"));
                }
            }
        }

        return errorList;
    }

    private List<IntegrityError> validateNumberFilter(String[] strArray, String param) {
        List<IntegrityError> errorList = new ArrayList<>();

        if (strArray != null) {

            for (String str : strArray) {
                String[] args = str.split(":");
                String numStr = args.length == 1 ? str : args[1];

                try {
                    Integer.parseInt(numStr);
                } catch (NumberFormatException e) {
                    errorList.add(new IntegrityError(400, String.format("Incorrect value '%s' of '%s' param", numStr, param)));
                }
            }
        }

        return errorList;
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
                                         String[] numArray, String param) {
        if (numArray != null) {
            for (String numStr : numArray) {
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

