package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.dtos;

import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.BiomedicDataType;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.utils.MyDateTime;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ObservationDTO implements Serializable {
    private int id;
    private String patientUsername;
    private String examiner;
    private MyDateTime dateTime;
    private BiomedicDataTypeDTO biomedicDataType;
    private String qualitative;
    private double value;

    public ObservationDTO() {
    }

    public ObservationDTO(int id, String patientUsername, String examiner,MyDateTime dateTime, BiomedicDataTypeDTO biomedicDataType, String qualitative, double value) {
        this.id = id;
        this.patientUsername = patientUsername;
        this.examiner = examiner;
        this.dateTime = new MyDateTime(dateTime.toString());
        this.biomedicDataType = biomedicDataType;
        this.qualitative = qualitative;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatientUsername() {
        return patientUsername;
    }

    public void setPatientUsername(String patientUsername) {
        this.patientUsername = patientUsername;
    }

    public String getExaminer() {
        return examiner;
    }

    public void setExaminer(String examiner) {
        this.examiner = examiner;
    }

    public MyDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(MyDateTime dateTime) {
        this.dateTime = new MyDateTime(dateTime.toString());
    }

    public BiomedicDataTypeDTO getBiomedicDataType() {
        return biomedicDataType;
    }

    public void setBiomedicDataType(BiomedicDataTypeDTO biomedicDataType) {
        this.biomedicDataType = biomedicDataType;
    }

    public String getQualitative() {
        return qualitative;
    }

    public void setQualitative(String qualitative) {
        this.qualitative = qualitative;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
