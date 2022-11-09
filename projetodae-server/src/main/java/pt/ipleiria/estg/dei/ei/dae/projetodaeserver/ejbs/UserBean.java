package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.Person;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserBean {

    @PersistenceContext
    EntityManager em;

    public Person authenticate(final String username, final String password) throws Exception {
        Person user = em.find(Person.class, username);
        if (user != null &&
                user.getPassword().equals(Person.hashPassword(password))) {
            return user;
        }
        throw new Exception("Failed logging in with username '" + username + "': unknown username or wrong password");
    }
}