package com.kekens.soaback1.service.impl;

import com.kekens.soaback1.dao.LabWorkDao;
import com.kekens.soaback1.model.Difficulty;
import com.kekens.soaback1.model.LabWork;
import com.kekens.soaback1.service.LabWorkService;
import com.kekens.soaback1.util.LabWorkFilterConfiguration;
import com.kekens.soaback1.util.LabWorkFilterUtil;
import com.kekens.soaback1.validator.IntegrityError;
import com.kekens.soaback1.validator.LabWorkValidator;
import com.kekens.soaback1.validator.exception.IncorrectDataException;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Stateless
@Remote(LabWorkService.class)
public class LabWorkServiceImpl implements LabWorkService {

    private final LabWorkDao labWorkDao = new LabWorkDao();
    private final LabWorkValidator labWorkValidator = new LabWorkValidator();

    @Override
    public LabWork findLabWorkById(int id) throws IncorrectDataException {
        LabWork labWork = labWorkDao.findById(id);

        if (labWork == null) {
            throw new IncorrectDataException(Collections.singletonList(new IntegrityError(404, "Not found LabWork with that ID: " + id)));
        } else {
            return labWork;
        }
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

        if (errorList.size() > 0) {
            throw new IncorrectDataException(errorList);
        } else {
            labWorkDao.update(labWork);
        }
    }

    @Override
    public void deleteLabWork(LabWork labWork) {
        labWorkDao.delete(labWork);
    }

    @Override
    public List<LabWork> findAllLabWorks(LabWorkFilterConfiguration labWorkFilterConfiguration) throws IncorrectDataException {

        LabWorkFilterUtil labWorkFilterUtil = new LabWorkFilterUtil(labWorkFilterConfiguration);
        List<IntegrityError> errorList = new ArrayList<>(labWorkFilterUtil.validateFilterConfiguration());

        if (errorList.size() > 0) {
            throw new IncorrectDataException(errorList);
        }

        return labWorkDao.findAllFiltering(labWorkFilterUtil);
    }

    @Override
    public LabWork deleteLabWorkByDifficulty(String diff) throws IncorrectDataException {
        Difficulty difficulty;

        try {
            difficulty = Difficulty.valueOf(diff);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IncorrectDataException(Collections.singletonList(new IntegrityError(400, "Incorrect value of difficulty")));
        }

        List<LabWork> listLabWork = labWorkDao.findAll();

        for (LabWork labWork : listLabWork) {
            if (labWork.getDifficulty() == difficulty) {
                deleteLabWork(labWork);
                return labWork;
            }
        }

        return null;
    }

    @Override
    public int getCountLabWorkByDifficulty(String diff) throws IncorrectDataException {
        int count = 0;
        Difficulty difficulty;

        try {
            difficulty = Difficulty.valueOf(diff);
        } catch (IllegalArgumentException e) {
            throw new IncorrectDataException(Collections.singletonList(new IntegrityError(400, "Incorrect value of difficulty")));
        }

        List<LabWork> listLabWork = labWorkDao.findAll();

        for (LabWork labWork : listLabWork) {
            if ((labWork.getDifficulty() != null) && (labWork.getDifficulty().value > difficulty.value)) {
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
