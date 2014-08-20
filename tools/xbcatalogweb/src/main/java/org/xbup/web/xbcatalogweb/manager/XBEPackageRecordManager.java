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
package org.xbup.web.xbcatalogweb.manager;

import org.xbup.web.xbcatalogweb.base.manager.XBCPackageRecordManager;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xbup.lib.catalog.entity.XBENode;
import org.xbup.lib.catalog.entity.XBEXName;
import org.xbup.lib.catalog.entity.XBEXStri;
import org.xbup.lib.catalog.entity.manager.XBENodeManager;
import org.xbup.lib.catalog.entity.manager.XBEXLangManager;
import org.xbup.web.xbcatalogweb.base.XBCPackageRecord;
import org.xbup.web.xbcatalogweb.entity.XBEPackageRecord;

/**
 * XBUP catalog XBEPackageRecord manager.
 *
 * @version 0.1 wr23.0 2014/05/23
 * @author XBUP Project (http://xbup.org)
 */
@Repository
public class XBEPackageRecordManager implements XBCPackageRecordManager {

    @PersistenceContext
    protected EntityManager em;
    @Autowired
    private XBEXLangManager langManager;
    @Autowired
    private XBENodeManager nodeManager;

    public XBEPackageRecordManager() {
        super();
    }

    @Override
    public XBCPackageRecord createItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persistItem(XBCPackageRecord t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeItem(XBCPackageRecord t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBEPackageRecord getItem(long itemId) {
        long languageId = langManager.getDefaultLang().getId();
        String queryString = "SELECT node, name, stri FROM XBNode node"
                + " LEFT JOIN XBXName name ON name.item = node AND name.lang.id = " + languageId
                + " LEFT JOIN XBXStri stri ON stri.item = node WHERE node.id = " + itemId;
        Query query = em.createQuery(queryString);
        Object row = query.getSingleResult();
        XBEPackageRecord packageRecord = new XBEPackageRecord();
        packageRecord.setNode((XBENode) ((Object[]) row)[0]);
        packageRecord.setName((XBEXName) ((Object[]) row)[1]);
        packageRecord.setStri((XBEXStri) ((Object[]) row)[2]);

        return packageRecord;
    }

    @Override
    public List<XBCPackageRecord> getAllItems() {
        long languageId = langManager.getDefaultLang().getId();
        String queryString = "SELECT node, name, stri FROM XBNode node"
                + " LEFT JOIN XBXName name ON name.item = node AND name.lang.id = " + languageId
                + " LEFT JOIN XBXStri stri ON stri.item = node ORDER BY node.id";
        Query query = em.createQuery(queryString);
        List<XBCPackageRecord> results = new ArrayList<XBCPackageRecord>();
        for (Object row : query.getResultList()) {
            XBEPackageRecord packageRecord = new XBEPackageRecord();
            packageRecord.setNode((XBENode) ((Object[]) row)[0]);
            packageRecord.setName((XBEXName) ((Object[]) row)[1]);
            packageRecord.setStri((XBEXStri) ((Object[]) row)[2]);
            results.add(packageRecord);
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
            if (filterCondition.contains("name.")) {
                filterPrefix += "XBXName name";
                filterPostfix += " AND name.item = node AND name.lang.id = " + languageId;
            }
            if (filterCondition.contains("stri.")) {
                if (!filterPrefix.isEmpty()) {
                    filterPrefix += ", ";
                }
                filterPrefix += "XBXStri stri";
                filterPostfix += " AND stri.item = node";
            }
            if (!filterPrefix.isEmpty()) {
                filterPrefix = "EXISTS(SELECT 1 FROM " + filterPrefix
                        + " WHERE ";
                filterPostfix += ")";
            }
        }

        Query query = em.createQuery(
                "SELECT COUNT(node) from XBNode node"
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
    public List<XBCPackageRecord> findAllPaged(int startFrom, int maxResults, String filterCondition, String orderCondition) {
        long languageId = langManager.getDefaultLang().getId();
        if (orderCondition == null) {
            orderCondition = "node.id";
        }

        Query query = em.createQuery(
                "SELECT node, name, stri FROM XBNode node"
                + " LEFT JOIN XBXName name ON name.item = node AND name.lang.id = " + languageId
                + " LEFT JOIN XBXStri stri ON stri.item = node"
                + (filterCondition == null
                || filterCondition.isEmpty() ? ""
                : " WHERE "
                + filterCondition)
                + " ORDER BY " + orderCondition);
        query.setFirstResult(startFrom);
        query.setMaxResults(maxResults);

        List<XBCPackageRecord> results = new ArrayList<XBCPackageRecord>();
        for (Object row : query.getResultList()) {
            XBEPackageRecord packageRecord = new XBEPackageRecord();
            packageRecord.setNode((XBENode) ((Object[]) row)[0]);
            packageRecord.setName((XBEXName) ((Object[]) row)[1]);
            packageRecord.setStri((XBEXStri) ((Object[]) row)[2]);
            results.add(packageRecord);
        }

        return results;
    }

    @Override
    public List<XBCPackageRecord> findFullTree() {
        List<XBCPackageRecord> resultList = new ArrayList<XBCPackageRecord>();
        List<List<XBCPackageRecord>> parentLists = new ArrayList<List<XBCPackageRecord>>();
        List<XBCPackageRecord> rootList = new LinkedList<XBCPackageRecord>();
        XBEPackageRecord root = getItem(nodeManager.getRootNode().getId());
        root.setHasChildren(true);

        rootList.add(root);
        parentLists.add(rootList);

        while (parentLists.size() > 0) {
            if (parentLists.get(parentLists.size() - 1).size() > 0) {
                XBEPackageRecord parentPackage = (XBEPackageRecord) parentLists.get(parentLists.size() - 1).get(0);
                resultList.add(parentPackage);
                parentLists.get(parentLists.size() - 1).remove(0);
                if (parentPackage.isHasChildren()) {
                    List<XBCPackageRecord> childPackages = childPackages(parentPackage, parentLists.size() > 1 ? parentPackage.getFullName() : "");
                    if (childPackages != null && childPackages.size() > 0) {
                        parentLists.add(childPackages);
                    }
                }
            } else {
                parentLists.remove(parentLists.size() - 1);
            }
        }

        return resultList;
    }

    @Override
    public List<XBCPackageRecord> childPackages(XBCPackageRecord parentPackage, String prefix) {
        long languageId = langManager.getDefaultLang().getId();

        Query query = em.createQuery(
                "SELECT node, name, stri, (CASE WHEN EXISTS(SELECT 1 FROM XBNode sub WHERE sub.parent = node) THEN 1 ELSE 0 END) FROM XBNode node"
                + " LEFT JOIN XBXName name ON name.item = node AND name.lang.id = " + languageId
                + " LEFT JOIN XBXStri stri ON stri.item = node"
                + " WHERE node.parent.id = " + parentPackage.getId()
                + " ORDER BY name.text, node.id");

        List<XBCPackageRecord> results = new ArrayList<XBCPackageRecord>();
        for (Object row : query.getResultList()) {
            XBEPackageRecord packageRecord = new XBEPackageRecord();
            packageRecord.setNode((XBENode) ((Object[]) row)[0]);
            packageRecord.setName((XBEXName) ((Object[]) row)[1]);
            packageRecord.setStri((XBEXStri) ((Object[]) row)[2]);
            packageRecord.setHasChildren((Integer) ((Object[]) row)[3] == 1);
            packageRecord.setPrefix(prefix);
            results.add(packageRecord);
        }

        return results;
    }
}
