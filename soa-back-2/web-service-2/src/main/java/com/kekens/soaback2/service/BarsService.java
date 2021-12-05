package com.kekens.soaback2.service;

import com.kekens.soaback2.validator.exception.IncorrectDataException;

import javax.ejb.Remote;

@Remote
public interface BarsService {

    void decreaseDifficulty(int labworkId, int stepCount) throws IncorrectDataException;
    void deleteLabWorkFromDiscipline(int disciplineId, int labworkId) throws IncorrectDataException;

}