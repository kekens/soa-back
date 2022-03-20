package com.kekens.soaback1.util;

import lombok.Data;
import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Map;

@Getter
@Data
@XmlRootElement(name = "filter")
@XmlAccessorType(XmlAccessType.FIELD)
public class LabWorkFilterConfiguration implements Serializable {

    @XmlElement
    private String[] nameStrArray;
    @XmlElement
    private String[] coordinatesXStrArray;
    @XmlElement
    private String[] coordinatesYStrArray;
    @XmlElement
    private String[] creationDateStrArray;
    @XmlElement
    private String[] minimalPointStrArray;
    @XmlElement
    private String[] difficultyStrArray;
    @XmlElement
    private String[] disciplineNameStrArray;
    @XmlElement
    private String[] disciplineLectureHoursStrArray;

    @XmlElement
    public String[] sortingParams;
    @XmlElement
    public String pageSize;
    @XmlElement
    public String pageIndex;

//    public LabWorkFilterConfiguration(Map<String, String[]> parameterMap) {
//        this.nameStrArray = parameterMap.get("name");
//        this.coordinatesXStrArray = parameterMap.get("coordinates_x");
//        this.coordinatesYStrArray = parameterMap.get("coordinates_y");
//        this.creationDateStrArray = parameterMap.get("creationDate");
//        this.minimalPointStrArray = parameterMap.get("minimalPoint");
//        this.difficultyStrArray = parameterMap.get("difficulty");
//        this.disciplineNameStrArray = parameterMap.get("disciplineName");
//        this.disciplineLectureHoursStrArray = parameterMap.get("disciplineLectureHours");
//        this.sortingParams = parameterMap.get("sort");
//        this.pageSize = parameterMap.get("count") != null ? parameterMap.get("count")[0] : null;
//        this.pageIndex = parameterMap.get("page") != null ? parameterMap.get("page")[0] : null;
//    }

}

