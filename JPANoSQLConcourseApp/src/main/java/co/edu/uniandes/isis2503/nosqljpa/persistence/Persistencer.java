/*
 * The MIT License
 *
 * Copyright 2016 Universidad De Los Andes - Departamento de Ingeniería de Sistemas.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package co.edu.uniandes.isis2503.nosqljpa.persistence;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

/**
 *
 * @author Luis Felipe Mendivelso Osorio <lf.mendivelso10@uniandes.edu.co>
 * @param <T>
 * @param <PK>
 */
public class Persistencer<T, PK> {

    private static final Logger LOG = Logger.getLogger(Persistencer.class.getName());
    protected Class<T> entityClass;
    protected EntityManager entityManager;
    
    
    public Persistencer(){
        
        //TODO Agregar código según la guía
        
    }

    public T add(T entity) {
        try {
            
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
            
        } catch (RuntimeException e) {
            LOG.log(Level.WARNING, e.getMessage());
        }
        return entity;

    }

    public T update(T entity) {
        try {
            entityManager.merge(entity);
        } catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage());
        }
        return entity;
    }

    public T find(PK id) {
        T entity;
        try {
            entity = entityManager.find(entityClass, id);
        } catch (NoResultException | NonUniqueResultException e) {
            entity = null;
            LOG.log(Level.WARNING, e.getMessage());
        }
        return entity;
    }

    public T findByName(String name) {
        T entity;
        Query query = entityManager.createQuery("Select e FROM " + entityClass.getSimpleName() + " e WHERE e.name = :name");
        query.setParameter("name", name);
        try {
            entity = (T) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            entity = null;
            LOG.log(Level.WARNING, e.getMessage());
        }
        return entity;
    }

    public T findByEmail(String email) {
        T entity;
        Query query = entityManager.createQuery("Select e FROM " + entityClass.getSimpleName() + " e WHERE e.email = :email");
        query.setParameter("email", email);
        try {
            entity = (T) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            entity = null;
            LOG.log(Level.WARNING, e.getMessage());
        }
        return entity;
    }

    public List<T> all() {
        List<T> entities;
        String queryString = "Select c FROM " + entityClass.getSimpleName() + " c";
        Query query = entityManager.createQuery(queryString);
        try {
            entities = query.getResultList();
        } catch (NoResultException | NonUniqueResultException e) {
            entities = null;
            LOG.log(Level.WARNING, e.getMessage());
        }
        return entities;
    }

    public Boolean delete(T entity) {
        try {
            entity = this.entityManager.merge(entity);
            this.entityManager.remove(entity);
            return true;
        } catch (RuntimeException e) {
            LOG.log(Level.WARNING, e.getMessage());
            return false;
        }
    }
    
}
