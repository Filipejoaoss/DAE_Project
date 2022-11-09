package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ws;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.dtos.AuthDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ejbs.JwtBean;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ejbs.UserBean;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.Person;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.jwt.Jwt;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.text.ParseException;
import java.util.logging.Logger;
@Path("/auth")
public class LoginService {
    private static final Logger log = Logger.getLogger(LoginService.class.getName());

    @Context
    private SecurityContext securityContext;


    @EJB
    JwtBean jwtBean;
    @EJB
    UserBean userBean;
    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(AuthDTO authDTO) {
        try {
            var user = userBean.authenticate(authDTO.getUsername(), authDTO.getPassword());
            if (user != null) {
                if (user.getUsername() != null) {
                    log.info("Generating JWT for user " + user.getUsername());
                }
                String token = jwtBean.createJwt(user.getUsername(), new
                        String[]{user.getClass().getSimpleName()});
                return Response.ok(new Jwt(token)).build();
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("/user")
    public Response demonstrateClaims(@HeaderParam("Authorization") String auth) {
        if (auth != null && auth.startsWith("Bearer ")) {
            try {
                JWT j = JWTParser.parse(auth.substring(7));
                return Response.ok(j.getJWTClaimsSet().getClaims()).build();
            } catch (ParseException e) {
                log.warning(e.toString());
                return Response.status(400).build();
            }
        }
        return Response.status(204).build();
    }

    @GET
    @Path("/")
    @RolesAllowed({"Administrator","Medic", "Patient"})
    public Response getUsername(){
        return Response.status(Response.Status.OK).entity(securityContext.getUserPrincipal().getName()).build();
    }
}