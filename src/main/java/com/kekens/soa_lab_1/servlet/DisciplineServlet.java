package com.kekens.soa_lab_1.servlet;

import com.kekens.soa_lab_1.model.Discipline;
import com.kekens.soa_lab_1.service.DisciplineService;
import com.kekens.soa_lab_1.service.impl.DisciplineServiceImpl;
import com.kekens.soa_lab_1.util.JsonUtil;
import com.kekens.soa_lab_1.validator.IntegrityError;
import com.kekens.soa_lab_1.validator.exception.IncorrectDataException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/disciplines/*")
public class DisciplineServlet extends HttpServlet {

    DisciplineService disciplineService = new DisciplineServiceImpl();
    private final JsonUtil<Discipline> jsonUtilDiscipline = new JsonUtil<>(Discipline.class);
    private final JsonUtil<IntegrityError> jsonUtilIntegrityError = new JsonUtil<>(IntegrityError.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();

        if ((path == null) || (path.equals("/"))) {
            List<Discipline> disciplineList = disciplineService.findAllDisciplines();
            sendResponse(response, jsonUtilDiscipline.buildJsonStringFromList(disciplineList), HttpServletResponse.SC_OK);
        } else {
            path = path.replaceAll("/","");

            try {
                Discipline discipline = disciplineService.findDisciplineById(Integer.parseInt(path));
                sendResponse(response, jsonUtilDiscipline.buildJsonStringFromObject(discipline), HttpServletResponse.SC_OK);
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
        Discipline discipline = jsonUtilDiscipline.buildObjectFromRequest(request);

        try {
            disciplineService.createDiscipline(discipline);
        } catch (IncorrectDataException e) {
            sendResponse(response, jsonUtilIntegrityError.buildJsonStringFromList(e.getErrorList()), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();

        if (path != null) {
            path = path.replaceAll("/","");

            try {
                Discipline discipline = jsonUtilDiscipline.buildObjectFromRequest(request);
                discipline.setId(disciplineService.findDisciplineById(Integer.parseInt(path)).getId());
                disciplineService.updateDiscipline(discipline);
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
        String path = request.getPathInfo();

        if (path != null) {
            path = path.replaceAll("/", "");

            try {
                Discipline discipline = disciplineService.findDisciplineById(Integer.parseInt(path));
                disciplineService.deleteDiscipline(discipline);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
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

    private void sendResponse(HttpServletResponse response, String responseString, int statusCode) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
        out.print(responseString);
        out.flush();
    }
}