package com.kekens.soa_lab_1.service.impl;

import com.kekens.soa_lab_1.dao.LabWorkDao;
import com.kekens.soa_lab_1.model.LabWork;
import com.kekens.soa_lab_1.service.LabWorkService;
import com.kekens.soa_lab_1.validator.LabWorkValidator;
import com.kekens.soa_lab_1.validator.exception.IncorrectDataException;

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
        List<String> errorList = labWorkValidator.validateLabWork(labWork);

        if (errorList.size() > 0) {
            throw new IncorrectDataException(errorList);
        } else {
            labWorkDao.save(labWork);
        }
    }

    @Override
    public void updateLabWork(LabWork labWork) throws IncorrectDataException {
        List<String> errorList = labWorkValidator.validateLabWork(labWork);

        if (errorList.size() > 0) {
            throw new IncorrectDataException(errorList);
        } else {
            labWorkDao.update(labWork);
        }
    }

    @Override
    public void deleteLabWork(int id) {
        labWorkDao.delete(id);
    }
}
