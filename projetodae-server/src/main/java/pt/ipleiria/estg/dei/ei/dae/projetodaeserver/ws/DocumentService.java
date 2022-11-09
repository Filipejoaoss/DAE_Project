package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ws;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ejbs.DocumentBean;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ejbs.ObservationBean;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.Document;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.Observation;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.utils.EntityToDTO;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("documents") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”
public class DocumentService {

    @Context
    private SecurityContext securityContext;

    @EJB
    private DocumentBean documentBean;

    @EJB
    private ObservationBean observationBean;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator","Medic", "Patient"})
    public Response all(){
        if(securityContext.isUserInRole("Medic")){
            return Response.status(Response.Status.OK).entity(new EntityToDTO().documentToDTOs(documentBean.medicAll(securityContext.getUserPrincipal().getName()))).build();
        }
        if(securityContext.isUserInRole("Patient")){
            return Response.status(Response.Status.OK).entity(new EntityToDTO().documentToDTOs(documentBean.patientAll(securityContext.getUserPrincipal().getName()))).build();
        }
        return Response.status(Response.Status.OK).entity(new EntityToDTO().documentToDTOs(documentBean.all())).build();
    }

    @GET
    @Path("/observations/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator","Medic", "Patient"})
    public Response observationsAll(@PathParam("id") int id){
        Observation observation;
        try {
            observation = observationBean.findOrFail(id);
        }catch (MyEntityNotFoundException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if(securityContext.isUserInRole("Medic")){
            if(!securityContext.getUserPrincipal().getName().equals(observation.getPatient().getMedic().getUsername())){
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        if(securityContext.isUserInRole("Patient")){
            if(!securityContext.getUserPrincipal().getName().equals(observation.getPatient().getUsername())){
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        return Response.status(Response.Status.OK).entity(new EntityToDTO().documentToDTOs(documentBean.observationsAll(observation.getId()))).build();
    }



    @POST
    @Path("/observations/{id}/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RolesAllowed({"Administrator","Medic", "Patient"})
    public Response upload(@PathParam("id") int observationId, MultipartFormDataInput input) throws IOException {
        try{
            Observation observation = observationBean.findOrFail(observationId);
            if(securityContext.isUserInRole("Patient")){
                if(!securityContext.getUserPrincipal().getName().equals(observation.getPatient().getUsername())){
                    return Response.status(Response.Status.FORBIDDEN).build();
                }
            }
            if(securityContext.isUserInRole("Medic")){
                if(!securityContext.getUserPrincipal().getName().equals(observation.getPatient().getMedic().getUsername())){
                    return Response.status(Response.Status.FORBIDDEN).build();
                }
            }
        }catch (MyEntityNotFoundException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        // Get file data to save
        List<InputPart> inputParts = uploadForm.get("file");
        for (InputPart inputPart : inputParts) {
            try {
                MultivaluedMap<String, String> header = inputPart.getHeaders();
                String filename = getFilename(header);
                // convert the uploaded file to inputstream
                InputStream inputStream = inputPart.getBody(InputStream.class, null);
                byte[] bytes = IOUtils.toByteArray(inputStream);
                String path = System.getProperty("user.home") + File.separator + "uploads";
                File customDir = new File(path);
                if (!customDir.exists()) {
                    customDir.mkdir();
                }
                String filepath = customDir.getCanonicalPath() + File.separator + filename;
                writeFile(bytes, filepath);
                documentBean.create(path, filename, observationId);
                return Response.status(Response.Status.OK).entity("Uploaded file name : " + filename).build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @RolesAllowed({"Administrator","Medic", "Patient"})
    public Response download(@PathParam("id") Integer id) throws MyEntityNotFoundException {
        Document document = documentBean.findOrFail(id);

        if(securityContext.isUserInRole("Medic")){
            if(!securityContext.getUserPrincipal().getName().equals(document.getObservation().getPatient().getMedic().getUsername())){
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        if(securityContext.isUserInRole("Patient")){
            if(!securityContext.getUserPrincipal().getName().equals(document.getObservation().getPatient().getUsername())){
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }

        File fileDownload = new File(document.getFilePath() + File.separator +
                document.getFileName());
        Response.ResponseBuilder response = Response.ok((Object) fileDownload);
        response.header("Content-Disposition", "attachment;filename=" +
                document.getFileName());
        return response.build();
    }


    @DELETE
    @Path("/{id}")
    @RolesAllowed({"Administrator","Medic"})
    public Response delete(@PathParam("id") int id) throws IOException, MyEntityNotFoundException {
        Document d = documentBean.findOrFail(id);
        if(securityContext.isUserInRole("Medic")){
            if(securityContext.getUserPrincipal().getName().equals(d.getObservation().getPatient().getMedic().getUsername())){
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        String path = System.getProperty("user.home") + File.separator + "uploads";
        File customDir = new File(path);
        if (!customDir.exists()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(false).build();
        }
        String filepath = customDir.getCanonicalPath() + File.separator + d.getFileName();
        if(!deleteFile(filepath))
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(false).build();
        documentBean.destroy(d.getId());
        return Response.status(Response.Status.OK).entity(true).build();
    }

    private String getFilename(MultivaluedMap<String, String> header) {
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {
                String[] name = filename.split("=");
                String finalFileName = name[1].trim().replaceAll("\"", "");
                return finalFileName;
            }
        }
        return "unknown";
    }

    private void writeFile(byte[] content, String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fop = new FileOutputStream(file);
        fop.write(content);
        fop.flush();
        fop.close();
        System.out.println("Written: " + filename);
    }

    private boolean deleteFile(String filename){
        File file = new File(filename);
        if (!file.exists()) {
            return false;
        }
        file.delete();
        if (file.exists()) {
            return false;
        }
        return true;
    }

}
