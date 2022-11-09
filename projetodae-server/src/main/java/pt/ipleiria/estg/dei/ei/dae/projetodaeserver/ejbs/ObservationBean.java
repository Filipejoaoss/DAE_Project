package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.*;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.utils.MyDateTime;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class ObservationBean extends BaseBean<Observation,Integer> {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private PatientBean patientBean;

    @EJB
    private MedicBean medicBean;

    @EJB
    private BiomedicDataTypeBean biomedicDataTypeBean;

    public void create(String patientUsername, String examiner, MyDateTime dateTime, int dataTypeId, String qualitative, double numeric) throws MyEntityNotFoundException {
        biomedicDataTypeBean.all(); //Needs this to refresh the database so it can find the biodatatypes, can be removed if in production
        BiomedicDataType bio = biomedicDataTypeBean.findOrFail(dataTypeId);
        QualitativeValue qualitativeValue = new QualitativeValue(bio,qualitative);
        em.persist(qualitativeValue);
        NumericValue numericValue = new NumericValue(bio,numeric);
        em.persist(numericValue);
        Patient p = patientBean.findOrFail(patientUsername);
        Observation obs = new Observation(p,examiner,dateTime,bio,qualitativeValue,numericValue);
        em.persist(obs);
    }

    public List<Observation> patientAll(String username) {
        return em.createQuery(
                "SELECT o FROM Observation o WHERE o.patient.username LIKE :username ORDER BY o.id")
                .setParameter("username", username)
                .getResultList();
    }

    public void patch(Observation observation, BiomedicDataType biomedicDataType, String qualitative, double numeric) throws Exception {
        Observation o = findOrFail(observation.getId());
        try{
            QualitativeValue q = em.find(QualitativeValue.class,o.getQualitative().getId());
            NumericValue n = em.find(NumericValue.class,o.getNumeric().getId());
            q.setDataType(biomedicDataType);
            q.setQualitativeValue(qualitative);
            n.setDataType(biomedicDataType);
            n.setValue(numeric);
            o.setDataType(biomedicDataType);
            em.merge(q);
            em.merge(n);
            em.merge(o);
        }catch (Exception e){
            throw new Exception("Something went wrong: " + e.getMessage());
        }
    }

    @Override
    public void beforeDestroy(Observation entity) {
        return;
    }
}
