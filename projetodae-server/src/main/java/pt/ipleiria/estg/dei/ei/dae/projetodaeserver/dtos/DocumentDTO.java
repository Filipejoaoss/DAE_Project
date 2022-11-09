package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.dtos;

import java.io.Serializable;

public class DocumentDTO implements Serializable {
    private int id;
    private String filePath;
    private String fileName;
    private int observationId;

    public DocumentDTO() {
    }

    public DocumentDTO(int id, String filePath, String fileName, int observationId) {
        this.id = id;
        this.filePath = filePath;
        this.fileName = fileName;
        this.observationId = observationId;
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

    public int getObservationId() {
        return observationId;
    }

    public void setObservationId(int observationId) {
        this.observationId = observationId;
    }
}
