package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.model.ModelEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Repository<T extends ModelEntity> {

    private final EntityManager em;

    public Repository(EntityManager em){
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

    public T findById(Long id) {
        return em.find(T.class, id);
    }

    public List<T> getAll() {
        return null;
        TypedQuery<T> query = em.createQuery("Select c from Client c", T.class);
        return query.getResultList();
    }
}
