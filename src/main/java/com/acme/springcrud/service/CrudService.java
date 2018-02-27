/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.springcrud.service;

import com.acme.springcrud.util.ReflectionUtil;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author eu
 */
@Service
@Transactional(readOnly = true)
public class CrudService implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String OBJ_ALIAS = "o";

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = false)
    public <T> T save(T t) {
        if (ReflectionUtil.isNewObject(t)) {
            em.persist(t);
        } else {
            t = em.merge(t);
        }
        em.flush();
        return t;
    }

    public <T> List<T> findAll(Class<T> persistentClass, String... orderFields) {
        String orderByClause = buildOrderByClause(orderFields);
        String jpql = String.format("select %s from %s as %s %s", OBJ_ALIAS, persistentClass.getSimpleName(), OBJ_ALIAS, orderByClause);
        return em.createQuery(jpql).getResultList();
    }
    
    @Transactional(readOnly = false)
    public void delete(Object obj) {
        obj = em.getReference(obj.getClass(), ReflectionUtil.getPKValue(obj));
        em.remove(obj);
        em.flush();
    }    

    public String buildOrderByClause(String... orderFields) {
        String orderByClause = "";
        if (orderFields.length > 0) {
            for (int i = 0; i < orderFields.length; i++) {
                orderFields[i] = OBJ_ALIAS + "." + orderFields[i];
            }
            orderByClause = " order by " + StringUtils.join(orderFields, ", ");
        }
        return orderByClause;
    }
    
    public <T> T find(Class persistentClass, Object id) {
        return (T) em.find(persistentClass, id);
    }
}
