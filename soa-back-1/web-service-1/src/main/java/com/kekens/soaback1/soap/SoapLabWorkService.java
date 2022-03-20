package com.kekens.soaback1.soap;

import com.kekens.soaback1.model.LabWork;
import com.kekens.soaback1.soap.fault.SoapFault;
import com.kekens.soaback1.util.LabWorkFilterConfiguration;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface SoapLabWorkService {

    @WebMethod
    List<LabWork> getAllLabWorks(LabWorkFilterConfiguration labWorkFilterConfiguration) throws SoapFault;

    @WebMethod
    LabWork getLabWork(String id) throws SoapFault;

    @WebMethod
    String getCountByDifficultyMore(String difficulty) throws SoapFault;

    @WebMethod
    void createLabWork(LabWork labWork) throws SoapFault;

    @WebMethod
    void updateLabWork(String id, LabWork labWork) throws SoapFault;

    @WebMethod
    void deleteLabWork(String id) throws SoapFault;

    @WebMethod
    LabWork deleteByDifficulty(String difficulty) throws SoapFault;

}
