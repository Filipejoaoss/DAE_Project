package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ws;

import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.dtos.EmailDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.dtos.MedicDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.dtos.PatientDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ejbs.EmailBean;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ejbs.MedicBean;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ejbs.PatientBean;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.Medic;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.utils.EntityToDTO;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.stream.Collectors;

@Path("medics")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class MedicService {

    @Context
    private SecurityContext securityContext;

    @EJB
    private MedicBean medicBean;

    @EJB
    private PatientBean patientBean;

    @EJB
    private EmailBean emailBean;

    @GET
    @Path("/")
    @RolesAllowed({"Administrator"})
    public Response all() {
        return Response.status(Response.Status.OK).entity(new EntityToDTO().medicToDTOs(medicBean.all())).build();
    }

    @GET
    @Path("{username}")
    @RolesAllowed({"Administrator","Medic"})
    public Response get(@PathParam("username") String username) {
        try {
            Medic medic = medicBean.findOrFail(username);
            if (securityContext.isUserInRole("Medic")){
                if(!securityContext.getUserPrincipal().getName().equals(medic.getUsername())){
                    return Response.status(Response.Status.FORBIDDEN).build();
                }
            }
            return Response.ok(new EntityToDTO().medicToDTO(medic)).build();
        }catch (MyEntityNotFoundException e){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("ERROR_FINDING_MEDIC")
                    .build();
        }


    }

    @GET
    @Path("{username}/patients")
    @RolesAllowed({"Administrator","Medic"})
    public Response getPatients(@PathParam("username") String username) throws MyEntityNotFoundException {
        try{
            Medic medic = medicBean.findOrFail(username);
            if (securityContext.isUserInRole("Medic")){
                if(!securityContext.getUserPrincipal().getName().equals(medic.getUsername())){
                    return Response.status(Response.Status.FORBIDDEN).build();
                }
            }
            return Response.status(Response.Status.OK).entity(new EntityToDTO().patientToDTOs(medic.getPatients())).build();
        }catch (MyEntityNotFoundException e){
            return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_MEDIC")
                .build();
        }
    }

    @POST
    @Path("{username}/patients")
    @RolesAllowed({"Medic"})
    public Response addPatient(@PathParam("username") String username, PatientDTO patientDTO) throws MyEntityNotFoundException {
        Medic m = medicBean.findOrFail(username);
        if(securityContext.isUserInRole("Medic")){
            if (!securityContext.getUserPrincipal().getName().equals(m.getUsername())){
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        Patient p = patientBean.findOrFail(patientDTO.getUsername());
        medicBean.addPatientToMedic(m.getUsername(),p.getUsername());
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("{username}/patients/{patientUsername}")
    @RolesAllowed({"Administrator","Medic"})
    public Response removePatient(@PathParam("username") String username, @PathParam("patientUsername") String patientUsername) throws MyEntityNotFoundException {
        Medic m = medicBean.findOrFail(username);
        if(securityContext.isUserInRole("Medic")){
            if (!securityContext.getUserPrincipal().getName().equals(m.getUsername())){
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        Patient p = patientBean.findOrFail(patientUsername);
        if(p.getMedic().getUsername() != m.getUsername()){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        medicBean.removePatientToMedic(m.getUsername(),p.getUsername());
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("/")
    @RolesAllowed({"Administrator"})
    public Response create (MedicDTO medicDTO) throws MyConstraintViolationException, MyEntityExistsException {
        medicBean.create(
                medicDTO.getUsername(),
                medicDTO.getName(),
                medicDTO.getPassword(),
                medicDTO.getContact(),
                medicDTO.getAddress(),
                medicDTO.getEmail()
        );
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/{username}")
    @RolesAllowed({"Administrator"})
    public Response delete (@PathParam("username") String username) throws MyEntityNotFoundException {
        try {
            medicBean.findOrFail(username);
        }catch (MyEntityNotFoundException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        medicBean.destroy(username);
        try{
            medicBean.findOrFail(username);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }catch (MyEntityNotFoundException e){
            return Response.status(Response.Status.OK).build();
        }
    }

    @PUT
    @Path("{username}")
    @RolesAllowed({"Administrator"})
    public Response update (@PathParam("username") String username, MedicDTO medicDTO) throws MyEntityNotFoundException {
        medicBean.update(username, medicDTO.getName(), medicDTO.getPassword(), medicDTO.getContact(), medicDTO.getAddress(), medicDTO.getEmail());
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("{username}/email")
    @RolesAllowed({"Administrator"})
    public Response sendEmail(@PathParam("username") String username, EmailDTO email) {
        try{
            Medic medic = medicBean.findOrFail(username);
            emailBean.send(medic.getEmail(), email.getSubject(), email.getMessage());
            return Response.status(Response.Status.OK).entity("Email sent").build();
        }catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}
