/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.catalog.entity.manager;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.exbin.xbup.core.catalog.base.XBCBase;
import org.exbin.xbup.core.catalog.base.manager.XBCManager;

/**
 * Default manager for entity items.
 *
 * @version 0.2.1 2020/08/17
 * @author ExBin Project (http://exbin.org)
 * @param <T> entity class
 */
@ParametersAreNonnullByDefault
public class XBEDefaultManager<T extends XBCBase> implements XBCManager<T> {

    @PersistenceContext
    protected EntityManager em;

    public XBEDefaultManager() {
    }

    public XBEDefaultManager(EntityManager entityManager) {
        this.em = entityManager;
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    @Override
    public T createItem() {
        try {
            return (T) getGenericClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
                | SecurityException | IllegalArgumentException | InvocationTargetException ex) {
            throw new IllegalStateException("Unable to create new item", ex);
        }
    }

    @Override
    public void removeItem(T item) {
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
        } catch (IllegalStateException ex) {
        }

        T removedItem = em.merge(item);
        em.remove(removedItem);

        if (em.getFlushMode() == FlushModeType.COMMIT && transaction != null) {
            em.flush();
            transaction.commit();
        }
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    @Override
    public List<T> getAllItems() {
        try {
            List result = em.createQuery("SELECT object(o) FROM " + getTableName() + " as o").getResultList();
            return (List<T>) result;
        } catch (NoResultException ex) {
            return List.of();
        } catch (Exception ex) {
            Logger.getLogger(XBEDefaultManager.class.getName()).log(Level.SEVERE, null, ex);
            return List.of();
        }
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    @Override
    public Optional<T> getItem(long itemId) {
        Object result = em.find(getGenericClass(), itemId);
        return Optional.ofNullable((T) result);
        /*        try {
         return (T) em.createQuery("SELECT object(o) FROM XBItem as o WHERE o.id = "+ itemId).getSingleResult();
         } catch (NoResultException ex) {
         return null;
         } catch (Exception ex) {
         Logger.getLogger(XBEDefaultCatalogManager.class.getName()).log(Level.SEVERE, null, ex);
         return null;
         } */
    }

    @Override
    public long getItemsCount() {
        try {
            return (Long) em.createQuery("SELECT count(o) FROM " + getTableName() + " as o").getSingleResult();
        } catch (NoResultException ex) {
            return 0;
        } catch (Exception ex) {
            Logger.getLogger(XBEDefaultManager.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public void persistItem(T item) {
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
        } catch (IllegalStateException ex) {
        }

        em.persist(item); // was merge

        if (em.getFlushMode() == FlushModeType.COMMIT && transaction != null) {
            em.flush();
            transaction.commit();
        }
    }

    /**
     * Gets class on which is this class parametrized / generic of.
     *
     * @return generic class of this class
     */
    @Nonnull
    public Class getGenericClass() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class) parameterizedType.getActualTypeArguments()[0];
    }

    /**
     * Gets table name for this manager.
     *
     * @return table name
     */
    @Nonnull
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
