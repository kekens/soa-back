package com.kekens.soa_back_2.resource;

import com.kekens.soa_back_2.service.BarsService;
import com.kekens.soa_back_2.service.impl.BarsServiceImpl;
import com.kekens.soa_back_2.validator.IntegrityError;
import com.kekens.soa_back_2.validator.exception.IncorrectDataException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

@Path("/bars")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BarsResource {

    private final BarsService barsServiceImpl = new BarsServiceImpl();

    @POST
    @Path("/labwork/{labwork-id}/difficulty/decrease/{steps-count}")
    public Response decreaseLabWorkDifficulty(@PathParam("labwork-id") String id,
                                              @PathParam("steps-count") String stepCount)
    {
        try {
            this.barsServiceImpl.decreaseDifficulty(Integer.parseInt(id), Integer.parseInt(stepCount));
            return Response.status(Response.Status.OK).build();
        } catch (IncorrectDataException e) {
            Response.Status status;
            if (e.getErrorList().stream().map(IntegrityError::getCode).collect(Collectors.toList()).contains(404)) {
                status = Response.Status.NOT_FOUND;
            } else {
                status = Response.Status.BAD_REQUEST;
            }
            return Response.status(status).entity(e.getErrorList()).build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new IntegrityError(400, "Incorrect parameter")).build();
        }
    }

    @DELETE
    @Path("/discipline/{discipline-id}/labwork/{labwork-id}/remove")
    public Response deleteLabWorkFromDiscipline(@PathParam("discipline-id") String disciplineId,
                                                @PathParam("labwork-id") String labWorkId)
    {
        try {
            this.barsServiceImpl.deleteLabWorkFromDiscipline(Integer.parseInt(disciplineId), Integer.parseInt(labWorkId));
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (IncorrectDataException e) {
            Response.Status status;
            if (e.getErrorList().stream().map(IntegrityError::getCode).collect(Collectors.toList()).contains(404)) {
                status = Response.Status.NOT_FOUND;
            } else {
                status = Response.Status.BAD_REQUEST;
            }
            return Response.status(status).entity(e.getErrorList()).build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new IntegrityError(400, "Incorrect parameter")).build();
        }
    }

}