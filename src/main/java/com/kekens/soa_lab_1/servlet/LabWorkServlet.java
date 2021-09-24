package com.kekens.soa_lab_1.servlet;

import com.kekens.soa_lab_1.model.LabWork;
import com.kekens.soa_lab_1.service.LabWorkService;
import com.kekens.soa_lab_1.service.impl.LabWorkServiceImpl;
import com.kekens.soa_lab_1.util.JsonUtil;
import com.kekens.soa_lab_1.validator.exception.IncorrectDataException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

@WebServlet(value = "/lab-work")
public class LabWorkServlet extends HttpServlet {

    private final LabWorkService labWorkService = new LabWorkServiceImpl();
    private final JsonUtil<LabWork> jsonUtilLabWork = new JsonUtil<>(LabWork.class);
    private final JsonUtil<String> jsonUtilString = new JsonUtil<>(String.class);

    static Logger log = Logger.getLogger(LabWorkServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("GET");

        LabWork labWork = labWorkService.findLabWorkById(Integer.parseInt(request.getParameter("id")));
        String labWorkJsonString = jsonUtilLabWork.buildJsonStringFromObject(labWork);

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(labWorkJsonString);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("POST");

        LabWork labWork = jsonUtilLabWork.buildObjectFromRequest(request);
        log.info(labWork.toString());

        try {
            labWorkService.updateLabWork(labWork);
        } catch (IncorrectDataException e) {
            sendErrorListResponse(response, e.getErrorList());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("PUT");

        LabWork labWork = jsonUtilLabWork.buildObjectFromRequest(request);
        log.info(labWork.toString());

        try {
            labWorkService.createLabWork(labWork);
        } catch (IncorrectDataException e) {
            sendErrorListResponse(response, e.getErrorList());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("DELETE");

        labWorkService.deleteLabWork(Integer.parseInt(request.getParameter("id")));
    }

    private void sendErrorListResponse(HttpServletResponse response, List<String> errorList) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(jsonUtilString.buildJsonStringFromList(errorList));
        out.flush();
    }
}
