package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ws;

import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.dtos.BiomedicDataTypeDTO;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ejbs.BiomedicDataTypeBean;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.BiomedicDataType;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.utils.EntityToDTO;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("biomedicDataTypes")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class BiomedicDataTypeService {
    @EJB
    private BiomedicDataTypeBean biomedicDataTypeBean;

    @GET
    @Path("/")
    @RolesAllowed({"Administrator","Medic","Patient"})
    public Response getAllBiomedicDataTypesWS() {
        return Response.status(Response.Status.OK).entity(new EntityToDTO().biomedicDataTypeToDTOs(biomedicDataTypeBean.all())).build();
    }

    @GET
    @Path("{id}")
    @RolesAllowed({"Administrator","Medic","Patient"})
    public Response get(@PathParam("id") int id) throws MyEntityNotFoundException {
        BiomedicDataType b = biomedicDataTypeBean.findOrFail(id);
        return Response.status(Response.Status.OK).entity(new EntityToDTO().biomedicDataTypeToDTO(b)).build();
    }

    @POST
    @Path("/")
    @RolesAllowed({"Administrator","Medic"})
    public Response create(BiomedicDataTypeDTO b){
        try{
            biomedicDataTypeBean.create(
                    b.getName(),
                    b.getDescription(),
                    b.getValueMin(),
                    b.getValueMax(),
                    b.getUnity()
            );
            return Response.status(Response.Status.CREATED).entity("Created").build();
        }catch(Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"Administrator","Medic"})
    public Response update(@PathParam("id") int id, BiomedicDataTypeDTO bDto) throws MyEntityNotFoundException {
        biomedicDataTypeBean.findOrFail(id);
        try {
            BiomedicDataType b = new BiomedicDataType(
                    bDto.getId(),
                    bDto.getName(),
                    bDto.getDescription(),
                    bDto.getValueMin(),
                    bDto.getValueMax(),
                    bDto.getUnity()
            );
            biomedicDataTypeBean.update(b);
            return Response.status(Response.Status.OK).build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"Administrator","Medic"})
    public Response delete(@PathParam("id") int id) throws MyEntityNotFoundException {
        BiomedicDataType b = biomedicDataTypeBean.findOrFail(id);
        try {
            biomedicDataTypeBean.destroy(id);
            return Response.status(Response.Status.OK).build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }


}
