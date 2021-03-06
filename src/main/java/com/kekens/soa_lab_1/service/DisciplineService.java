package com.kekens.soa_lab_1.service;

import com.kekens.soa_lab_1.model.Discipline;
import com.kekens.soa_lab_1.validator.exception.IncorrectDataException;

import java.util.List;

public interface DisciplineService {

    Discipline findDisciplineById(int id) throws IncorrectDataException;
    void createDiscipline(Discipline discipline) throws IncorrectDataException;
    void updateDiscipline(Discipline discipline) throws IncorrectDataException;
    void deleteDiscipline(Discipline discipline);
    List<Discipline> findAllDisciplines();

}
