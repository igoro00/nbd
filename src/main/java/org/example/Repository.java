package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.example.model.ModelEntity;
import java.util.List;
import java.util.UUID;

@Transactional
public class Repository<T extends ModelEntity> {

    @PersistenceContext
    private EntityManager em;

    private final Class<T> entityClass;

    public Repository(Class<T> entityClass, EntityManager em){
        this.entityClass = entityClass;
        this.em = em;
    }

    public T add(T element) {
        try{
            this.em.getTransaction().begin();
            if (element.getId() == null){
                em.persist(element);
            } else {
                element = em.merge(element);
            }
            this.em.getTransaction().commit();
            return element;
        } catch (Exception e) {
            this.em.getTransaction().rollback();
            throw e;
        }
    }

    public T findById(UUID id) {
        return em.find(entityClass, id);
    }

    public List<T> findAll() {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> rootEntry = cq.from(entityClass);
        CriteriaQuery<T> all = cq.select(rootEntry);
        return em.createQuery(all).getResultList();
    }
}
