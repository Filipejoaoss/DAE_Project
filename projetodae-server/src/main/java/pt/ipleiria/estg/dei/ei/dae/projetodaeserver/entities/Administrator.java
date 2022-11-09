package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllAdministrators",
                query = "SELECT a FROM Administrator a ORDER BY a.name"
        )
})
public class Administrator extends Person{
    @Version
    private int version;

    public Administrator() {
    }

    public Administrator(@NotNull String username, @NotNull String name, @NotNull String password, String contact, String address, @Email String email) {
        super(username, name, password, contact, address, email);
    }
}
