package com.kekens.soa_lab_1.servlet;

import com.kekens.soa_lab_1.model.LabWork;
import com.kekens.soa_lab_1.service.LabWorkService;
import com.kekens.soa_lab_1.service.impl.LabWorkServiceImpl;
import com.kekens.soa_lab_1.util.LabWorkFilterConfiguration;
import com.kekens.soa_lab_1.util.JsonUtil;
import com.kekens.soa_lab_1.validator.IntegrityError;
import com.kekens.soa_lab_1.validator.exception.IncorrectDataException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@WebServlet(value = "/lab-work/*")
public class LabWorkServlet extends HttpServlet {

    private final LabWorkService labWorkService = new LabWorkServiceImpl();
    private final JsonUtil<LabWork> jsonUtilLabWork = new JsonUtil<>(LabWork.class);
    private final JsonUtil<IntegrityError> jsonUtilIntegrityError = new JsonUtil<>(IntegrityError.class);
    private final JsonUtil<String> jsonUtilString = new JsonUtil<>(String.class);

    static Logger log = Logger.getLogger(LabWorkServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("GET");

        String path = request.getPathInfo();

        if (path == null) {
            // Filter
            LabWorkFilterConfiguration labWorkFilterConfiguration = parseFilterRequest(request);
            List<LabWork> listLabWork = labWorkService.findAllLabWorks(labWorkFilterConfiguration);

            log.info(request.getQueryString());

            sendOkResponse(response, jsonUtilLabWork.buildJsonStringFromList(listLabWork));
        } else if (path.equals("/difficulty/count")) {
            // Get count of LabWork which has difficulty more than
            try {
                String diff = request.getParameter("difficulty");
                int count = labWorkService.getCountLabWorkByDifficulty(diff);
                sendOkResponse(response, jsonUtilString.buildJsonStringFromObject(String.format("%s: %d", diff, count)));
            } catch (IncorrectDataException e) {
                sendErrorListResponse(response, e.getErrorList());
            }
        } else if (path.equals("/name/substr")) {
            // Get all LabWorks which contains name
            List<LabWork> listLabWork = labWorkService.findAllLabWorkByName(request.getParameter("name_substr"));
            sendOkResponse(response, jsonUtilLabWork.buildJsonStringFromList(listLabWork));
        } else {
            path = path.replaceAll("/","");
            try {
                LabWork labWork = labWorkService.findLabWorkById(Integer.parseInt(path));
                sendOkResponse(response,jsonUtilLabWork.buildJsonStringFromObject(labWork));
            } catch (NumberFormatException e) {
                sendErrorListResponse(response, Collections.singletonList(new IntegrityError(404,"Incorrect path")));
            } catch (IncorrectDataException e) {
                sendErrorListResponse(response, e.getErrorList());
            }
        }

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("PUT");

        // Get LabWork object from HttpServletRequest
        LabWork labWork = jsonUtilLabWork.buildObjectFromRequest(request);
        log.info(labWork.toString());

        // Update LabWork
        try {
            labWorkService.updateLabWork(labWork);
        } catch (IncorrectDataException e) {
            sendErrorListResponse(response, e.getErrorList());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("POST");

        // Get LabWork object from HttpServletRequest
        LabWork labWork = jsonUtilLabWork.buildObjectFromRequest(request);
        log.info(labWork.toString());

        // Create LabWork
        try {
            labWorkService.createLabWork(labWork);
        } catch (IncorrectDataException e) {
            sendErrorListResponse(response, e.getErrorList());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("DELETE");

        String path = request.getPathInfo();
        log.info(request.getQueryString());

        if (path != null) {
            if (path.equals("/difficulty")) {
                log.info(request.getQueryString());

                // Delete one LabWork by difficulty
                try {
                    labWorkService.deleteLabWorkByDifficulty(request.getParameter("difficulty"));
                } catch (IncorrectDataException e) {
                    sendErrorListResponse(response, e.getErrorList());
                }
            } else {
                // Delete LabWork by id
                path = path.replaceAll("/","");
                try {
                    labWorkService.deleteLabWork(Integer.parseInt(path));
                } catch (NumberFormatException e) {
                    sendErrorListResponse(response, Collections.singletonList(new IntegrityError(404,"Incorrect path")));
                } catch (IncorrectDataException e) {
                    sendErrorListResponse(response, e.getErrorList());
                }
            }
        }
    }

    private void sendErrorListResponse(HttpServletResponse response, List<IntegrityError> errorList) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        sendResponse(response, jsonUtilIntegrityError.buildJsonStringFromList(errorList));
    }

    private void sendOkResponse(HttpServletResponse response, String responseString) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        sendResponse(response, responseString);
    }

    private void sendResponse(HttpServletResponse response, String responseString) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(responseString);
        out.flush();
    }

    private LabWorkFilterConfiguration parseFilterRequest(HttpServletRequest request) {
        return new LabWorkFilterConfiguration(request.getParameterMap());
    }
}
