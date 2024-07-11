package br.com.don.repository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;


public abstract class GenericRepository<T, ID extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Class<T> entityClass;

    @Inject
    protected EntityManager entityManager;


    @SuppressWarnings("unchecked")
    public GenericRepository() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }


    public T find(Object value) {
        String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e = :value";
        try {
            TypedQuery<T> query = entityManager.createQuery(jpql, entityClass)
                .setParameter("value", value);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    public T findById(ID id) {
        try {
            return entityManager.find(entityClass, id);
        } catch (NoResultException e) {
            return null;
        }
    }


    public void deleteById(ID id) {
        T entity = this.findById(id);
        if (entity != null) {
            this.delete(entity);
        }
    }


    public List<T> findAll() {
        String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
        try {
            TypedQuery<T> query = entityManager.createQuery(jpql, entityClass);
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }


    public List<T> findAllByProperty(String propertyName, Object value) {
        String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e." + propertyName + " = :value";
        try {
            TypedQuery<T> query = entityManager.createQuery(jpql, entityClass)
                .setParameter("value", value);
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }


    public T findByProperty(String propertyName, Object value) {
        String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e." + propertyName + " = :value";
        try {
            TypedQuery<T> query = entityManager.createQuery(jpql, entityClass)
                .setParameter("value", value);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    public List<T> findDTOs(String jpql, Class<T> dtoClass) {
        try {
            TypedQuery<T> query = entityManager.createQuery(jpql, dtoClass);
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }


    public T save(T entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
        return entity;
    }


    public void update(T entity) {
        entityManager.getTransaction().begin();
        entityManager.merge(entity);
        entityManager.getTransaction().commit();
    }


    public void delete(T entity) {
        entityManager.getTransaction().begin();
        entityManager.remove(entity);
        entityManager.getTransaction().commit();
    }
}
