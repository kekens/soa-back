package com.kekens.soa_lab_1.service;

import com.google.gson.Gson;
import com.kekens.soa_lab_1.model.LabWork;

public interface LabWorkService {

    LabWork findLabWorkById(int id);

}
