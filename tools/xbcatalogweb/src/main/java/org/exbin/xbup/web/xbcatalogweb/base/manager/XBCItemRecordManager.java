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
package org.exbin.xbup.web.xbcatalogweb.base.manager;

import java.io.Serializable;
import java.util.List;
import org.exbin.xbup.catalog.entity.XBEXHDoc;
import org.exbin.xbup.core.catalog.base.manager.XBCManager;
import org.exbin.xbup.web.xbcatalogweb.base.XBCFullItemRecord;
import org.exbin.xbup.web.xbcatalogweb.base.XBCItemRecord;
import org.exbin.xbup.web.xbcatalogweb.entity.XBEItemRecord;

/**
 * XBUP catalog XBEItemRecord manager.
 *
 * @version 0.1.24 2015/01/16
 * @author ExBin Project (http://exbin.org)
 */
public interface XBCItemRecordManager extends XBCManager<XBCItemRecord>, Serializable {

    List<XBCItemRecord> findAllByParent(Long selectedPackageId);

    List<XBCItemRecord> findAllPaged(int startFrom, int maxResults, String filterCondition, String orderCondition);

    int findAllPagedCount(String filterCondition);

    XBCFullItemRecord findForEditById(Long selectedItemId);

    public XBEXHDoc getItemDoc(XBEItemRecord selectedItem);

    public XBCFullItemRecord createForEdit();

    public XBCFullItemRecord findNodeByPath(Long[] path);

    public XBCFullItemRecord findBlockSpecByPath(Long[] path);

    public XBCFullItemRecord findGroupSpecByPath(Long[] path);

    public XBCFullItemRecord findFormatSpecByPath(Long[] path);
}
