package com.kekens.soa_back_2.service.impl;

import com.kekens.soa_back_2.model.Difficulty;
import com.kekens.soa_back_2.model.LabWork;
import com.kekens.soa_back_2.service.BarsService;
import com.kekens.soa_back_2.validator.IntegrityError;
import com.kekens.soa_back_2.validator.exception.IncorrectDataException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;

public class BarsServiceImpl implements BarsService {

    private static final String BACK_2_URI = "http://localhost:1111/soa-back-1";

    @Override
    public void decreaseDifficulty(int labworkId, int stepCount) throws IncorrectDataException {
        Response responseLab = getTarget().path("labworks").path(String.valueOf(labworkId)).request()
                .accept(MediaType.APPLICATION_JSON).get();

        if (responseLab.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new IncorrectDataException(Collections.singletonList(new IntegrityError(404, "Resource not found")));
        }

        LabWork labWork = responseLab.readEntity(LabWork.class);
        Difficulty difficulty = labWork.getDifficulty();

        if (difficulty.value == 0) {
            throw new IncorrectDataException(Collections.singletonList(new IntegrityError(409, "Difficulty is already the smallest")));
        }

        while ((difficulty.value != 0) && (stepCount != 0)) {
            difficulty = Difficulty.valueOf(difficulty.value - 1);
            if (difficulty == null) {
                difficulty = Difficulty.VERY_EASY;
                break;
            }
            stepCount--;
        }

        labWork.setDifficulty(difficulty);
        labWork.setCreationDate(null);
        getTarget().path("labworks").path(String.valueOf(labworkId)).request()
                .accept(MediaType.APPLICATION_JSON).put(Entity.entity(labWork, MediaType.APPLICATION_JSON));
    }

    private WebTarget getTarget() {
        Client client = ClientBuilder.newClient();
        return client.target(BACK_2_URI);
    }
}
