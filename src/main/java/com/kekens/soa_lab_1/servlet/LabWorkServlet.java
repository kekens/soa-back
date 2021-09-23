package com.kekens.soa_lab_1.servlet;

import com.google.gson.Gson;
import com.kekens.soa_lab_1.dao.LabWorkDao;
import com.kekens.soa_lab_1.model.Discipline;
import com.kekens.soa_lab_1.model.LabWork;
import com.kekens.soa_lab_1.service.LabWorkService;
import com.kekens.soa_lab_1.service.impl.LabWorkServiceImpl;
import com.kekens.soa_lab_1.util.HibernateSessionFactoryUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.logging.Logger;

@WebServlet(value = "/lab-work")
public class LabWorkServlet extends HttpServlet {

    private final Gson gson = new Gson();
    private final LabWorkService labWorkService = new LabWorkServiceImpl();

    static Logger log = Logger.getLogger(LabWorkServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idString = request.getParameter("id");
        LabWork labWork = labWorkService.findLabWorkById(Integer.parseInt(idString));
        String labWorkJsonString = gson.toJson(labWork);

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(labWorkJsonString);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        log.info("PUT");

        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } finally {
            reader.close();
        }
        log.info(sb.toString());

        LabWork labWork = gson.fromJson(sb.toString(), LabWork.class);
        log.info(String.valueOf(labWork.getCoordinates().getX()));
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
