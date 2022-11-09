package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.*;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.utils.MyDateTime;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class PrescriptionBean extends BaseBean<Prescription,Integer>{

    @PersistenceContext
    private EntityManager em;

    @EJB
    private PatientBean patientBean;

    @EJB
    private ObservationBean observationBean;

    public void create(String patientUsername, MyDateTime startDate, MyDateTime endDate, String title, String description, int observationId) throws MyConstraintViolationException, MyEntityNotFoundException {
        Patient patient = patientBean.findOrFail(patientUsername);
        Observation observation = observationBean.findOrFail(observationId);
        try {
            Prescription p = new Prescription(patient,startDate,endDate,title,description,observation);
            em.persist(p);
        }catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(e);
        }
    }
    public void update(int id,MyDateTime startDate, MyDateTime endDate, String title, String description) throws MyEntityNotFoundException {
            Prescription p = findOrFail(id);

            p.setStartDate(startDate);
            p.setEndDate(endDate);
            p.setTitle(title);
            p.setDescription(description);

            em.merge(p);
    }

    public List<Prescription> patientAll(String username) {
        return em.createQuery(
                        "SELECT p FROM Prescription p WHERE p.patient.username LIKE :username ORDER BY p.id")
                .setParameter("username", username)
                .getResultList();
    }

    public List<Prescription> medicAll(String username) {
        return em.createQuery(
                        "SELECT p FROM Prescription p WHERE p.patient.medic.username LIKE :username ORDER BY p.id")
                .setParameter("username", username)
                .getResultList();
    }

    public List<Prescription> observationsAll(int id) {
        return em.createQuery(
                        "SELECT p FROM Prescription p WHERE p.observation.id = :id ORDER BY p.id")
                .setParameter("id", id)
                .getResultList();
    }


}
