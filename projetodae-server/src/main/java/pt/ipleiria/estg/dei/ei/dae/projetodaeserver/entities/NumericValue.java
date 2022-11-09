package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class NumericValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @ManyToOne
    private BiomedicDataType dataType;

    @NotNull
    private double value;

    public NumericValue() {
    }

    public NumericValue(@NotNull BiomedicDataType dataType,@NotNull double value) {
        this.dataType = dataType;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BiomedicDataType getDataType() {
        return dataType;
    }

    public void setDataType(BiomedicDataType dataType) {
        this.dataType = dataType;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
