package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ws;

import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.dtos.AdministratorDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.dtos.EmailDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ejbs.AdministratorBean;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ejbs.EmailBean;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.Medic;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.utils.EntityToDTO;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("administrators")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class AdministratorService {
    @EJB
    private AdministratorBean administratorBean;

    @EJB
    private EmailBean emailBean;

    @GET
    @Path("/")
    @RolesAllowed({"Administrator"})
    public Response getAllAdministratorsWS() {
        return Response.status(Response.Status.OK).entity(new EntityToDTO().administratorToDTOs(administratorBean.all())).build();
    }

    @GET
    @Path("{username}")
    @RolesAllowed({"Administrator"})
    public Response getAdministratorDetails(@PathParam("username") String username) {
        Administrator administrator = administratorBean.find(username);
        if (administrator != null) {
            return Response.ok(new EntityToDTO().administratorToDTO(administrator)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_ADMINISTRATOR")
                .build();
    }

    @POST
    @Path("/")
    @RolesAllowed({"Administrator"})
    public Response create (AdministratorDTO administratorDTO) throws MyConstraintViolationException, MyEntityExistsException {
        administratorBean.create(
                administratorDTO.getUsername(),
                administratorDTO.getName(),
                administratorDTO.getPassword(),
                administratorDTO.getContact(),
                administratorDTO.getAddress(),
                administratorDTO.getEmail()
        );
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/{username}")
    @RolesAllowed({"Administrator"})
    public Response delete (@PathParam("username") String username) throws MyEntityNotFoundException {
        administratorBean.findOrFail(username);
        administratorBean.destroy(username);
        try{
            administratorBean.findOrFail(username);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }catch (MyEntityNotFoundException e){
            return Response.status(Response.Status.OK).build();
        }
    }

    @PUT
    @Path("{username}")
    @RolesAllowed({"Administrator"})
    public Response update (@PathParam("username") String username, AdministratorDTO administratorDTO) throws MyEntityNotFoundException {
        administratorBean.update(username, administratorDTO.getName(), administratorDTO.getPassword(), administratorDTO.getContact(), administratorDTO.getAddress(), administratorDTO.getEmail());

        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("{username}/email")
    @RolesAllowed({"Administrator"})
    public Response sendEmail(@PathParam("username") String username, EmailDTO email) {
        try{
            Administrator admin = administratorBean.findOrFail(username);
            emailBean.send(admin.getEmail(), email.getSubject(), email.getMessage());
            return Response.status(Response.Status.OK).entity("Email sent").build();
        }catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("report")
    @RolesAllowed({"Administrator","Medic", "Patient"})
    public Response createReport(EmailDTO email){
        try{
            for (Administrator admin : administratorBean.all()) {
                emailBean.send(admin.getEmail(), "Report from " + email.getSubject(), email.getMessage());
            }
            return Response.status(Response.Status.OK).entity("Report sent").build();
        }catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
