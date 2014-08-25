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
package org.xbup.lib.catalog.entity.service;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xbup.lib.core.catalog.base.XBCRev;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.manager.XBCRevManager;
import org.xbup.lib.core.catalog.base.service.XBCRevService;
import org.xbup.lib.catalog.XBECatalog;
import org.xbup.lib.catalog.entity.XBERev;
import org.xbup.lib.catalog.entity.manager.XBERevManager;

/**
 * Interface for XBERev items service.
 *
 * @version 0.1.21 2012/01/01
 * @author XBUP Project (http://xbup.org)
 */
@Service
public class XBERevService extends XBEDefaultService<XBERev> implements XBCRevService<XBERev>, Serializable {

    @Autowired
    private XBERevManager manager;

    public XBERevService() {
        super();
    }

    public XBERevService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBERevManager(catalog);
        catalog.addCatalogManager(XBCRevManager.class, itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }

    @Override
    public XBERev findRevById(long id) {
        return ((XBERevManager)itemManager).findRevById(id);
    }

    @Override
    public XBERev findRevByXB(XBCSpec spec, long xbIndex) {
        return ((XBERevManager)itemManager).findRevByXB(spec, xbIndex);
    }

    @Override
    public XBERev getRev(XBCSpec spec, long index) {
        return ((XBERevManager)itemManager).getRev(spec, index);
    }

    @Override
    public List<XBCRev> getRevs(XBCSpec spec) {
        return ((XBERevManager)itemManager).getRevs(spec);
    }

    @Override
    public Long getAllRevisionsCount() {
        return ((XBERevManager)itemManager).getAllRevisionsCount();
    }

    @Override
    public long getRevsCount(XBCSpec spec) {
        return ((XBERevManager)itemManager).getRevsCount(spec);
    }
}
