package com.kekens.service.impl;

import com.kekens.dao.DisciplineDao;
import com.kekens.model.Discipline;
import com.kekens.service.DisciplineService;
import com.kekens.service.LabWorkService;
import com.kekens.validator.DisciplineValidator;
import com.kekens.validator.IntegrityError;
import com.kekens.validator.exception.IncorrectDataException;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.Collections;
import java.util.List;

@Stateless
@Remote(DisciplineService.class)
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

        if (!errorList.isEmpty()) {
            throw new IncorrectDataException(errorList);
        } else {
            disciplineDao.save(discipline);
        }
    }

    @Override
    public void updateDiscipline(Discipline discipline) throws IncorrectDataException {
        List<IntegrityError> errorList = disciplineValidator.validateDiscipline(discipline);

        if (!errorList.isEmpty()) {
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