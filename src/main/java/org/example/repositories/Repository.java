package org.example.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.example.model.ModelEntity;

import java.util.List;
import java.util.UUID;

@Transactional
public class Repository<T extends ModelEntity> {
    private final EntityManager em;

    private final Class<T> entityClass;

    public Repository(Class<T> entityClass, EntityManager em) {
        this.entityClass = entityClass;
        this.em = em;
    }

    public T addWithoutTransaction(T element) {
        if (element.getId() == null){
            em.persist(element);
        } else {
            element = em.merge(element);
        }
        return element;
    }

    public T add(T element) {
        em.getTransaction().begin();
        T managedElement = addWithoutTransaction(element);
        em.getTransaction().commit();
        return managedElement;
    }

    public T findById(UUID id) {
        return em.find(entityClass, id);
    }

    public int countAll() {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> root = cq.from(entityClass);
        cq.select(cb.count(root));
        return this.em.createQuery(cq).getSingleResult().intValue();
    }

    public List<T> findAll() {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> rootEntry = cq.from(entityClass);
        CriteriaQuery<T> all = cq.select(rootEntry);
        return em.createQuery(all).getResultList();
    }

    public EntityManager getEM() {
        return em;
    }
}
