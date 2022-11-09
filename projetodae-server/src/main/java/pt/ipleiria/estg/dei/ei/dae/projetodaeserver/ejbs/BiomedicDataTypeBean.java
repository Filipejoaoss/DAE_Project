package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.BiomedicDataType;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

@Stateless
public class BiomedicDataTypeBean extends BaseBean<BiomedicDataType,Integer>{

    @PersistenceContext
    private EntityManager em;

    public void create(String name, String description,double valueMin,double valueMax,String unity)  {
        BiomedicDataType b = new BiomedicDataType(name,description,valueMin,valueMax,unity);
        em.persist(b);
    }

    public BiomedicDataType update(BiomedicDataType bNew) throws MyEntityNotFoundException {
        BiomedicDataType b = findOrFail(bNew.getId());
        em.lock(b, LockModeType.OPTIMISTIC);
        b.setDescription(bNew.getDescription());
        b.setValueMin(bNew.getValueMin());
        b.setValueMax(bNew.getValueMax());
        b.setUnity(bNew.getUnity());
        em.merge(b);
        return b;
    }

    @Override
    public void beforeDestroy(BiomedicDataType entity) {
        return;
    }
}
