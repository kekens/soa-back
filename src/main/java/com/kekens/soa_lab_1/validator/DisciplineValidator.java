package com.kekens.soa_lab_1.validator;

import com.kekens.soa_lab_1.model.Discipline;

import java.util.ArrayList;
import java.util.List;

public class DisciplineValidator {

    public List<IntegrityError> validateDiscipline(Discipline discipline) {
        List<IntegrityError> errorList = new ArrayList<>();

        if ((discipline.getName() == null) || (discipline.getName().isEmpty())) {
            errorList.add(new IntegrityError(400, "Discipline name mustn't be empty"));
        }

        if ((discipline.getLectureHours() != null) && (discipline.getLectureHours() < 1)) {
            errorList.add(new IntegrityError(400, "Discipline lecture hours must be more than 0"));
        }

        return errorList;
    }

}
