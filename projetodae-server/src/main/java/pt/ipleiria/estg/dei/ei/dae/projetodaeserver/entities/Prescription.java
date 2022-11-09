package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities;

import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.utils.MyDateTime;

import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllPrescriptions",
                query = "SELECT p FROM Prescription p ORDER BY p.id"
        )
})
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Patient patient;

    private MyDateTime startDate;

    private MyDateTime endDate;

    @ManyToOne
    private Observation observation;

    private String title;

    private String description;

    @Version
    private int version;

    public Prescription(Patient patient, MyDateTime startDate, MyDateTime endDate, String title, String description, Observation observation) {
        this.patient = patient;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.description = description;
        this.observation = observation;
    }

    public Prescription() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
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

    public Observation getObservation() {
        return observation;
    }

    public void setObservation(Observation observation) {
        this.observation = observation;
    }
}
