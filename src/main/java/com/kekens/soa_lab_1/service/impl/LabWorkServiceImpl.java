package com.kekens.soa_lab_1.service.impl;

import com.kekens.soa_lab_1.dao.LabWorkDao;
import com.kekens.soa_lab_1.model.Difficulty;
import com.kekens.soa_lab_1.model.LabWork;
import com.kekens.soa_lab_1.service.LabWorkService;
import com.kekens.soa_lab_1.util.LabWorkFilterConfiguration;
import com.kekens.soa_lab_1.validator.IntegrityError;
import com.kekens.soa_lab_1.validator.LabWorkValidator;
import com.kekens.soa_lab_1.validator.exception.IncorrectDataException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LabWorkServiceImpl implements LabWorkService {

    private final LabWorkDao labWorkDao = new LabWorkDao();
    private final LabWorkValidator labWorkValidator = new LabWorkValidator();

    @Override
    public LabWork findLabWorkById(int id) {
        return labWorkDao.findById(id);
    }

    @Override
    public void createLabWork(LabWork labWork) throws IncorrectDataException {
        List<IntegrityError> errorList = labWorkValidator.validateLabWork(labWork);

        if (errorList.size() > 0) {
            throw new IncorrectDataException(errorList);
        } else {
            labWorkDao.save(labWork);
        }
    }

    @Override
    public void updateLabWork(LabWork labWork) throws IncorrectDataException {
        List<IntegrityError> errorList = labWorkValidator.validateLabWork(labWork);

        if (findLabWorkById(labWork.getId()) == null) {
            errorList.add(new IntegrityError(104, "Not found LabWork with that ID"));
        }

        if (errorList.size() > 0) {
            throw new IncorrectDataException(errorList);
        } else {
            labWorkDao.update(labWork);
        }
    }

    @Override
    public void deleteLabWork(int id) throws IncorrectDataException {
        LabWork labWork = findLabWorkById(id);

        if (labWork == null) {
            throw new IncorrectDataException(Collections.singletonList(new IntegrityError(104, "Not found LabWork with that ID")));
        } else {
            labWorkDao.delete(labWork);
        }
    }

    @Override
    public List<LabWork> findAllLabWorks(LabWorkFilterConfiguration labWorkFilterConfiguration) {
        return labWorkDao.findAllFiltering(labWorkFilterConfiguration);
    }

    @Override
    public void deleteLabWorkByDifficulty(String diff) throws IncorrectDataException {
        Difficulty difficulty;

        try {
            difficulty = Difficulty.valueOf(diff);
        } catch (IllegalArgumentException e) {
            throw new IncorrectDataException(Collections.singletonList(new IntegrityError(103, "Incorrect value of difficulty")));
        }

        List<LabWork> listLabWork = labWorkDao.findAll();

        for (LabWork labWork : listLabWork) {
            if (labWork.getDifficulty() == difficulty) {
                deleteLabWork(labWork.getId());
                break;
            }
        }
    }

    @Override
    public int getCountLabWorkByDifficulty(String diff) throws IncorrectDataException {
        int count = 0;
        Difficulty difficulty;

        try {
            difficulty = Difficulty.valueOf(diff);
        } catch (IllegalArgumentException e) {
            throw new IncorrectDataException(Collections.singletonList(new IntegrityError(103, "Incorrect value of difficulty")));
        }

        List<LabWork> listLabWork = labWorkDao.findAll();

        for (LabWork labWork : listLabWork) {
            if (labWork.getDifficulty().value > difficulty.value) {
                count++;
            }
        }

        return count;
    }

    @Override
    public List<LabWork> findAllLabWorkByName(String name) {
        List<LabWork> resultList = new ArrayList<>();
        List<LabWork> allLabWorks = labWorkDao.findAll();

        for (LabWork labWork : allLabWorks) {
            if (labWork.getName().contains(name)) {
                resultList.add(labWork);
            }
        }

        return resultList;
    }

}
