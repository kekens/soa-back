package com.kekens.soa_lab_1.service;

import com.kekens.soa_lab_1.model.LabWork;
import com.kekens.soa_lab_1.util.LabWorkFilterConfiguration;
import com.kekens.soa_lab_1.validator.exception.IncorrectDataException;

import java.util.List;

public interface LabWorkService {

    LabWork findLabWorkById(int id);
    void createLabWork(LabWork labWork) throws IncorrectDataException;
    void updateLabWork(LabWork labWork) throws IncorrectDataException;
    void deleteLabWork(int id) throws IncorrectDataException;
    List<LabWork> findAllLabWorks(LabWorkFilterConfiguration labWorkFilterConfiguration);

    void deleteLabWorkByDifficulty(String diff) throws IncorrectDataException;
    int getCountLabWorkByDifficulty(String diff) throws IncorrectDataException;
    List<LabWork> findAllLabWorkByName(String name);
}
