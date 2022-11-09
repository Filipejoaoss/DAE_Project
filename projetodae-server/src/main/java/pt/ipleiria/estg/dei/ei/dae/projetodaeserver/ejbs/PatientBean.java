package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

@Stateless
public class PatientBean extends BaseBean<Patient, String>{

    @PersistenceContext
    private EntityManager entityManager;

    public void create(String username, String name, String password, String contact, String address, String email) throws MyEntityExistsException, MyConstraintViolationException {
        Patient patient = find(username);

        if (patient != null) {
            throw new MyEntityExistsException("Patient with username " + username + "already exist!");
        }

        try {
            patient = new Patient(username, name, password, contact, address, email);
            entityManager.persist(patient);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public void update(String username, String name, String password, String contact, String address, String email) throws MyEntityNotFoundException {
        Patient patient = findOrFail(username);


        entityManager.lock(patient, LockModeType.OPTIMISTIC);
        patient.setName(name);
        patient.setPassword(password);
        patient.setContact(contact);
        patient.setAddress(address);
        patient.setEmail(email);

        entityManager.merge(patient);
    }


    @Override
    public void beforeDestroy(Patient entity) {
        return;
    }
}
