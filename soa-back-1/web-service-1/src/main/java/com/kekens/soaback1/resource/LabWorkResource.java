package com.kekens.soaback1.resource;

import com.kekens.soaback1.model.LabWork;
import com.kekens.soaback1.util.LabWorkFilterConfiguration;
import com.kekens.soaback1.validator.exception.IncorrectDataException;
import com.kekens.soaback1.validator.IntegrityError;
import com.kekens.soaback1.service.LabWorkService;
import com.kekens.soaback1.util.RemoteBeanLookupUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/labworks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LabWorkResource {

    private final LabWorkService labWorkService;

    public LabWorkResource() throws IncorrectDataException {
        this.labWorkService = RemoteBeanLookupUtil.lookupLabWorkBean();
    }

    @GET
    public Response getAllLabWorks(@Context UriInfo uriInfo)  {
        try {
            LabWorkFilterConfiguration labWorkFilterConfiguration = parseFilterRequest(uriInfo.getQueryParameters());
            List<LabWork> listLabWork = labWorkService.findAllLabWorks(labWorkFilterConfiguration);

            return Response.status(Response.Status.OK).entity(listLabWork).build();
        } catch (IncorrectDataException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getErrorList()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getLabWork(@PathParam("id") String id) {
        try {
            LabWork labWork = labWorkService.findLabWorkById(Integer.parseInt(id));
            return Response.status(Response.Status.OK).entity(labWork).build();
        } catch (NumberFormatException | IncorrectDataException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new IntegrityError(404, "Resource not found")).build();
        }
    }

    @GET
    @Path("/difficulty/count")
    public Response getCountByDifficultyMore(@QueryParam("difficulty") String difficulty) {
        try {
            int count = labWorkService.getCountLabWorkByDifficulty(difficulty);
            return Response.status(Response.Status.OK).entity(String.format("%s: %d", difficulty, count)).build();
        } catch (IncorrectDataException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getErrorList()).build();
        }
    }

    @POST
    public Response createLabWork(LabWork labWork) {
        try {
            labWorkService.createLabWork(labWork);

            return Response.status(Response.Status.OK).build();
        } catch (IncorrectDataException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getErrorList()).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateLabWork(@PathParam("id") String id, LabWork labWork) {
        try {
            System.out.println("\n\n");
            System.out.println(labWork);
            System.out.println("\n\n");
            labWork.setId(labWorkService.findLabWorkById(Integer.parseInt(id)).getId());
            labWorkService.updateLabWork(labWork);

            return Response.status(Response.Status.OK).build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new IntegrityError(404, "Resource not found")).build();
        } catch (IncorrectDataException e) {
            Response.Status status;
            if (e.getErrorList().stream().map(IntegrityError::getCode).collect(Collectors.toList()).contains(404)) {
                status = Response.Status.NOT_FOUND;
            } else {
                status = Response.Status.BAD_REQUEST;
            }
            return Response.status(status).entity(e.getErrorList()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteLabWork(@PathParam("id") String id) {
        try {
            LabWork labWork = labWorkService.findLabWorkById(Integer.parseInt(id));
            labWorkService.deleteLabWork(labWork);

            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new IntegrityError(404, "Resource not found")).build();
        } catch (IncorrectDataException e) {
            Response.Status status;
            if (e.getErrorList().stream().map(IntegrityError::getCode).collect(Collectors.toList()).contains(404)) {
                status = Response.Status.NOT_FOUND;
            } else {
                status = Response.Status.BAD_REQUEST;
            }

            return Response.status(status).entity(e.getErrorList()).build();
        }
    }

    @DELETE
    @Path("/difficulty")
    public Response deleteByDifficulty(@QueryParam("difficulty") String difficulty) {
        try {
            LabWork labWork = labWorkService.deleteLabWorkByDifficulty(difficulty);
            return Response.status(Response.Status.OK).entity(labWork).build();
        } catch (IncorrectDataException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getErrorList()).build();
        }
    }

    private LabWorkFilterConfiguration parseFilterRequest(MultivaluedMap<String, String> params) {
        Map<String, String[]> result = new HashMap<>();
        params.forEach((key, value) -> result.put(key.toLowerCase(), value.toArray(new String[0])));
        return new LabWorkFilterConfiguration(result);
    }
}
