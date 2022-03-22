package com.kekens.soaback1.soap.impl;

import com.kekens.soaback1.model.LabWork;
import com.kekens.soaback1.service.LabWorkService;
import com.kekens.soaback1.soap.SoapLabWorkService;
import com.kekens.soaback1.soap.fault.SoapFault;
import com.kekens.soaback1.util.LabWorkFilterConfiguration;
import com.kekens.soaback1.util.RemoteBeanLookupUtil;
import com.kekens.soaback1.validator.IntegrityError;
import com.kekens.soaback1.validator.exception.IncorrectDataException;

import javax.jws.WebService;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@WebService(endpointInterface = "com.kekens.soaback1.soap.SoapLabWorkService")
public class SoapLabWorkServiceImpl implements SoapLabWorkService {

    private final LabWorkService labWorkService;

    public SoapLabWorkServiceImpl() throws IncorrectDataException {
        this.labWorkService = RemoteBeanLookupUtil.lookupLabWorkBean();
    }

    @Override
    public List<LabWork> getAllLabWorks(LabWorkFilterConfiguration labWorkFilterConfiguration) throws SoapFault {
        try {
            return labWorkService.findAllLabWorks(labWorkFilterConfiguration);
        } catch (IncorrectDataException e) {
            throw new SoapFault(e.toString(), 400);
        }
    }

    @Override
    public LabWork getLabWork(String id) throws SoapFault {
        try {
            return labWorkService.findLabWorkById(Integer.parseInt(id));
        } catch (IncorrectDataException | NumberFormatException e) {
            throw new SoapFault("Not found labwork with that id: " + id, 404);
        }
    }

    @Override
    public String getCountByDifficultyMore(String difficulty) throws SoapFault {
        try {
            int count = 0;
            return String.valueOf(labWorkService.getCountLabWorkByDifficulty(difficulty));
        } catch (IncorrectDataException e) {
            throw new SoapFault(e.toString(), 400);
        }
    }

    @Override
    public void createLabWork(LabWork labWork) throws SoapFault {
        try {
           labWorkService.createLabWork(labWork);
        } catch (IncorrectDataException e) {
            throw new SoapFault(e.toString(), 400);
        }
    }

    @Override
    public void updateLabWork(String id, LabWork labWork) throws SoapFault {
        try {
            labWork.setId(labWorkService.findLabWorkById(Integer.parseInt(id)).getId());
            labWorkService.updateLabWork(labWork);
        } catch (IncorrectDataException e) {
            if (e.getErrorList().stream().map(IntegrityError::getCode).collect(Collectors.toList()).contains(404)) {
                throw new SoapFault(e.toString(), 404);
            } else {
                throw new SoapFault(e.toString(), 400);
            }
        }
    }

    @Override
    public void deleteLabWork(String id) throws SoapFault {
        LabWork labWork = null;
        try {
            labWork = labWorkService.findLabWorkById(Integer.parseInt(id));
            labWorkService.deleteLabWork(labWork);
        } catch (IncorrectDataException e) {
            if (e.getErrorList().stream().map(IntegrityError::getCode).collect(Collectors.toList()).contains(404)) {
                throw new SoapFault(e.toString(), 404);
            } else {
                throw new SoapFault(e.toString(), 400);
            }
        }
    }

    @Override
    public LabWork deleteByDifficulty(String difficulty) throws SoapFault {
        try {
            return labWorkService.deleteLabWorkByDifficulty(difficulty);
        } catch (IncorrectDataException e) {
            throw new SoapFault(e.toString(), 400);
        }
    }

//    private LabWorkFilterConfiguration parseFilterRequest(MultivaluedMap<String, String> params) {
//        Map<String, String[]> result = new HashMap<>();
//        params.forEach((key, value) -> result.put(key.toLowerCase(), value.toArray(new String[0])));
//        return new LabWorkFilterConfiguration(result);
//    }

}
