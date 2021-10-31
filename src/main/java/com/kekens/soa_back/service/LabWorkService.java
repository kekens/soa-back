package com.kekens.soa_back.service;

import com.kekens.soa_back.model.LabWork;
import com.kekens.soa_back.util.LabWorkFilterConfiguration;
import com.kekens.soa_back.validator.exception.IncorrectDataException;

import java.util.List;

public interface LabWorkService {

    LabWork findLabWorkById(int id) throws IncorrectDataException;
    void createLabWork(LabWork labWork) throws IncorrectDataException;
    void updateLabWork(LabWork labWork) throws IncorrectDataException;
    void deleteLabWork(LabWork labWork);
    List<LabWork> findAllLabWorks(LabWorkFilterConfiguration labWorkFilterConfiguration) throws IncorrectDataException;

    LabWork deleteLabWorkByDifficulty(String diff) throws IncorrectDataException;
    int getCountLabWorkByDifficulty(String diff) throws IncorrectDataException;
    List<LabWork> findAllLabWorkByName(String name);
}
