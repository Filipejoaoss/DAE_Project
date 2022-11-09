package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllMedics",
                query = "SELECT m FROM Medic m ORDER BY m.name"
        )
})
public class Medic extends Person{

    @OneToMany(mappedBy = "medic", cascade = CascadeType.REMOVE)
    private List<Patient> patients;

    @Version
    private int version;

    public Medic() {
    }

    public Medic(@NotNull String username, @NotNull String name, @NotNull String password, String contact, String address, @Email String email) {
        super(username, name, password, contact, address, email);
        this.patients = new LinkedList<>();
    }

    public Medic(String username, String name, String password, String contact, String address, String email, List<Patient> patients) {
        super(username, name, password, contact, address, email);
        this.patients = patients;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    //fUNCTIONS
    public void addPatient(Patient patient){
        if (!patients.add(patient)){
            this.patients.add(patient);
        }
    }

    public void removePatient(Patient patient){
        if (!patients.contains(patient)){
            return;
        }
        this.patients.remove(patient);
    }


}
