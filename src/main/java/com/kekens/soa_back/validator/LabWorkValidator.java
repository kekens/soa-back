package com.kekens.soa_back.validator;

import com.kekens.soa_back.dao.DisciplineDao;
import com.kekens.soa_back.model.Discipline;
import com.kekens.soa_back.model.LabWork;

import java.util.ArrayList;
import java.util.List;

public class LabWorkValidator {

    public List<IntegrityError> validateLabWork(LabWork labWork) {
        List<IntegrityError> errorList = new ArrayList<>();
        CoordinatesValidator coordinatesValidator = new CoordinatesValidator();
        DisciplineValidator disciplineValidator = new DisciplineValidator();
        DisciplineDao disciplineDao = new DisciplineDao();

        if ((labWork.getName() == null) || (labWork.getName().isEmpty())) {
            errorList.add(new IntegrityError(400, "LabWork name mustn't be empty"));
        }

        if ((labWork.getCoordinates() == null)) {
            errorList.add(new IntegrityError(400, "LabWork coordinates mustn't be empty"));
        } else {
            errorList.addAll(coordinatesValidator.validateCoordinates(labWork.getCoordinates()));
        }

        if ((labWork.getMinimalPoint() != null) && (labWork.getMinimalPoint() <= 0)) {
            errorList.add(new IntegrityError(400, "LabWork minimal point must be null or more than 0"));
        }

        if ((labWork.getDiscipline() == null)) {
            errorList.add(new IntegrityError(400, "LabWork discipline mustn't be empty"));
        } else {
            errorList.addAll(disciplineValidator.validateDiscipline(labWork.getDiscipline()));

            int id = labWork.getDiscipline().getId();

            if (id < 1) {
                errorList.add(new IntegrityError(400, String.format("Incorrect value '%d' of discipline ID", id)));
            }

            Discipline discipline = disciplineDao.findById(id);

            if ((discipline == null) || (!discipline.equals(labWork.getDiscipline()))) {
                errorList.add(new IntegrityError(400, String.format("Cannot find this discipline: %s", labWork.getDiscipline().toString())));
            }
        }


        return errorList;
    }

}
