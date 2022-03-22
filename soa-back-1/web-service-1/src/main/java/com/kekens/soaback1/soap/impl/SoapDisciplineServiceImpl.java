package com.kekens.soaback1.soap.impl;

import com.kekens.soaback1.model.Discipline;
import com.kekens.soaback1.service.DisciplineService;
import com.kekens.soaback1.soap.SoapDisciplineService;
import com.kekens.soaback1.soap.fault.SoapFault;
import com.kekens.soaback1.util.RemoteBeanLookupUtil;
import com.kekens.soaback1.validator.IntegrityError;
import com.kekens.soaback1.validator.exception.IncorrectDataException;

import javax.jws.WebService;
import java.util.List;
import java.util.stream.Collectors;

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
    public Discipline getDisciplineById(String id) throws SoapFault {
        try {
            Discipline discipline = disciplineService.findDisciplineById(Integer.parseInt(id));
            return discipline;
        } catch (IncorrectDataException e) {
            throw new SoapFault(e.toString(), 404);
        }
    }

    @Override
    public void createDiscipline(Discipline discipline) throws SoapFault {
        try {
            disciplineService.createDiscipline(discipline);
        } catch (IncorrectDataException e) {
            throw new SoapFault(e.toString(), 400);
        }
    }

    @Override
    public void updateDiscipline(String id, Discipline discipline) throws SoapFault {
        try {
            discipline.setId(disciplineService.findDisciplineById(Integer.parseInt(id)).getId());
            disciplineService.updateDiscipline(discipline);
        } catch (IncorrectDataException e) {
            if (e.getErrorList().stream().map(IntegrityError::getCode).collect(Collectors.toList()).contains(404)) {
                throw new SoapFault(e.toString(), 404);
            } else {
                throw new SoapFault(e.toString(), 400);
            }
        }
    }

    @Override
    public void deleteDiscipline(String id) throws SoapFault {
        try {
            Discipline discipline = disciplineService.findDisciplineById(Integer.parseInt(id));
            disciplineService.deleteDiscipline(discipline);
        } catch (IncorrectDataException e) {
            if (e.getErrorList().stream().map(IntegrityError::getCode).collect(Collectors.toList()).contains(404)) {
                throw new SoapFault(e.toString(), 404);
            } else {
                throw new SoapFault(e.toString(), 400);
            }
        }
    }
}
