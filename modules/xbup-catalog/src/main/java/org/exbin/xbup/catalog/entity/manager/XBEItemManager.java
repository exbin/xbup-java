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

import org.exbin.xbup.core.catalog.base.service.XBItemWithDetail;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.Query;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEItem;
import org.exbin.xbup.catalog.entity.XBEXDesc;
import org.exbin.xbup.catalog.entity.XBEXName;
import org.exbin.xbup.catalog.entity.XBEXStri;
import org.exbin.xbup.core.catalog.base.manager.XBCItemManager;
import org.exbin.xbup.core.catalog.base.manager.XBCXLangManager;
import org.springframework.stereotype.Repository;

/**
 * XBUP catalog item manager.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Repository
public class XBEItemManager extends XBEDefaultCatalogManager<XBEItem> implements XBCItemManager<XBEItem>, Serializable {

    public XBEItemManager() {
        super();
    }

    public XBEItemManager(XBECatalog catalog) {
        super(catalog);
    }

    @Nonnull
    @Override
    public List<XBItemWithDetail> findAllPaged(int startFrom, int maxResults, @Nullable String filterCondition, @Nullable String orderCondition, @Nullable String specType) {
        XBCXLangManager langManager = catalog.getCatalogManager(XBCXLangManager.class);
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
            results.add(itemRecord);
        }

        return results;
    }

    @Override
    public int findAllPagedCount(@Nullable String filterCondition, @Nullable String specType) {
        XBCXLangManager langManager = catalog.getCatalogManager(XBCXLangManager.class);
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
