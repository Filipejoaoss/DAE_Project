package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.Medic;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

@Stateless
public class AdministratorBean extends BaseBean<Administrator, String>{

    @PersistenceContext
    private EntityManager entityManager;

    public void create(String username, String name, String password, String contact, String address, String email) throws MyEntityExistsException, MyConstraintViolationException {
        Administrator administrator = find(username);

        if (administrator != null){
            throw new MyEntityExistsException("Administrator with username " + username + "already exist!");
        }

        try{
            administrator = new Administrator(username, name, password, contact, address, email);
            entityManager.persist(administrator);
        } catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(e);
        }

    }

    public void update(String username, String name, String password, String contact, String address, String email) throws MyEntityNotFoundException {
        Administrator administrator = findOrFail(username);


        entityManager.lock(administrator, LockModeType.OPTIMISTIC);
        administrator.setName(name);
        administrator.setPassword(password);
        administrator.setContact(contact);
        administrator.setAddress(address);
        administrator.setEmail(email);

        entityManager.merge(administrator);
    }

    @Override
    public void beforeDestroy(Administrator entity) {
        return;
    }
}
