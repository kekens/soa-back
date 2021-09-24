package com.kekens.soa_lab_1.validator;

import com.kekens.soa_lab_1.model.Discipline;

import java.util.ArrayList;
import java.util.List;

public class DisciplineValidator {

    public List<String> validateDiscipline(Discipline discipline) {
        List<String> errorList = new ArrayList<>();

        if ((discipline.getName() == null) || (discipline.getName().isEmpty())) {
            errorList.add("Y coordinate mustn't be empty");
        }

        return errorList;
    }

}
