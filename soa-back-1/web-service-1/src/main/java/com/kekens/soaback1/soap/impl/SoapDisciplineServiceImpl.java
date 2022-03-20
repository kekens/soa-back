package com.kekens.soaback1.soap.impl;

import com.kekens.soaback1.model.Discipline;
import com.kekens.soaback1.service.DisciplineService;
import com.kekens.soaback1.soap.SoapDisciplineService;
import com.kekens.soaback1.util.RemoteBeanLookupUtil;
import com.kekens.soaback1.validator.exception.IncorrectDataException;

import javax.jws.WebService;
import java.util.List;

@WebService(endpointInterface = "com.kekens.soaback1.soap.SoapDisciplineService")
public class SoapDisciplineServiceImpl implements SoapDisciplineService {

    private final DisciplineService disciplineService;

    public SoapDisciplineServiceImpl() throws IncorrectDataException {
        this.disciplineService = RemoteBeanLookupUtil.lookupDisciplineBean();
    }

    @Override
    public List<Discipline> getAllDisciplines() {
        List<Discipline> disciplineList = disciplineService.findAllDisciplines();
        return disciplineList;
    }

    @Override
    public Discipline getDisciplineById(String id) throws IncorrectDataException {
        Discipline discipline = disciplineService.findDisciplineById(Integer.parseInt(id));
        return discipline;
    }

    @Override
    public void createDiscipline(Discipline discipline) throws IncorrectDataException {
        disciplineService.createDiscipline(discipline);
    }

    @Override
    public void updateDiscipline(String id, Discipline discipline) throws IncorrectDataException {
        discipline.setId(disciplineService.findDisciplineById(Integer.parseInt(id)).getId());
        disciplineService.updateDiscipline(discipline);
    }

    @Override
    public void deleteDiscipline(String id) throws IncorrectDataException {
        Discipline discipline = disciplineService.findDisciplineById(Integer.parseInt(id));
        disciplineService.deleteDiscipline(discipline);
    }
}
