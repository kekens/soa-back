package com.kekens.soaback1.util;

import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

@Getter
public class LabWorkFilterConfiguration implements Serializable {

    private final String[] nameStrArray;
    private final String[] coordinatesXStrArray;
    private final String[] coordinatesYStrArray;
    private final String[] creationDateStrArray;
    private final String[] minimalPointStrArray;
    private final String[] difficultyStrArray;
    private final String[] disciplineNameStrArray;
    private final String[] disciplineLectureHoursStrArray;

    private final String[] sortingParams;
    private final String pageSize;
    private final String pageIndex;

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

}

