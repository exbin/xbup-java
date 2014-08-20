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

import org.xbup.web.xbcatalogweb.base.manager.XBCItemRecordManager;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.xbup.lib.catalog.entity.XBEItem;
import org.xbup.lib.catalog.entity.XBEXDesc;
import org.xbup.lib.catalog.entity.XBEXFile;
import org.xbup.lib.catalog.entity.XBEXHDoc;
import org.xbup.lib.catalog.entity.XBEXName;
import org.xbup.lib.catalog.entity.XBEXStri;
import org.xbup.lib.catalog.entity.manager.XBEItemManager;
import org.xbup.lib.catalog.entity.manager.XBEXDescManager;
import org.xbup.lib.catalog.entity.manager.XBEXFileManager;
import org.xbup.lib.catalog.entity.manager.XBEXHDocManager;
import org.xbup.lib.catalog.entity.manager.XBEXLangManager;
import org.xbup.lib.catalog.entity.manager.XBEXNameManager;
import org.xbup.lib.catalog.entity.manager.XBEXStriManager;
import org.xbup.web.xbcatalogweb.base.XBCFullItemRecord;
import org.xbup.web.xbcatalogweb.base.XBCItemRecord;
import org.xbup.web.xbcatalogweb.entity.XBEFullItemRecord;
import org.xbup.web.xbcatalogweb.entity.XBEItemRecord;

/**
 * XBUP catalog XBEItemRecord manager.
 *
 * @version 0.1 wr24.0 2014/08/20
 * @author XBUP Project (http://xbup.org)
 */
@Repository
public class XBEItemRecordManager implements XBCItemRecordManager {

    @PersistenceContext
    protected EntityManager em;
    @Autowired
    private XBEXLangManager langManager;
    @Autowired
    private XBEItemManager itemManager;
    @Autowired
    private XBEXStriManager striManager;
    @Autowired
    private XBEXNameManager nameManager;
    @Autowired
    private XBEXDescManager descManager;
    @Autowired
    private XBEXFileManager fileManager;
    @Autowired
    private XBEXHDocManager hdocManager;

    public XBEItemRecordManager() {
        super();
    }

    @Override
    public XBCItemRecord createItem() {
        return new XBEItemRecord();
    }

    @Transactional
    @Override
    public void persistItem(XBCItemRecord item) {
        itemManager.persistItem(item.getItem());

        if (item.getStri().getText() == null || item.getStri().getText().isEmpty()) {
            if (item.getStri().getId() == null) {
                striManager.removeItem(item.getStri());
            }
        } else {
            striManager.persistItem(item.getStri());
        }

        if (item.getName().getText() == null || item.getName().getText().isEmpty()) {
            if (item.getName().getId() == null) {
                nameManager.removeItem(item.getName());
            }
        } else {
            nameManager.persistItem(item.getName());
        }

        if (item.getDesc().getText() == null || item.getDesc().getText().isEmpty()) {
            if (item.getDesc().getId() == null) {
                descManager.removeItem(item.getDesc());
            }
        } else {
            descManager.persistItem(item.getDesc());
        }

        if (item instanceof XBEFullItemRecord) {
            if (item.getDesc().getText() == null || item.getDesc().getText().isEmpty()) {
                if (((XBEFullItemRecord) item).getHdoc().getId() == null) {
                    if (((XBEFullItemRecord) item).getHdoc().getDocFile() != null && ((XBEFullItemRecord) item).getHdoc().getDocFile().getId() == null) {
                        fileManager.removeItem(((XBEFullItemRecord) item).getHdoc().getDocFile());
                    }

                    hdocManager.removeItem(((XBEFullItemRecord) item).getHdoc());
                }
            } else {
                fileManager.persistItem(((XBEFullItemRecord) item).getHdoc().getDocFile());
                hdocManager.persistItem(((XBEFullItemRecord) item).getHdoc());
            }
        }
    }

    @Transactional
    @Override
    public void removeItem(XBCItemRecord item) {
        if (item.getStri().getId() != null) {
            striManager.removeItem(item.getStri());
        }

        if (item.getName().getId() == null) {
            nameManager.removeItem(item.getName());
        }

        if (item.getDesc().getId() == null) {
            descManager.removeItem(item.getDesc());
        }

        if (((XBEFullItemRecord) item).getHdoc().getId() == null) {
            if (((XBEFullItemRecord) item).getHdoc().getDocFile() != null && ((XBEFullItemRecord) item).getHdoc().getDocFile().getId() == null) {
                fileManager.removeItem(((XBEFullItemRecord) item).getHdoc().getDocFile());
            }

            hdocManager.removeItem(((XBEFullItemRecord) item).getHdoc());
        }
        
        // TODO definition and other extensions
        
        itemManager.removeItem(item.getItem());
    }

    @Override
    public XBEItemRecord getItem(long itemId) {
        long languageId = langManager.getDefaultLang().getId();
        String queryString = "SELECT item, name, dsc, stri FROM XBItem item"
                + " LEFT JOIN XBXName name ON name.item = item AND name.lang.id = " + languageId
                + " LEFT JOIN XBXDesc dsc ON dsc.item = item AND name.lang.id = " + languageId
                + " LEFT JOIN XBXStri stri ON stri.item = item WHERE item.id = " + itemId;
        Query query = em.createQuery(queryString);
        Object row = query.getSingleResult();
        XBEItemRecord itemRecord = new XBEItemRecord();
        itemRecord.setItem((XBEItem) ((Object[]) row)[0]);
        itemRecord.setName((XBEXName) ((Object[]) row)[1]);
        itemRecord.setDesc((XBEXDesc) ((Object[]) row)[2]);
        itemRecord.setStri((XBEXStri) ((Object[]) row)[3]);

        return itemRecord;
    }

    @Override
    public List<XBCItemRecord> getAllItems() {
        long languageId = langManager.getDefaultLang().getId();
        String queryString = "SELECT item, name, dsc, stri FROM XBItem item"
                + " LEFT JOIN XBXName name ON name.item = item AND name.lang.id = " + languageId
                + " LEFT JOIN XBXDesc dsc ON dsc.item = item AND name.lang.id = " + languageId
                + " LEFT JOIN XBXStri stri ON stri.item = item ORDER BY item.id";
        Query query = em.createQuery(queryString);
        List<XBCItemRecord> results = new ArrayList<XBCItemRecord>();
        for (Object row : query.getResultList()) {
            XBEItemRecord itemRecord = new XBEItemRecord();
            itemRecord.setItem((XBEItem) ((Object[]) row)[0]);
            itemRecord.setName((XBEXName) ((Object[]) row)[1]);
            itemRecord.setDesc((XBEXDesc) ((Object[]) row)[2]);
            itemRecord.setStri((XBEXStri) ((Object[]) row)[3]);
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
            if (filterCondition.contains("name.")) {
                filterPrefix += "XBXName name";
                filterPostfix += " AND name.item = item AND name.lang.id = " + languageId;
            }
            if (filterCondition.contains("desc.")) {
                if (!filterPrefix.isEmpty()) {
                    filterPrefix += ", ";
                }
                filterPrefix += "XBXDesc dsc";
                filterPostfix += " AND dsc.item = item AND dsc.lang.id = " + languageId;
            }
            if (filterCondition.contains("stri.")) {
                if (!filterPrefix.isEmpty()) {
                    filterPrefix += ", ";
                }
                filterPrefix += "XBXStri stri";
                filterPostfix += " AND stri.item = item";
            }
            if (!filterPrefix.isEmpty()) {
                filterPrefix = "EXISTS(SELECT 1 FROM " + filterPrefix
                        + " WHERE ";
                filterPostfix += ")";
            }
        }

        Query query = em.createQuery(
                "SELECT COUNT(item) from XBItem item"
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
    public List<XBCItemRecord> findAllPaged(int startFrom, int maxResults, String filterCondition, String orderCondition) {
        long languageId = langManager.getDefaultLang().getId();
        if (orderCondition == null) {
            orderCondition = "item.id";
        }

        Query query = em.createQuery(
                "SELECT item, name, dsc, stri FROM XBItem item"
                + " LEFT JOIN XBXName name ON name.item = item AND name.lang.id = " + languageId
                + " LEFT JOIN XBXDesc dsc ON dsc.item = item AND name.lang.id = " + languageId
                + " LEFT JOIN XBXStri stri ON stri.item = item"
                + (filterCondition == null
                || filterCondition.isEmpty() ? ""
                : " WHERE "
                + filterCondition)
                + " ORDER BY " + orderCondition);
        query.setFirstResult(startFrom);
        query.setMaxResults(maxResults);

        List<XBCItemRecord> results = new ArrayList<XBCItemRecord>();
        for (Object row : query.getResultList()) {
            XBEItemRecord itemRecord = new XBEItemRecord();
            itemRecord.setItem((XBEItem) ((Object[]) row)[0]);
            itemRecord.setName((XBEXName) ((Object[]) row)[1]);
            itemRecord.setDesc((XBEXDesc) ((Object[]) row)[2]);
            itemRecord.setStri((XBEXStri) ((Object[]) row)[3]);
            results.add(itemRecord);
        }

        return results;
    }

    @Override
    public XBEFullItemRecord findForEditById(Long selectedItemId) {
        long languageId = langManager.getDefaultLang().getId();
        String queryString = "SELECT item, name, dsc, stri, hdoc FROM XBItem item"
                + " LEFT JOIN XBXName name ON name.item = item AND name.lang.id = " + languageId
                + " LEFT JOIN XBXDesc dsc ON dsc.item = item AND name.lang.id = " + languageId
                + " LEFT JOIN XBXStri stri ON stri.item = item"
                + " LEFT JOIN XBXHDoc hdoc ON hdoc.item = item AND hdoc.lang.id = " + languageId
                + " LEFT JOIN FETCH hdoc.docFile hfil"
                + " WHERE item.id = " + selectedItemId;
        Query query = em.createQuery(queryString);
        Object[] row = (Object[]) query.getSingleResult();
        XBEFullItemRecord itemRecord = new XBEFullItemRecord();
        itemRecord.setLanguage(langManager.getDefaultLang());
        itemRecord.setItem((XBEItem) ((Object[]) row)[0]);
        itemRecord.setName((XBEXName) ((Object[]) row)[1]);
        itemRecord.setDesc((XBEXDesc) ((Object[]) row)[2]);
        itemRecord.setStri((XBEXStri) ((Object[]) row)[3]);
        itemRecord.setHdoc((XBEXHDoc) ((Object[]) row)[4]);

        if (itemRecord.getName() == null) {
            XBEXName name = new XBEXName();
            name.setItem(itemRecord.getItem());
            name.setLang(itemRecord.getLanguage());
            itemRecord.setName(name);
        }

        if (itemRecord.getDesc() == null) {
            XBEXDesc desc = new XBEXDesc();
            desc.setItem(itemRecord.getItem());
            desc.setLang(itemRecord.getLanguage());
            itemRecord.setDesc(desc);
        }

        if (itemRecord.getStri() == null) {
            XBEXStri stri = new XBEXStri();
            stri.setItem(itemRecord.getItem());
            itemRecord.setStri(stri);
        }

        if (itemRecord.getHdoc() == null) {
            XBEXHDoc hdoc = new XBEXHDoc();
            XBEXFile file = new XBEXFile();
            hdoc.setItem(itemRecord.getItem());
            hdoc.setLang(itemRecord.getLanguage());
            hdoc.setDocFile(file);
            itemRecord.setHdoc(hdoc);
        } else {
            if (itemRecord.getHdoc().getDocFile() == null) {
                XBEXFile file = new XBEXFile();
                itemRecord.getHdoc().setDocFile(file);
            }
        }

        return itemRecord;
    }

    public XBCFullItemRecord createForEdit() {
        XBEFullItemRecord itemRecord = new XBEFullItemRecord();
        itemRecord.setLanguage(langManager.getDefaultLang());
        XBEItem item = new XBEItem();
        itemRecord.setItem(item);
        XBEXName name = new XBEXName();
        name.setItem(item);
        itemRecord.setName(name);
        XBEXDesc desc = new XBEXDesc();
        desc.setItem(item);
        itemRecord.setDesc(desc);
        XBEXStri stri = new XBEXStri();
        stri.setItem(item);
        itemRecord.setStri(stri);
        XBEXHDoc hdoc = new XBEXHDoc();
        hdoc.setItem(item);
        hdoc.setLang(langManager.getDefaultLang());
        hdoc.setDocFile(new XBEXFile());
        itemRecord.setHdoc(hdoc);
        
        return itemRecord;
    }
    
    @Override
    public List<XBCItemRecord> findAllByParent(Long selectedPackageId) {
        long languageId = langManager.getDefaultLang().getId();
        String queryString = "SELECT item, name, dsc, stri FROM XBItem item"
                + " LEFT JOIN XBXName name ON name.item = item AND name.lang.id = " + languageId
                + " LEFT JOIN XBXDesc dsc ON dsc.item = item AND name.lang.id = " + languageId
                + " LEFT JOIN XBXStri stri ON stri.item = item"
                + (selectedPackageId == null ? "" : " WHERE item.parent.id = " + selectedPackageId)
                + " ORDER BY item.id";
        Query query = em.createQuery(queryString);
        List<XBCItemRecord> results = new ArrayList<XBCItemRecord>();
        for (Object row : query.getResultList()) {
            XBEItemRecord itemRecord = new XBEItemRecord();
            itemRecord.setItem((XBEItem) ((Object[]) row)[0]);
            itemRecord.setName((XBEXName) ((Object[]) row)[1]);
            itemRecord.setDesc((XBEXDesc) ((Object[]) row)[2]);
            itemRecord.setStri((XBEXStri) ((Object[]) row)[3]);
            results.add(itemRecord);
        }

        return results;
    }

    @Override
    public XBEXHDoc getItemDoc(XBEItemRecord selectedItem) {
        long languageId = langManager.getDefaultLang().getId();
        String queryString = "SELECT hdoc FROM XBXHDoc hdoc"
                + " LEFT JOIN FETCH hdoc.docFile hfil"
                + " WHERE hdoc.item.id = " + selectedItem.getId() + " AND hdoc.lang.id = " + languageId;
        Query query = em.createQuery(queryString);
        List<Object> results = query.getResultList();
        if (results.size() > 0) {
            return (XBEXHDoc) results.get(0);
        }
        
        return null;
    }
}
