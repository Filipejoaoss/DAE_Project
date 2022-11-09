package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ws;

import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.dtos.ObservationDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.dtos.PatientDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ejbs.BiomedicDataTypeBean;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ejbs.ObservationBean;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ejbs.PatientBean;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.BiomedicDataType;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.Observation;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyIllegalArgumentException;
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

@Path("observations")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ObservationService {

    @Context
    private SecurityContext securityContext;

    @EJB
    private ObservationBean observationBean;

    @EJB
    private PatientBean patientBean;

    @EJB
    private BiomedicDataTypeBean biomedicDataTypeBean;


    @GET
    @Path("{username}")
    @RolesAllowed({"Administrator","Medic", "Patient"})
    public Response all(@PathParam("username") String username){
        if(securityContext.isUserInRole("Patient")){
            if(!securityContext.getUserPrincipal().getName().equals(username)){
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        try{
            Patient p = patientBean.findOrFail(username);
            return Response.status(Response.Status.OK).entity(new EntityToDTO().observationToDTOs(observationBean.patientAll(p.getUsername()))).build();
        }catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GET
    @Path("{username}/{id}")
    @RolesAllowed({"Administrator","Medic", "Patient"})
    public Response get(@PathParam("username") String username,@PathParam("id") int id){
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

            var o = observationBean.findOrFail(id);
            if(o.getPatient().getUsername() != p.getUsername()){
                throw new MyIllegalArgumentException("The patient provided is not the one in the observation provided");
            }
            return Response.status(Response.Status.OK).entity(new EntityToDTO().observationToDTO(o)).build();
        }catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("{username}")
    @RolesAllowed({"Administrator","Medic", "Patient"})
    public Response create(@PathParam("username") String username, ObservationDTO observationDTO) throws MyEntityNotFoundException {
        if(securityContext.isUserInRole("Patient")){
            if(!securityContext.getUserPrincipal().getName().equals(username)){
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        if(securityContext.isUserInRole("Medic")){
            if(!securityContext.getUserPrincipal().getName().equals(patientBean.findOrFail(username).getMedic().getUsername())){
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }

        try{
            if(!username.equals(observationDTO.getPatientUsername())){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            observationBean.create(username,observationDTO.getExaminer(),observationDTO.getDateTime(),observationDTO.getBiomedicDataType().getId(),observationDTO.getQualitative(),observationDTO.getValue());
            return Response.status(Response.Status.OK).build();
        }catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("/{username}/{id}")
    @RolesAllowed({"Administrator","Medic","Patient"})
    public Response delete (@PathParam("username") String username, @PathParam("id") int id) throws MyEntityNotFoundException {
        Patient p = patientBean.findOrFail(username);
        Observation o = observationBean.findOrFail(id);
        if(securityContext.isUserInRole("Medic")){
            if(!securityContext.getUserPrincipal().getName().equals(p.getMedic().getUsername()) || !securityContext.getUserPrincipal().getName().equals(o.getPatient().getMedic().getUsername())){
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        if(securityContext.isUserInRole("Patient")){
            if(!securityContext.getUserPrincipal().getName().equals(p.getUsername()) || !securityContext.getUserPrincipal().getName().equals(o.getPatient().getUsername())){
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        observationBean.destroy(o.getId());
        try{
            observationBean.findOrFail(id);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }catch (MyEntityNotFoundException e){
            return Response.status(Response.Status.OK).build();
        }
    }

    @PATCH
    @Path("/{username}/{id}")
    @RolesAllowed({"Administrator","Medic","Patient"})
    public Response patch (@PathParam("username") String username, @PathParam("id") int id, ObservationDTO observationDTO) throws MyEntityNotFoundException {
        try {
            Patient p = patientBean.findOrFail(username);
            Observation o = observationBean.findOrFail(id);
            if (securityContext.isUserInRole("Medic")) {
                if (!securityContext.getUserPrincipal().getName().equals(p.getMedic().getUsername()) || !securityContext.getUserPrincipal().getName().equals(o.getPatient().getMedic().getUsername())) {
                    return Response.status(Response.Status.FORBIDDEN).build();
                }
            }
            if (securityContext.isUserInRole("Patient")) {
                if (!securityContext.getUserPrincipal().getName().equals(p.getUsername()) || !securityContext.getUserPrincipal().getName().equals(o.getPatient().getUsername())) {
                    return Response.status(Response.Status.FORBIDDEN).build();
                }
            }
            BiomedicDataType b;
            try{
                b = biomedicDataTypeBean.findOrFail(observationDTO.getBiomedicDataType().getId());
            }catch (MyEntityNotFoundException e){
                b = o.getDataType();
            }
            observationBean.patch(o,b,observationDTO.getQualitative(),observationDTO.getValue());

            return Response.status(Response.Status.OK).entity(new EntityToDTO().observationToDTO(observationBean.findOrFail(o.getId()))).build();
        }catch (MyEntityNotFoundException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
