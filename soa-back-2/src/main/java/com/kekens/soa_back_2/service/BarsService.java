package com.kekens.soa_back_2.service;

import com.kekens.soa_back_2.validator.exception.IncorrectDataException;

public interface BarsService {

    void decreaseDifficulty(int labworkId, int stepCount) throws IncorrectDataException;
    void deleteLabWorkFromDiscipline(int disciplineId, int labworkId) throws IncorrectDataException;

}