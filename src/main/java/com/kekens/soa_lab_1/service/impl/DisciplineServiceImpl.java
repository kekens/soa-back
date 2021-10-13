package com.kekens.soa_lab_1.service.impl;

import com.kekens.soa_lab_1.dao.DisciplineDao;
import com.kekens.soa_lab_1.model.Discipline;
import com.kekens.soa_lab_1.service.DisciplineService;
import com.kekens.soa_lab_1.validator.DisciplineValidator;
import com.kekens.soa_lab_1.validator.IntegrityError;
import com.kekens.soa_lab_1.validator.exception.IncorrectDataException;

import java.util.Collections;
import java.util.List;

public class DisciplineServiceImpl implements DisciplineService {

    private final DisciplineDao disciplineDao = new DisciplineDao();
    private final DisciplineValidator disciplineValidator = new DisciplineValidator();

    @Override
    public Discipline findDisciplineById(int id) throws IncorrectDataException {
        Discipline discipline = disciplineDao.findById(id);

        if (discipline == null) {
            throw new IncorrectDataException(Collections.singletonList(new IntegrityError(404, "Not found Discipline with that ID: " + id)));
        } else {
            return discipline;
        }
    }

    @Override
    public void createDiscipline(Discipline discipline) throws IncorrectDataException {
        List<IntegrityError> errorList = disciplineValidator.validateDiscipline(discipline);

        if (errorList.size() > 0) {
            throw new IncorrectDataException(errorList);
        } else {
            disciplineDao.save(discipline);
        }
    }

    @Override
    public void updateDiscipline(Discipline discipline) throws IncorrectDataException {
        List<IntegrityError> errorList = disciplineValidator.validateDiscipline(discipline);

        if (errorList.size() > 0) {
            throw new IncorrectDataException(errorList);
        } else {
            disciplineDao.update(discipline);
        }
    }

    @Override
    public void deleteDiscipline(Discipline discipline) {
        disciplineDao.delete(discipline);
    }

    @Override
    public List<Discipline> findAllDisciplines() {
        return disciplineDao.findAll();
    }
}