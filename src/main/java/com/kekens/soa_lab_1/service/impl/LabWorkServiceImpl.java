package com.kekens.soa_lab_1.service.impl;

import com.google.gson.Gson;
import com.kekens.soa_lab_1.dao.LabWorkDao;
import com.kekens.soa_lab_1.model.LabWork;
import com.kekens.soa_lab_1.service.LabWorkService;

public class LabWorkServiceImpl implements LabWorkService {

    private final LabWorkDao labWorkDao = new LabWorkDao();

    @Override
    public LabWork findLabWorkById(int id) {
        return labWorkDao.findById(id);
    }
}
