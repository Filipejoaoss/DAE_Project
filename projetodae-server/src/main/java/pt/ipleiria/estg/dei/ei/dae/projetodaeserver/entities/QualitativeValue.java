package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class QualitativeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @ManyToOne
    private BiomedicDataType dataType;

    @NotNull
    private String qualitativeValue;

    public QualitativeValue() {
    }

    public QualitativeValue(@NotNull BiomedicDataType dataType,@NotNull String qualitativeValue) {
        this.dataType = dataType;
        this.qualitativeValue = qualitativeValue;
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

    public String getQualitativeValue() {
        return qualitativeValue;
    }

    public void setQualitativeValue(String qualitativeValue) {
        this.qualitativeValue = qualitativeValue;
    }
}
