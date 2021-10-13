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
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

        if ((path == null) || (path.equals("/"))) {
            try {
                // Filter
                LabWorkFilterConfiguration labWorkFilterConfiguration = parseFilterRequest(request);
                List<LabWork> listLabWork = labWorkService.findAllLabWorks(labWorkFilterConfiguration);

                log.info(request.getQueryString());

                sendResponse(response, jsonUtilLabWork.buildJsonStringFromList(listLabWork), HttpServletResponse.SC_OK);
            } catch (IncorrectDataException e) {
                sendResponse(response, jsonUtilIntegrityError.buildJsonStringFromList(e.getErrorList()), HttpServletResponse.SC_BAD_REQUEST);
            }
        } else if (path.equals("/difficulty/count")) {
            // Get count of LabWork which has difficulty more than
            try {
                String diff = request.getParameter("difficulty");
                int count = labWorkService.getCountLabWorkByDifficulty(diff);
                sendResponse(response, jsonUtilString.buildJsonStringFromObject(String.format("%s: %d", diff, count)), HttpServletResponse.SC_OK);
            } catch (IncorrectDataException e) {
                sendResponse(response, jsonUtilIntegrityError.buildJsonStringFromList(e.getErrorList()), HttpServletResponse.SC_BAD_REQUEST);
            }
        } else if (path.equals("/name/substr")) {
            // Get all LabWorks which contains name
            List<LabWork> listLabWork = labWorkService.findAllLabWorkByName(request.getParameter("name_substr"));
            sendResponse(response, jsonUtilLabWork.buildJsonStringFromList(listLabWork), HttpServletResponse.SC_OK);
        } else {
            path = path.replaceAll("/","");
            try {
                LabWork labWork = labWorkService.findLabWorkById(Integer.parseInt(path));
                sendResponse(response,jsonUtilLabWork.buildJsonStringFromObject(labWork), HttpServletResponse.SC_OK);
            } catch (NumberFormatException e) {
                sendResponse(response, jsonUtilIntegrityError.buildJsonStringFromObject(
                        new IntegrityError(404,"Resource not found")), HttpServletResponse.SC_NOT_FOUND);
            } catch (IncorrectDataException e) {
                sendResponse(response, jsonUtilIntegrityError.buildJsonStringFromList(e.getErrorList()), HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("POST");

        // Create LabWork
        try {
            // Get LabWork object from HttpServletRequest
            LabWork labWork = jsonUtilLabWork.buildObjectFromRequest(request);
            log.info(labWork.toString());

            labWorkService.createLabWork(labWork);
        } catch (IncorrectDataException e) {
            sendResponse(response, jsonUtilIntegrityError.buildJsonStringFromList(e.getErrorList()), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("PUT");

        String path = request.getPathInfo();

        if (path != null) {
            path = path.replaceAll("/","");

            // Update LabWork
            try {
                LabWork labWork = jsonUtilLabWork.buildObjectFromRequest(request);
                labWork.setId(labWorkService.findLabWorkById(Integer.parseInt(path)).getId());
                log.info(labWork.toString());
                labWorkService.updateLabWork(labWork);
            } catch (NumberFormatException e) {
                sendResponse(response, jsonUtilIntegrityError.buildJsonStringFromObject(
                        new IntegrityError(404,"Resource not found")), HttpServletResponse.SC_NOT_FOUND);
            } catch (IncorrectDataException e) {
                if (e.getErrorList().stream().map(IntegrityError::getCode).collect(Collectors.toList()).contains(404)) {
                    sendResponse(response, jsonUtilIntegrityError.buildJsonStringFromList(e.getErrorList()), HttpServletResponse.SC_NOT_FOUND);
                } else {
                    sendResponse(response, jsonUtilIntegrityError.buildJsonStringFromList(e.getErrorList()), HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        } else {
            sendResponse(response, jsonUtilIntegrityError.buildJsonStringFromObject(
                    new IntegrityError(404, "Resource not found")), HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("DELETE");

        String path = request.getPathInfo();
        log.info(path);

        if (path != null) {
            if (path.equals("/difficulty")) {
                log.info(request.getQueryString());

                // Delete one LabWork by difficulty
                try {
                    LabWork labWork = labWorkService.deleteLabWorkByDifficulty(request.getParameter("difficulty"));
                    sendResponse(response, jsonUtilLabWork.buildJsonStringFromObject(labWork), HttpServletResponse.SC_OK);
                } catch (IncorrectDataException e) {
                    sendResponse(response, jsonUtilIntegrityError.buildJsonStringFromList(e.getErrorList()), HttpServletResponse.SC_BAD_REQUEST);
                }
            } else {
                // Delete LabWork by id
                path = path.replaceAll("/","");

                try {
                    LabWork labWork = labWorkService.findLabWorkById(Integer.parseInt(path));
                    labWorkService.deleteLabWork(labWork);
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } catch (NumberFormatException e) {
                    sendResponse(response, jsonUtilIntegrityError.buildJsonStringFromObject(new IntegrityError(404,"Resource not found")), HttpServletResponse.SC_NOT_FOUND);
                } catch (IncorrectDataException e) {
                    if (e.getErrorList().stream().map(IntegrityError::getCode).collect(Collectors.toList()).contains(404)) {
                        sendResponse(response, jsonUtilIntegrityError.buildJsonStringFromList(e.getErrorList()), HttpServletResponse.SC_NOT_FOUND);
                    } else {
                        sendResponse(response, jsonUtilIntegrityError.buildJsonStringFromList(e.getErrorList()), HttpServletResponse.SC_BAD_REQUEST);
                    }
                }
            }
        } else {
            sendResponse(response, jsonUtilIntegrityError.buildJsonStringFromObject(
                    new IntegrityError(404, "Resource not found")), HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void sendResponse(HttpServletResponse response, String responseString, int statusCode) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
        out.print(responseString);
        out.flush();
    }

    private LabWorkFilterConfiguration parseFilterRequest(HttpServletRequest request) {
        return new LabWorkFilterConfiguration(request.getParameterMap());
    }
}
