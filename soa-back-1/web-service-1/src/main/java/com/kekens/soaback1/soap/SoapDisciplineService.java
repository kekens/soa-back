package com.kekens.soaback1.soap;

import com.kekens.soaback1.model.Discipline;
import com.kekens.soaback1.validator.exception.IncorrectDataException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface SoapDisciplineService {

    @WebMethod
    List<Discipline> getAllDisciplines();

    @WebMethod
    Discipline getDisciplineById(String id) throws IncorrectDataException;

    @WebMethod
    void createDiscipline(Discipline discipline) throws IncorrectDataException;

    @WebMethod
    void updateDiscipline(String id, Discipline discipline) throws IncorrectDataException;

    @WebMethod
    void deleteDiscipline(String id) throws IncorrectDataException;

}
