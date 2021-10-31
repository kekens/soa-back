package com.kekens.soa_back.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kekens.soa_back.model.Discipline;
import com.kekens.soa_back.service.DisciplineService;
import com.kekens.soa_back.service.impl.DisciplineServiceImpl;
import com.kekens.soa_back.util.JsonUtil;
import com.kekens.soa_back.validator.IntegrityError;
import com.kekens.soa_back.validator.exception.IncorrectDataException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/disciplines")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DisciplineResource {

    DisciplineService disciplineService = new DisciplineServiceImpl();
    private final JsonUtil<Discipline> jsonUtilDiscipline = new JsonUtil<>(Discipline.class);
    private final JsonUtil<IntegrityError> jsonUtilIntegrityError = new JsonUtil<>(IntegrityError.class);

    @GET
    public Response getAllDisciplines() {
        List<Discipline> disciplineList = disciplineService.findAllDisciplines();
        return Response.status(Response.Status.OK).entity(disciplineList).build();
    }

    @GET
    @Path("/{id}")
    public Response getDisciplineById(@PathParam("id") String id) throws JsonProcessingException {
        try {
            Discipline discipline = disciplineService.findDisciplineById(Integer.parseInt(id));
            return Response.status(Response.Status.OK).entity(jsonUtilDiscipline.buildJsonStringFromObject(discipline)).build();
        } catch (NumberFormatException | IncorrectDataException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(jsonUtilIntegrityError.buildJsonStringFromObject(
                    new IntegrityError(404,"Resource not found"))).build();
        }
    }

    @POST
    public Response createDiscipline(Discipline discipline) throws JsonProcessingException {
        try {
            disciplineService.createDiscipline(discipline);
            return Response.status(Response.Status.OK).build();
        } catch (IncorrectDataException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(jsonUtilIntegrityError.buildJsonStringFromList(e.getErrorList())).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateDiscipline(@PathParam("id") String id, Discipline discipline) throws JsonProcessingException {
        try {
            discipline.setId(disciplineService.findDisciplineById(Integer.parseInt(id)).getId());
            disciplineService.updateDiscipline(discipline);
            return Response.status(Response.Status.OK).build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(jsonUtilIntegrityError.buildJsonStringFromObject(
                    new IntegrityError(404, "Resource not found"))).build();
        } catch (IncorrectDataException e) {
            Response.Status status;
            if (e.getErrorList().stream().map(IntegrityError::getCode).collect(Collectors.toList()).contains(404)) {
                status = Response.Status.NOT_FOUND;
            } else {
                status = Response.Status.BAD_REQUEST;
            }

            return Response.status(status).entity(jsonUtilIntegrityError.buildJsonStringFromList(e.getErrorList())).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteDiscipline(@PathParam("id") String id) throws JsonProcessingException {
        try {
            Discipline discipline = disciplineService.findDisciplineById(Integer.parseInt(id));
            disciplineService.deleteDiscipline(discipline);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(jsonUtilIntegrityError.buildJsonStringFromObject(
                    new IntegrityError(404, "Resource not found"))).build();
        } catch (IncorrectDataException e) {
            Response.Status status;
            if (e.getErrorList().stream().map(IntegrityError::getCode).collect(Collectors.toList()).contains(404)) {
                status = Response.Status.NOT_FOUND;
            } else {
                status = Response.Status.BAD_REQUEST;
            }

            return Response.status(status).entity(jsonUtilIntegrityError.buildJsonStringFromList(e.getErrorList())).build();
        }
    }

//    @Override
//    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String path = request.getPathInfo();
//
//        if (path != null) {
//            path = path.replaceAll("/", "");
//
//            try {
//                Discipline discipline = disciplineService.findDisciplineById(Integer.parseInt(path));
//                disciplineService.deleteDiscipline(discipline);
//                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
//            } catch (NumberFormatException e) {
//                sendResponse(response, jsonUtilIntegrityError.buildJsonStringFromObject(
//                        new IntegrityError(404,"Resource not found")), HttpServletResponse.SC_NOT_FOUND);
//            } catch (IncorrectDataException e) {
//                if (e.getErrorList().stream().map(IntegrityError::getCode).collect(Collectors.toList()).contains(404)) {
//                    sendResponse(response, jsonUtilIntegrityError.buildJsonStringFromList(e.getErrorList()), HttpServletResponse.SC_NOT_FOUND);
//                } else {
//                    sendResponse(response, jsonUtilIntegrityError.buildJsonStringFromList(e.getErrorList()), HttpServletResponse.SC_BAD_REQUEST);
//                }
//            }
//        } else {
//            sendResponse(response, jsonUtilIntegrityError.buildJsonStringFromObject(
//                    new IntegrityError(404, "Resource not found")), HttpServletResponse.SC_NOT_FOUND);
//        }
//    }

}