/*
 * Copyright (C) ExBin Project
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
package org.exbin.xbup.web.xbcatalogweb.manager;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.exbin.xbup.catalog.entity.XBEXUser;
import org.exbin.xbup.catalog.entity.XBEXUserInfo;
import org.exbin.xbup.catalog.entity.manager.XBEXLangManager;
import org.exbin.xbup.catalog.entity.manager.XBEXUserManager;
import org.exbin.xbup.web.xbcatalogweb.base.XBCUserRecord;
import org.exbin.xbup.web.xbcatalogweb.base.manager.XBCUserRecordManager;
import org.exbin.xbup.web.xbcatalogweb.entity.XBEUserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * XBUP catalog XBEUserRecord manager.
 *
 * @version 0.1.23 2014/05/29
 * @author ExBin Project (http://exbin.org)
 */
@Repository
public class XBEUserRecordManager implements XBCUserRecordManager {

    @PersistenceContext
    protected EntityManager em;
    @Autowired
    private XBEXLangManager langManager;
    @Autowired
    private XBEXUserManager userManager;

    public XBEUserRecordManager() {
        super();
    }

    @Override
    public XBEUserRecord createItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Transactional
    @Override
    public void persistItem(XBCUserRecord item) {
        userManager.persistItem(item.getUser());
    }

    @Transactional
    @Override
    public void persistUser(XBEXUser user) {
        userManager.persistItem(user);
    }
    
    @Transactional
    @Override
    public void removeItem(XBCUserRecord userRecord) {
        userManager.removeItem(userRecord.getUser());
    }

    @Override
    public XBEUserRecord getItem(long itemId) {
        String queryString = "SELECT user, info FROM XBXUser user"
                + " LEFT JOIN XBXUserInfo info ON info.user = user"
                + " WHERE user.id = " + itemId;
        Query query = em.createQuery(queryString);
        Object row = query.getSingleResult();
        XBEUserRecord itemRecord = new XBEUserRecord();
        itemRecord.setUser((XBEXUser) ((Object[]) row)[0]);
        itemRecord.setInfo((XBEXUserInfo) ((Object[]) row)[1]);

        return itemRecord;
    }

    @Override
    public List<XBCUserRecord> getAllItems() {
        String queryString = "SELECT user, info FROM XBXUser user"
                + " LEFT JOIN XBXUserInfo info ON info.user = user"
                + " ORDER BY user.id";
        Query query = em.createQuery(queryString);
        List<XBCUserRecord> results = new ArrayList<XBCUserRecord>();
        for (Object row : query.getResultList()) {
            XBEUserRecord itemRecord = new XBEUserRecord();
            itemRecord.setUser((XBEXUser) ((Object[]) row)[0]);
            itemRecord.setInfo((XBEXUserInfo) ((Object[]) row)[1]);
            results.add(itemRecord);
        }

        return results;
    }

    @Override
    public long getItemsCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int findAllPagedCount(String filterCondition) {
        long languageId = langManager.getDefaultLang().getId();
        String filterPrefix = "";
        String filterPostfix = "";
        if (filterCondition != null) {
            if (filterCondition.contains("info.")) {
                filterPrefix += "XBXUserInfo info";
                filterPostfix += " AND info.user = user";
            }
            if (!filterPrefix.isEmpty()) {
                filterPrefix = "EXISTS(SELECT 1 FROM " + filterPrefix
                        + " WHERE ";
                filterPostfix += ")";
            }
        }

        Query query = em.createQuery(
                "SELECT COUNT(user) from XBXUser user"
                + (filterCondition == null
                || filterCondition.isEmpty() ? ""
                : " WHERE ")
                + filterPrefix
                + filterCondition
                + filterPostfix
        );
        return ((Long) query.getSingleResult()).intValue();
    }

    @Override
    public List<XBCUserRecord> findAllPaged(int startFrom, int maxResults, String filterCondition, String orderCondition) {
        long languageId = langManager.getDefaultLang().getId();
        if (orderCondition == null) {
            orderCondition = "item.id";
        }

        Query query = em.createQuery(
                "SELECT user, info FROM XBXUser user"
                + " LEFT JOIN XBXUserInfo info ON info.user = user"
                + (filterCondition == null
                || filterCondition.isEmpty() ? ""
                : " WHERE "
                + filterCondition)
                + " ORDER BY " + orderCondition);
        query.setFirstResult(startFrom);
        query.setMaxResults(maxResults);

        List<XBCUserRecord> results = new ArrayList<XBCUserRecord>();
        for (Object row : query.getResultList()) {
            XBEUserRecord itemRecord = new XBEUserRecord();
            itemRecord.setUser((XBEXUser) ((Object[]) row)[0]);
            itemRecord.setInfo((XBEXUserInfo) ((Object[]) row)[1]);
            results.add(itemRecord);
        }

        return results;
    }
}
