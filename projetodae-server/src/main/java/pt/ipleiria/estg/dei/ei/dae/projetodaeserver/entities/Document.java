package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities;

import javax.persistence.*;

@Entity
@NamedQuery(name = "getAllDocuments", query = "SELECT doc FROM Document doc")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String filePath;

    private String fileName;

    @ManyToOne
    private Observation observation;


    public Document() {
    }

    public Document( String filePath, String fileName, Observation observation) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.observation = observation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Observation getObservation() {
        return observation;
    }

    public void setObservation(Observation observation) {
        this.observation = observation;
    }
}
