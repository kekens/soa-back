package com.kekens.soa_lab_1.servlet;

import com.google.gson.Gson;
import com.kekens.soa_lab_1.model.LabWork;
import com.kekens.soa_lab_1.service.LabWorkService;
import com.kekens.soa_lab_1.service.impl.LabWorkServiceImpl;
import com.kekens.soa_lab_1.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

@WebServlet(value = "/lab-work")
public class LabWorkServlet extends HttpServlet {

    private final LabWorkService labWorkService = new LabWorkServiceImpl();
    private final JsonUtil<LabWork> jsonUtil = new JsonUtil<>(LabWork.class);

    static Logger log = Logger.getLogger(LabWorkServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idString = request.getParameter("id");
        LabWork labWork = labWorkService.findLabWorkById(Integer.parseInt(idString));
        String labWorkJsonString = jsonUtil.buildJsonStringFromObject(labWork);

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(labWorkJsonString);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("POST");

        LabWork labWork = jsonUtil.buildObjectFromRequest(request);
        log.info(labWork.toString());

        labWorkService.updateLabWork(labWork);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("PUT");

        LabWork labWork = jsonUtil.buildObjectFromRequest(request);
        log.info(labWork.toString());

        labWorkService.createLabWork(labWork);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
