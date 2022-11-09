package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.logging.Logger;

public class CatchAllExceptionMapper implements ExceptionMapper<pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.CatchAllException> {
    private static final Logger logger = Logger.getLogger("exceptions.CatchAllExceptionMapper");

    @Override
    public Response toResponse(pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.CatchAllException e) {
        String errorMsg = e.getMessage();
        logger.warning("ERROR: " + errorMsg);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMsg).build();
    }
}
