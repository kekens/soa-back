package com.kekens.soaback1.service;

import com.kekens.soaback1.model.LabWork;
import com.kekens.soaback1.validator.exception.IncorrectDataException;
import com.kekens.soaback1.util.LabWorkFilterConfiguration;

import javax.ejb.Remote;
import java.util.List;

@Remote
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
