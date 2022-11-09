package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.Medic;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;


@Stateless
public class MedicBean extends BaseBean<Medic, String>{

    @PersistenceContext
    private EntityManager entityManager;

    @EJB
    private PatientBean patientBean;

    public void create(String username, String name, String password, String contact, String address, String email) throws MyEntityExistsException, MyConstraintViolationException {
        Medic medic = find(username);

        if (medic != null){
            throw new MyEntityExistsException("Medic with username " + username + "already exist!");
        }

        try{
            medic = new Medic(username, name, password, contact, address, email);
            entityManager.persist(medic);
        } catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(e);
        }

    }

    public void update(String username, String name, String password, String contact, String address, String email) throws MyEntityNotFoundException {
        Medic medic = findOrFail(username);


        entityManager.lock(medic, LockModeType.OPTIMISTIC);
        medic.setName(name);
        medic.setPassword(password);
        medic.setContact(contact);
        medic.setAddress(address);
        medic.setEmail(email);

        entityManager.merge(medic);
    }

    public boolean addPatientToMedic(String medicUsername, String patientUsername) throws MyEntityNotFoundException {
        Medic medic = findOrFail(medicUsername);
        Patient patient = patientBean.findOrFail(patientUsername);

        if (!medic.getPatients().contains(patient)){
            medic.addPatient(patient);
            patient.setMedic(medic);
        }

        return true;
    }

    public boolean removePatientToMedic(String medicUsername, String patientUsername) throws MyEntityNotFoundException {
        Medic medic = findOrFail(medicUsername);

        Patient patient = patientBean.findOrFail(patientUsername);

        medic.removePatient(patient);
        patient.setMedic(null);

        return true;
    }

    @Override
    public void beforeDestroy(Medic entity) {
        return;
    }
}
