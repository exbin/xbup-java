/*
 * Copyright (C) XBUP Project
 *
 * This application or library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This application or library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along this application.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.xbup.lib.catalog.entity.manager;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.xbup.lib.core.catalog.base.XBCBase;
import org.xbup.lib.core.catalog.base.manager.XBCManager;
import org.xbup.lib.catalog.XBECatalog;

/**
 * Default manager for catalog items.
 *
 * @version 0.1.23 2014/03/25
 * @author XBUP Project (http://xbup.org)
 * @param <T> entity class
 */
public class XBEDefaultManager<T extends XBCBase> implements XBCManager<T> {

    @Autowired
    protected XBECatalog catalog;
    @PersistenceContext
    protected EntityManager em;

    public XBEDefaultManager() {
    }

    public XBEDefaultManager(XBECatalog catalog) {
        this.catalog = catalog;
        this.em = catalog.getEntityManager();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T createItem() {
        try {
            return (T) getGenericClass().newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(XBEDefaultManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(XBEDefaultManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void removeItem(T item) {
        // EntityTransaction transaction = em.getTransaction();
        // transaction.begin();
        T removedItem = em.merge(item);
        em.remove(removedItem);
        // em.flush();
        // transaction.commit();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> getAllItems() {
        try {
            List result = em.createQuery("SELECT object(o) FROM " + getTableName() + " as o").getResultList();
            return (List<T>) result;
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEDefaultManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getItem(long itemId) {
        Object result = em.find(getGenericClass(), itemId);
        return (T) result;
        /*        try {
         return (T) em.createQuery("SELECT object(o) FROM XBItem as o WHERE o.id = "+ itemId).getSingleResult();
         } catch (NoResultException ex) {
         return null;
         } catch (Exception ex) {
         Logger.getLogger(XBEDefaultManager.class.getName()).log(Level.SEVERE, null, ex);
         return null;
         } */
    }

    @Override
    public long getItemsCount() {
        try {
            return (Long) em.createQuery("SELECT count(o) FROM " + getTableName() + " as o").getSingleResult();
        } catch (NoResultException ex) {
            Logger.getLogger(XBEDefaultManager.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        } catch (Exception ex) {
            Logger.getLogger(XBEDefaultManager.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public void persistItem(T item) {
        // EntityTransaction transaction = em.getTransaction();
        // transaction.begin();
        em.merge(item);
        // em.flush();
        // transaction.commit();
    }

    /**
     * Get class on which is this class parametrized / generic of.
     *
     * @return generic class of this class
     */
    public Class getGenericClass() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class) parameterizedType.getActualTypeArguments()[0];
    }

    /**
     * Get table name for this manager.
     *
     * @return table name
     */
    @SuppressWarnings("unchecked")
    public String getTableName() {
        Class genClass = getGenericClass();
        Annotation annotation = genClass.getAnnotation(Entity.class);
        if (annotation != null) {
            return ((Entity) annotation).name();
        }

        return genClass.getSimpleName();
    }
}
