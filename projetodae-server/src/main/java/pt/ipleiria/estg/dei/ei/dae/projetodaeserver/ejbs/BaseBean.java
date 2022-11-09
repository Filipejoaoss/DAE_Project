package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.exceptions.MyEntityNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.NotFoundException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class BaseBean<E, PK> {
    @PersistenceContext
    private EntityManager em;

    private  Class<E> getEntityClass(){
        var paramType = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<E>) paramType.getActualTypeArguments()[0];
    }

    private String getAllQueryName(){
        String str = getEntityClass().getSimpleName().toLowerCase();
        return "getAll" + str.substring(0, 1).toUpperCase() + str.substring(1) + "s";
    }

    public E find (PK primaryKey){
        return em.find(getEntityClass(), primaryKey);
    }

    public E findOrFail(PK primaryKey) throws MyEntityNotFoundException {
        var entity = find(primaryKey);

        if (entity == null){
            throw new MyEntityNotFoundException(getEntityClass().getSimpleName() + " not found with key: " + primaryKey);
        }

        return entity;
    }

    public List<E> all(){
        return em.createNamedQuery(getAllQueryName()).getResultList();
    }

    //Override on each bean and implement
    public void beforeDestroy(E entity){
    }

    public void destroy (PK pk) throws MyEntityNotFoundException {
        var entity = findOrFail(pk);

        beforeDestroy(entity);

        em.remove(entity);
    }

    /*public void beforeCreatingUser(String email) {
        TypedQuery<User> query = em.createNamedQuery("getUserByEmail",User.class).setParameter("email",email);

        if(!query.getResultList().isEmpty()){
            throw new MyPersistenceException();
        }
    }*/
}
