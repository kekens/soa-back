package com.kekens.soa_lab_1.validator;

import com.kekens.soa_lab_1.model.LabWork;

import java.util.ArrayList;
import java.util.List;

public class LabWorkValidator {

    public List<IntegrityError> validateLabWork(LabWork labWork) {
        List<IntegrityError> errorList = new ArrayList<>();
        CoordinatesValidator coordinatesValidator = new CoordinatesValidator();
        DisciplineValidator disciplineValidator = new DisciplineValidator();

        if ((labWork.getName() == null) || (labWork.getName().isEmpty())) {
            errorList.add(new IntegrityError(100, "LabWork name mustn't be empty"));
        }

        if ((labWork.getCoordinates() == null)) {
            errorList.add(new IntegrityError(100, "LabWork coordinates mustn't be empty"));
        }

        if ((labWork.getMinimalPoint() != null) && (labWork.getMinimalPoint() <= 0)) {
            errorList.add(new IntegrityError(100, "LabWork minimal point must be null or more than 0"));
        }

        if ((labWork.getDiscipline() == null)) {
            errorList.add(new IntegrityError(100, "LabWork discipline mustn't be empty"));
        }

        errorList.addAll(coordinatesValidator.validateCoordinates(labWork.getCoordinates()));
        errorList.addAll(disciplineValidator.validateDiscipline(labWork.getDiscipline()));

        return errorList;
    }

}
