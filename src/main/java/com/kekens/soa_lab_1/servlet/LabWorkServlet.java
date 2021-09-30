package com.kekens.soa_lab_1.servlet;

import com.kekens.soa_lab_1.model.LabWork;
import com.kekens.soa_lab_1.service.LabWorkService;
import com.kekens.soa_lab_1.service.impl.LabWorkServiceImpl;
import com.kekens.soa_lab_1.util.LabWorkFilterConfiguration;
import com.kekens.soa_lab_1.util.JsonUtil;
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
    private final JsonUtil<String> jsonUtilString = new JsonUtil<>(String.class);

    static Logger log = Logger.getLogger(LabWorkServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("GET");

        String path = request.getPathInfo();

        log.info("PATH " + path);

        if (path == null) {
            // Filter
            LabWorkFilterConfiguration labWorkFilterConfiguration = parseFilterRequest(request);
            List<LabWork> listLabWork = labWorkService.findAllLabWorks(labWorkFilterConfiguration);

            sendResponse(response, jsonUtilLabWork.buildJsonStringFromList(listLabWork));
        } else if (path.equals("/difficulty/count")) {
            // Get count of LabWork which has difficulty more than
            try {
                String diff = request.getParameter("difficulty");
                int count = labWorkService.getCountLabWorkByDifficulty(diff);
                sendResponse(response, String.format("Count of LabWork which has difficulty more than %s: %d", diff, count));
            } catch (IncorrectDataException e) {
                sendErrorListResponse(response, e.getErrorList());
            }
        } else if (path.equals("/name/substr")) {
            // Get all LabWorks which contains name
            List<LabWork> listLabWork = labWorkService.findAllLabWorkByName(request.getParameter("name_substr"));
            sendResponse(response, jsonUtilLabWork.buildJsonStringFromList(listLabWork));
        } else {
            path = path.replaceAll("/","");
            try {
                LabWork labWork = labWorkService.findLabWorkById(Integer.parseInt(path));
                sendResponse(response,jsonUtilLabWork.buildJsonStringFromObject(labWork));
            } catch (NumberFormatException e) {
                sendErrorListResponse(response, Collections.singletonList("Incorrect path"));
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("POST");

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
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("PUT");

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

        if (path.equals("/difficulty")) {
            // Delete one LabWork by difficulty
            try {
                labWorkService.deleteLabWorkByDifficulty(request.getParameter("difficulty"));
            } catch (IncorrectDataException e) {
                sendErrorListResponse(response, e.getErrorList());
            }
        } else {
            // Delete LabWork by id
            try {
                labWorkService.deleteLabWork(Integer.parseInt(request.getParameter("id")));
            } catch (IncorrectDataException e) {
                sendErrorListResponse(response, e.getErrorList());
            }
        }

    }

    private void sendErrorListResponse(HttpServletResponse response, List<String> errorList) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        sendResponse(response, jsonUtilString.buildJsonStringFromList(errorList));
    }

    private void sendResponse(HttpServletResponse response, String responseString) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        out.print(responseString);
        out.flush();
    }

    private LabWorkFilterConfiguration parseFilterRequest(HttpServletRequest request) {
        return new LabWorkFilterConfiguration(
                request.getParameter("name"),
                request.getParameter("coordinates_x"),
                request.getParameter("coordinates_y"),
                request.getParameter("creationDate"),
                request.getParameter("minimalPoint"),
                request.getParameter("difficulty"),
                request.getParameter("disciplineName"),
                request.getParameter("disciplineLectureHours"),
                request.getParameterValues("sort"),
                request.getParameter("count"),
                request.getParameter("page")
                );
    }
}
