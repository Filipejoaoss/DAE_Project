package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

@Provider
public class MyIllegalArgumentExceptionMapper implements ExceptionMapper<pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyIllegalArgumentException> {
    private static final Logger logger = Logger.getLogger("exceptions.MyIllegalArgumentExceptionMapper");

    @Override
    public Response toResponse(pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyIllegalArgumentException e) {
        String errorMsg = e.getMessage();
        logger.warning("ERROR: " + errorMsg);

        return Response.status(Response.Status.BAD_REQUEST).entity(errorMsg).build();
    }
}
