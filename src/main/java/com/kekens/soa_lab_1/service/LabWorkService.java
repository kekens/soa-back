package com.kekens.soa_lab_1.service;

import com.kekens.soa_lab_1.model.LabWork;

public interface LabWorkService {

    LabWork findLabWorkById(int id);

    void createLabWork(LabWork labWork);
}
