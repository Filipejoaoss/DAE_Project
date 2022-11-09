package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.dtos;

import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.utils.MyDateTime;

import java.io.Serializable;

public class PrescriptionDTO implements Serializable {

    private int id;
    private String patientUsername;
    private MyDateTime startDate;
    private MyDateTime endDate;
    private boolean isActive;
    private String title;
    private String description;
    private int observationId;

    public PrescriptionDTO() {
    }

    public PrescriptionDTO(int id, String patientUsername, MyDateTime startDate, MyDateTime endDate, boolean isActive,String title, String description, int observationId) {
        this.id = id;
        this.patientUsername = patientUsername;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
        this.title = title;
        this.description = description;
        this.observationId = observationId;
    }

    public PrescriptionDTO(int id, String patientUsername, String startDate, String endDate, boolean isActive,  String title, String description, int observationId) {
        this.id = id;
        this.patientUsername = patientUsername;
        this.startDate = new MyDateTime(startDate);
        this.endDate = new MyDateTime(endDate);
        this.isActive = isActive;
        this.title = title;
        this.description = description;
        this.observationId = observationId;
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

    public MyDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(MyDateTime startDate) {
        this.startDate = startDate;
    }

    public MyDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(MyDateTime endDate) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getObservationId() {
        return observationId;
    }

    public void setObservationId(int observationId) {
        this.observationId = observationId;
    }
}
