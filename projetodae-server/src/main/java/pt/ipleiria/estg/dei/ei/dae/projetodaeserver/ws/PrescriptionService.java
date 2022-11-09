package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ws;

import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.dtos.PatientDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.dtos.PrescriptionDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ejbs.MedicBean;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ejbs.ObservationBean;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ejbs.PatientBean;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ejbs.PrescriptionBean;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.Medic;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.Observation;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.Prescription;
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

@Path("prescriptions")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class PrescriptionService {

    @Context
    private SecurityContext securityContext;

    @EJB
    private PrescriptionBean prescriptionBean;

    @EJB
    private PatientBean patientBean;

    @EJB
    private ObservationBean observationBean;

    @EJB
    private MedicBean medicBean;

    @GET
    @Path("/")
    @RolesAllowed({"Administrator","Medic", "Patient"})
    public Response all() {
        if(securityContext.isUserInRole("Administrator")){
            return Response.status(Response.Status.OK).entity(new EntityToDTO().prescriptionToDTOs(prescriptionBean.all())).build();
        }
        if(securityContext.isUserInRole("Medic")){
            return Response.status(Response.Status.OK).entity(new EntityToDTO().prescriptionToDTOs(prescriptionBean.medicAll(securityContext.getUserPrincipal().getName()))).build();
        }
        if(securityContext.isUserInRole("Patient")){
            return Response.status(Response.Status.OK).entity(new EntityToDTO().prescriptionToDTOs(prescriptionBean.patientAll(securityContext.getUserPrincipal().getName()))).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @GET
    @Path("/observations/{id}")
    @RolesAllowed({"Administrator","Medic", "Patient"})
    public Response observationsAll(@PathParam("id") int id) throws MyEntityNotFoundException {
        try{
            Observation o = observationBean.findOrFail(id);
            if(securityContext.isUserInRole("Medic")){
                if(!securityContext.getUserPrincipal().getName().equals(o.getPatient().getMedic().getUsername())){
                    return Response.status(Response.Status.FORBIDDEN).build();
                }
            }
            if(securityContext.isUserInRole("Patient")){
                if(!securityContext.getUserPrincipal().getName().equals(o.getPatient().getUsername())){
                    return Response.status(Response.Status.FORBIDDEN).build();
                }
            }
            return Response.status(Response.Status.OK).entity(new EntityToDTO().prescriptionToDTOs(prescriptionBean.observationsAll(o.getId()))).build();
        }catch (MyEntityNotFoundException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @GET
    @Path("{id}")
    @RolesAllowed({"Administrator","Medic", "Patient"})
    public Response get(@PathParam("id") int id){
        try{
            Prescription p = prescriptionBean.findOrFail(id);
            if(securityContext.isUserInRole("Patient")){
                if(!securityContext.getUserPrincipal().getName().equals(p.getPatient().getUsername())){
                    return Response.status(Response.Status.FORBIDDEN).build();
                }
            }
            if(securityContext.isUserInRole("Medic")){
                if(!securityContext.getUserPrincipal().getName().equals(p.getPatient().getMedic().getUsername())){
                    return Response.status(Response.Status.FORBIDDEN).build();
                }
            }
            return Response.status(Response.Status.OK).entity(new EntityToDTO().prescriptionToDTO(p)).build();
        } catch (MyEntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("ERROR_FINDING_PRESCRIPTION")
                    .build();
        }
    }

    @POST
    @Path("/")
    @RolesAllowed({"Administrator","Medic"})
    public Response create (PrescriptionDTO prescriptionDTO) throws MyConstraintViolationException, MyEntityExistsException, MyEntityNotFoundException {
        Patient p = patientBean.findOrFail(prescriptionDTO.getPatientUsername());
        Observation o = observationBean.findOrFail(prescriptionDTO.getObservationId());
        if (securityContext.isUserInRole("Medic")) {
            if(!securityContext.getUserPrincipal().getName().equals(p.getMedic().getUsername())){
                return Response.status(Response.Status.FORBIDDEN).entity("You are not the medic of this patient").build();
            }
        }
        prescriptionBean.create(
                p.getUsername(),
                prescriptionDTO.getStartDate(),
                prescriptionDTO.getEndDate(),
                prescriptionDTO.getTitle(),
                prescriptionDTO.getDescription(),
                o.getId()
        );
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    @RolesAllowed({"Administrator","Medic"})
    public Response update(@PathParam("id") int id, PrescriptionDTO prescriptionDTO) throws MyEntityNotFoundException {
        Patient patient = patientBean.findOrFail(prescriptionDTO.getPatientUsername());
        if (securityContext.isUserInRole("Medic")) {
            if(!securityContext.getUserPrincipal().getName().equals(patient.getMedic().getUsername())){
                return Response.status(Response.Status.FORBIDDEN).entity("You are not the medic of this patient").build();
            }
        }
        Prescription p = prescriptionBean.findOrFail(prescriptionDTO.getObservationId());
        prescriptionBean.update(id,prescriptionDTO.getStartDate(),prescriptionDTO.getEndDate(),prescriptionDTO.getTitle(),prescriptionDTO.getDescription());
        return Response.status(Response.Status.OK).build();
    }


    @DELETE
    @Path("{id}")
    @RolesAllowed({"Administrator","Medic"})
    public Response delete(@PathParam("id") int id){
        try {
            Prescription p = prescriptionBean.findOrFail(id);
            if (securityContext.isUserInRole("Medic")) {
                if(!securityContext.getUserPrincipal().getName().equals(p.getPatient().getMedic().getUsername())){
                    return Response.status(Response.Status.FORBIDDEN).entity("You are not the medic of this patient").build();
                }
            }
            prescriptionBean.destroy(p.getId());
            return Response.status(Response.Status.OK).entity(true).build();
        }catch (MyEntityNotFoundException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


}
