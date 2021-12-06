package com.kekens.soaback2.service.impl;

import com.kekens.soaback2.model.Difficulty;
import com.kekens.soaback2.model.Discipline;
import com.kekens.soaback2.model.LabWork;
import com.kekens.soaback2.service.BarsService;
import com.kekens.soaback2.util.ServiceDiscoveryConfiguration;
import com.kekens.soaback2.validator.IntegrityError;
import com.kekens.soaback2.validator.exception.IncorrectDataException;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Stateless
@Remote(BarsService.class)
public class BarsServiceImpl implements BarsService {

    private final boolean isSecure = Boolean.parseBoolean(System.getenv("IS_SECURE"));

    @Override
    public void decreaseDifficulty(int labworkId, int stepCount) throws IncorrectDataException {
        List<IntegrityError> errorList = new ArrayList<>();

        if (labworkId <= 0) {
            errorList.add(new IntegrityError(400, "Incorrect value of lab id"));
        }

        if (stepCount <= 0) {
            errorList.add(new IntegrityError(400, "Incorrect value of step count"));
        }

        if (!errorList.isEmpty()) {
            throw new IncorrectDataException(errorList);
        }

        WebTarget target = getTarget();

        if (target == null) {
            throw new IncorrectDataException(Collections.singletonList(new IntegrityError(400, "Can't setup SSL")));
        }

        Response responseLab = target.path("labworks").path(String.valueOf(labworkId)).request()
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
        target.path("labworks").path(String.valueOf(labworkId)).request()
                .accept(MediaType.APPLICATION_JSON).put(Entity.entity(labWork, MediaType.APPLICATION_JSON));
    }

    @Override
    public void deleteLabWorkFromDiscipline(int disciplineId, int labworkId) throws IncorrectDataException {
        List<IntegrityError> errorList = new ArrayList<>();

        if (disciplineId <= 0) {
            errorList.add(new IntegrityError(400, "Incorrect value of lab id"));
        }

        if (labworkId <= 0) {
            errorList.add(new IntegrityError(400, "Incorrect value of step count"));
        }

        if (!errorList.isEmpty()) {
            throw new IncorrectDataException(errorList);
        }

        WebTarget target = getTarget();

        if (target == null) {
            throw new IncorrectDataException(Collections.singletonList(new IntegrityError(400, "Can't setup SSL")));
        }

        Response responseDiscipline = target.path("disciplines").path(String.valueOf(disciplineId)).request()
                .accept(MediaType.APPLICATION_JSON).get();

        Response responseLab = target.path("labworks").path(String.valueOf(labworkId)).request()
                .accept(MediaType.APPLICATION_JSON).get();

        if (responseDiscipline.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new IncorrectDataException(Collections.singletonList(new IntegrityError(404, "Resource 'Discipline' not found")));
        }

        if (responseLab.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new IncorrectDataException(Collections.singletonList(new IntegrityError(404, "Resource 'LabWork' not found")));
        }

        Discipline discipline = responseDiscipline.readEntity(Discipline.class);
        LabWork labWork = responseLab.readEntity(LabWork.class);

        if (discipline.getId() != labWork.getDiscipline().getId()) {
            throw new IncorrectDataException(Collections.singletonList(new IntegrityError(404, "LabWork isn't in discipline's programme")));
        }

        target.path("labworks").path(String.valueOf(labworkId)).request().delete();
    }

    private WebTarget getTarget() throws IncorrectDataException {
        String consulUrl = "http://localhost:8500";

        Client clientConsul = ClientBuilder.newClient();
        Response responseConsul = clientConsul.target(consulUrl).path("v1").path("agent").path("service").path("soa1").request()
                .accept(MediaType.APPLICATION_JSON).get();

        if (responseConsul.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new IncorrectDataException(Collections.singletonList(new IntegrityError(404, "Service soa1 not found")));
        }

        ServiceDiscoveryConfiguration serviceDiscoveryConfiguration = responseConsul.readEntity(ServiceDiscoveryConfiguration.class);

        System.out.println("-----------");
        System.out.println(serviceDiscoveryConfiguration.getAddress());
        System.out.println(serviceDiscoveryConfiguration.getName());
        System.out.println(serviceDiscoveryConfiguration.getPort());
        System.out.println("-----------");

        String BACK_2_URI = serviceDiscoveryConfiguration.getAddress() + ":" + serviceDiscoveryConfiguration.getPort() + "/" + serviceDiscoveryConfiguration.getName();

        if (isSecure) {
            try {
                ClientBuilder clientBuilder = ClientBuilder.newBuilder();
                clientBuilder.sslContext(SSLContext.getDefault());
                KeyStore trustStore = KeyStore.getInstance("JKS");
                InputStream fin = getClass().getResourceAsStream("/payara.truststore");
                trustStore.load(fin, System.getenv("TRUSTSTORE_PASSWORD").toCharArray());
                clientBuilder.trustStore(trustStore);
                clientBuilder.hostnameVerifier((s, sslSession) -> s.equals("localhost"));
                Client client = clientBuilder.build();
                return client.target(BACK_2_URI);
            } catch (IOException e) {
                System.out.println("Can't find truststore file");
            } catch (Exception e) {
                System.out.println("Can't setup SSL");
                System.out.println(e.getMessage());
            }
            return null;
        } else {
            return ClientBuilder.newClient().target(BACK_2_URI);
        }
    }
}