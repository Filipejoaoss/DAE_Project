package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.utils;

import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.dtos.*;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.*;

import java.util.List;
import java.util.stream.Collectors;

public class EntityToDTO {

    public PatientDTO patientToDTO(Patient patient){
        String medicUsername;
        if(patient.getMedic() == null){
            medicUsername = "";
        }else{
            medicUsername = patient.getMedic().getUsername();
        }
        return new PatientDTO(
                patient.getUsername(),
                patient.getName(),
                patient.getPassword(),
                patient.getContact(),
                patient.getAddress(),
                patient.getEmail(),
                medicUsername
        );
    }
    public List<PatientDTO> patientToDTOs(List<Patient> patients){
        return patients.stream().map(this::patientToDTO).collect(Collectors.toList());
    }

    public ObservationDTO observationToDTO(Observation observation) {
        return new ObservationDTO(
                observation.getId(),
                observation.getPatient().getUsername(),
                observation.getExaminer(),
                observation.getDateTime(),
                new EntityToDTO().biomedicDataTypeToDTO(observation.getDataType()),
                observation.getQualitative().getQualitativeValue(),
                observation.getNumeric().getValue()
        );
    }

    public List<ObservationDTO> observationToDTOs(List<Observation> observations) {
        return observations.stream().map(this::observationToDTO).collect(Collectors.toList());
    }

    public AdministratorDTO administratorToDTO(Administrator administrator) {
        return new AdministratorDTO(
                administrator.getUsername(),
                administrator.getName(),
                administrator.getPassword(),
                administrator.getContact(),
                administrator.getAddress(),
                administrator.getEmail()
        );
    }

    public List<AdministratorDTO> administratorToDTOs(List<Administrator> administrators) {
        return administrators.stream().map(this::administratorToDTO).collect(Collectors.toList());
    }

    public MedicDTO medicToDTO(Medic medic) {
        return new MedicDTO(
                medic.getUsername(),
                medic.getName(),
                medic.getPassword(),
                medic.getContact(),
                medic.getAddress(),
                medic.getEmail()
        );
    }

    public List<MedicDTO> medicToDTOs(List<Medic> medics) {
        return medics.stream().map(this::medicToDTO).collect(Collectors.toList());
    }

    public BiomedicDataTypeDTO biomedicDataTypeToDTO(BiomedicDataType biomedicDataType) {
        return new BiomedicDataTypeDTO(
                biomedicDataType.getId(),
                biomedicDataType.getName(),
                biomedicDataType.getDescription(),
                biomedicDataType.getValueMin(),
                biomedicDataType.getValueMax(),
                biomedicDataType.getUnity()
        );
    }

    public List<BiomedicDataTypeDTO> biomedicDataTypeToDTOs(List<BiomedicDataType> biomedicDataTypes) {
        return biomedicDataTypes.stream().map(this::biomedicDataTypeToDTO).collect(Collectors.toList());
    }

    public PrescriptionDTO prescriptionToDTO(Prescription prescription) {
        return new PrescriptionDTO(
                prescription.getId(),
                prescription.getPatient().getUsername(),
                prescription.getStartDate(),
                prescription.getEndDate(),
                MyDateTime.todayInBetween(prescription.getStartDate(), prescription.getEndDate()),
                prescription.getTitle(),
                prescription.getDescription(),
                prescription.getObservation().getId()
        );
    }

    public List<PrescriptionDTO> prescriptionToDTOs(List<Prescription> prescriptions) {
        return prescriptions.stream().map(this::prescriptionToDTO).collect(Collectors.toList());
    }

    public DocumentDTO documentToDTO(Document document) {
        return new DocumentDTO(
                document.getId(),
                document.getFilePath(),
                document.getFileName(),
                document.getObservation().getId());
    }
    public List<DocumentDTO> documentToDTOs(List<Document> documents) {
        return documents.stream().map(this::documentToDTO).collect(Collectors.toList());
    }
}
