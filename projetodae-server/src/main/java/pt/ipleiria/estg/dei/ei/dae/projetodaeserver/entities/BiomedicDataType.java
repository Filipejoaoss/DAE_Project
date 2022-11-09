package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllBiomedicdatatypes",
                query = "SELECT b FROM BiomedicDataType b ORDER BY b.name"
        )
})
public class BiomedicDataType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    private double valueMin;

    private double valueMax;

    private String unity;

    @Version
    private int version;

    public BiomedicDataType() {

    }

    public BiomedicDataType(@NotNull String name,@NotNull String description, double valueMin, double valueMax, String unity) {
        this.name = name;
        this.description = description;
        this.valueMin = valueMin;
        this.valueMax = valueMax;
        this.unity = unity;
    }

    public BiomedicDataType(@NotNull int id, @NotNull String name,@NotNull String description, double valueMin, double valueMax, String unity) {
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
