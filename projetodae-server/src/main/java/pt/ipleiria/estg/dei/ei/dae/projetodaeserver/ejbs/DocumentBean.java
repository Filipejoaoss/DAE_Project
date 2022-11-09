package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.Document;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.Observation;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.Prescription;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class DocumentBean extends BaseBean<Document,Integer>{

    @PersistenceContext
    private EntityManager em;

    @EJB
    private ObservationBean observationBean;

    public void create(String filePath, String fileName, int observationId) throws MyEntityNotFoundException {
        try{
            Observation o = observationBean.findOrFail(observationId);
            Document d = new Document(filePath,fileName,o);
            em.persist(d);
        }catch (ConstraintViolationException e){
        }
    }

    public List<Document> medicAll(String username) {
        return em.createQuery(
                        "SELECT d FROM Document d WHERE d.observation.patient.medic.username LIKE :username ORDER BY d.id")
                .setParameter("username", username)
                .getResultList();
    }

    public List<Document> patientAll(String username) {
        return em.createQuery(
                        "SELECT d FROM Document d WHERE d.observation.patient.username LIKE :username ORDER BY d.id")
                .setParameter("username", username)
                .getResultList();
    }

    public List<Document> observationsAll(int id) {
        return em.createQuery(
                        "SELECT d FROM Document d WHERE d.observation.id = :id")
                .setParameter("id", id)
                .getResultList();
    }


}
