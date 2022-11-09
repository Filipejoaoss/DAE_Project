package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllPatients",
                query = "SELECT p FROM Patient p ORDER BY p.name"
        )
})
public class Patient extends Person {

    @ManyToOne
    private Medic medic;

    @Version
    private int version;

    public Patient() {
    }

    public Patient(@NotNull String username, @NotNull String name, @NotNull String password, String contact, String address, @Email String email) {
        super(username, name, password, contact, address, email);
    }

    public Medic getMedic() {
        return medic;
    }

    public void setMedic(Medic medic) {
        this.medic = medic;
    }
}