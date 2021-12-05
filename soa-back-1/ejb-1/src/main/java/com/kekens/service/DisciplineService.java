package com.kekens.service;

import com.kekens.model.Discipline;
import com.kekens.validator.exception.IncorrectDataException;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface DisciplineService {

    Discipline findDisciplineById(int id) throws IncorrectDataException;
    void createDiscipline(Discipline discipline) throws IncorrectDataException;
    void updateDiscipline(Discipline discipline) throws IncorrectDataException;
    void deleteDiscipline(Discipline discipline);
    List<Discipline> findAllDisciplines();

}
