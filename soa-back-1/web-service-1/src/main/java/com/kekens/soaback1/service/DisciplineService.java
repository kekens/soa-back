package com.kekens.soaback1.service;

import com.kekens.soaback1.model.Discipline;
import com.kekens.soaback1.validator.exception.IncorrectDataException;

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
