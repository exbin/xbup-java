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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.xbup.lib.core.catalog.base.manager.XBCItemManager;
import org.xbup.lib.catalog.XBECatalog;
import org.xbup.lib.catalog.entity.XBEItem;
import org.xbup.lib.catalog.entity.XBEXDesc;
import org.xbup.lib.catalog.entity.XBEXName;
import org.xbup.lib.catalog.entity.XBEXStri;
import org.xbup.lib.core.catalog.base.manager.XBCXLangManager;

/**
 * XBUP catalog item manager.
 *
 * @version 0.1.24 2014/12/12
 * @author XBUP Project (http://xbup.org)
 */
@Repository
public class XBEItemManager extends XBEDefaultCatalogManager<XBEItem> implements XBCItemManager<XBEItem>, Serializable {

    public XBEItemManager() {
        super();
    }

    public XBEItemManager(XBECatalog catalog) {
        super(catalog);
    }

    public List<XBItemWithDetail> findAllPaged(int startFrom, int maxResults, String filterCondition, String orderCondition, String specType) {
        XBEXLangManager langManager = ((XBEXLangManager) catalog.getCatalogManager(XBCXLangManager.class));
        long languageId = langManager.getDefaultLang().getId();
        if (orderCondition == null) {
            orderCondition = "item.id";
        }

        Query query = em.createQuery(
                "SELECT item, name, dsc, stri FROM " + (specType == null ? "XBItem" : specType) + " item"
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

        List<XBItemWithDetail> results = new ArrayList<>();
        for (Object row : query.getResultList()) {
            XBItemWithDetail itemRecord = new XBItemWithDetail();
            itemRecord.setItem((XBEItem) ((Object[]) row)[0]);
            XBEXName name = ((XBEXName) ((Object[]) row)[1]);
            itemRecord.setName(name == null ? null : name.getText());
            XBEXDesc desc = (XBEXDesc) ((Object[]) row)[2];
            itemRecord.setDescription(desc == null ? null : desc.getText());
            XBEXStri stri = (XBEXStri) ((Object[]) row)[3];
            itemRecord.setStringId(stri == null ? null : stri.getText());
            itemRecord.setTypeFromItem();
            results.add(itemRecord);
        }

        return results;
    }

    public int findAllPagedCount(String filterCondition, String specType) {
        XBEXLangManager langManager = ((XBEXLangManager) catalog.getCatalogManager(XBCXLangManager.class));
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
                "SELECT COUNT(item) FROM " + (specType == null ? "XBItem" : specType) + " item"
                + (filterCondition == null
                || filterCondition.isEmpty() ? ""
                        : " WHERE ")
                + filterPrefix
                + filterCondition
                + filterPostfix
        );
        return ((Long) query.getSingleResult()).intValue();
    }
}
