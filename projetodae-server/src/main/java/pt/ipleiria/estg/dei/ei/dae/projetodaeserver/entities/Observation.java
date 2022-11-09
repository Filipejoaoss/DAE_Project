package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities;

import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.utils.MyDateTime;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
public class Observation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Patient patient;


    private String examiner;

    private MyDateTime dateTime;

    @ManyToOne
    private BiomedicDataType dataType;

    @ManyToOne
    private QualitativeValue qualitative;

    @ManyToOne
    private NumericValue numeric;

    public Observation() {
    }

    public Observation(Patient patient, String examiner, MyDateTime dateTime, BiomedicDataType dataType, QualitativeValue qualitative, NumericValue numeric) {
        this.patient = patient;
        this.examiner = examiner;
        this.dateTime = dateTime;
        this.dataType = dataType;
        this.qualitative = qualitative;
        this.numeric = numeric;
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

    /*public Medic getMedic(){
        return patient.getMedic();
    }*/

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
        this.dateTime = dateTime;
    }

    public BiomedicDataType getDataType() {
        return dataType;
    }

    public void setDataType(BiomedicDataType dataType) {
        this.dataType = dataType;
    }

    public QualitativeValue getQualitative() {
        return qualitative;
    }

    public void setQualitative(QualitativeValue qualitative) {
        this.qualitative = qualitative;
    }

    public NumericValue getNumeric() {
        return numeric;
    }

    public void setNumeric(NumericValue numeric) {
        this.numeric = numeric;
    }
}
