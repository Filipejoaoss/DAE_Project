package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.dtos;

import java.io.Serializable;

public class BiomedicDataTypeDTO implements Serializable {
    private int id;
    private String name;
    private String description;
    private double valueMin;
    private double valueMax;
    private String unity;

    public BiomedicDataTypeDTO() {
    }

    public BiomedicDataTypeDTO(int id, String name, String description, double valueMin, double valueMax, String unity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.valueMin = valueMin;
        this.valueMax = valueMax;
        this.unity = unity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getValueMin() {
        return valueMin;
    }

    public void setValueMin(double valueMin) {
        this.valueMin = valueMin;
    }

    public double getValueMax() {
        return valueMax;
    }

    public void setValueMax(double valueMax) {
        this.valueMax = valueMax;
    }

    public String getUnity() {
        return unity;
    }

    public void setUnity(String unity) {
        this.unity = unity;
    }
}
