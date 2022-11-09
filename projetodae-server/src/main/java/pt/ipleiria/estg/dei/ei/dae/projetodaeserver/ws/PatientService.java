package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ws;


import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.dtos.EmailDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.dtos.ObservationDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.dtos.PatientDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ejbs.*;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.BiomedicDataType;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.Medic;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.Observation;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyIllegalArgumentException;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.utils.EntityToDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.utils.MyDateTime;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


@Path("patients")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class PatientService {

    @Context
    private SecurityContext securityContext;

    @EJB
    private PatientBean patientBean;

    @EJB
    private MedicBean medicBean;

    @EJB
    private ObservationBean observationBean;

    @EJB
    private EmailBean emailBean;

    @EJB
    private BiomedicDataTypeBean biomedicDataTypeBean;

    @GET
    @Path("/")
    @RolesAllowed({"Administrator","Medic"})
    public Response all() throws MyEntityNotFoundException {
        if(securityContext.isUserInRole("Administrator")){
            return Response.status(Response.Status.OK).entity(new EntityToDTO().patientToDTOs(patientBean.all())).build();
        }
        if(securityContext.isUserInRole("Medic")){
            return Response.status(Response.Status.OK).entity(new EntityToDTO().patientToDTOs(medicBean.findOrFail(securityContext.getUserPrincipal().getName()).getPatients())).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }
    @GET
    @Path("/noMedic")
    @RolesAllowed({"Medic"})
    public Response allWithNoMedic(){
        return Response.status(Response.Status.OK).entity(new EntityToDTO().patientToDTOs(patientBean.all().stream().filter(p -> p.getMedic() == null).collect(Collectors.toList()))).build();
    }


    @GET
    @Path("{username}")
    @RolesAllowed({"Administrator","Medic", "Patient"})
    public Response get(@PathParam("username") String username){
        try{
            Patient p = patientBean.findOrFail(username);
            if(securityContext.isUserInRole("Patient")){
                if(!securityContext.getUserPrincipal().getName().equals(username)){
                    return Response.status(Response.Status.FORBIDDEN).build();
                }
            }
            if(securityContext.isUserInRole("Medic")){
                if(!securityContext.getUserPrincipal().getName().equals(p.getMedic().getUsername())){
                    return Response.status(Response.Status.FORBIDDEN).build();
                }
            }
            return Response.ok(new EntityToDTO().patientToDTO(p)).build();
        }catch (MyEntityNotFoundException e){
            return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_PATIENT")
                .build();
        }
    }

    @POST
    @Path("/")
    // ? @RolesAllowed({"Administrator","Medic", "Patient"})
    public Response create (PatientDTO patientDTO) throws MyConstraintViolationException, MyEntityExistsException {
        patientBean.create(
                patientDTO.getUsername(),
                patientDTO.getName(),
                patientDTO.getPassword(),
                patientDTO.getContact(),
                patientDTO.getAddress(),
                patientDTO.getEmail()
                );
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/{username}")
    @RolesAllowed({"Administrator","Medic"})
    public Response delete (@PathParam("username") String username) throws MyEntityNotFoundException {
        Patient p = patientBean.findOrFail(username);
        if(securityContext.isUserInRole("Medic")){
            if(!securityContext.getUserPrincipal().getName().equals(p.getMedic().getUsername())){
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        patientBean.destroy(p.getUsername());
        try{
            patientBean.findOrFail(username);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }catch (MyEntityNotFoundException e){
            return Response.status(Response.Status.OK).build();
        }
    }

    @PUT
    @Path("{username}")
    @RolesAllowed({"Administrator","Medic", "Patient"})
    public Response update (@PathParam("username") String username, PatientDTO patientDTO) throws MyEntityNotFoundException {
        try{
            Patient p = patientBean.findOrFail(username);
            if(securityContext.isUserInRole("Patient")){
                if(!securityContext.getUserPrincipal().getName().equals(username)){
                    return Response.status(Response.Status.FORBIDDEN).build();
                }
            }
            if(securityContext.isUserInRole("Medic")){
                if(!securityContext.getUserPrincipal().getName().equals(p.getMedic().getUsername())){
                    return Response.status(Response.Status.FORBIDDEN).build();
                }
            }
            patientBean.update(username, patientDTO.getName(), patientDTO.getPassword(), patientDTO.getContact(), patientDTO.getAddress(), patientDTO.getEmail());

            return Response.status(Response.Status.OK).build();
        }catch (MyEntityNotFoundException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

    }

    @POST
    @Path("{username}/email")
    @RolesAllowed({"Administrator", "Medic"})
    public Response sendEmail(@PathParam("username") String username, EmailDTO email) {
        try{
            Patient patient = patientBean.findOrFail(username);
            emailBean.send(patient.getEmail(), email.getSubject(), email.getMessage());
            return Response.status(Response.Status.OK).entity("Email sent").build();
        }catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
