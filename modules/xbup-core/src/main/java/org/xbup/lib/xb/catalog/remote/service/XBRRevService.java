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
package org.xbup.lib.xb.catalog.remote.service;

import java.util.List;
import org.xbup.lib.xb.catalog.XBRCatalog;
import org.xbup.lib.xb.catalog.base.XBCRev;
import org.xbup.lib.xb.catalog.base.XBCSpec;
import org.xbup.lib.xb.catalog.base.manager.XBCRevManager;
import org.xbup.lib.xb.catalog.base.service.XBCRevService;
import org.xbup.lib.xb.catalog.remote.XBRRev;
import org.xbup.lib.xb.catalog.remote.manager.XBRRevManager;

/**
 * Interface for XBRRev items service.
 *
 * @version 0.1 wr21.0 2012/01/01
 * @author XBUP Project (http://xbup.org)
 */
public class XBRRevService extends XBRDefaultService<XBRRev> implements XBCRevService<XBRRev> {

    public XBRRevService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRRevManager(catalog);
        catalog.addCatalogManager(XBCRevManager.class, itemManager);
    }

    @Override
    public XBRRev findRevById(long id) {
        return ((XBRRevManager)itemManager).findRevById(id);
    }

    @Override
    public XBRRev findRevByXB(XBCSpec spec, long xbIndex) {
        return ((XBRRevManager)itemManager).findRevByXB(spec, xbIndex);
    }

    @Override
    public XBRRev getRev(XBCSpec spec, long index) {
        return ((XBRRevManager)itemManager).getRev(spec, index);
    }

    @Override
    public List<XBCRev> getRevs(XBCSpec spec) {
        return ((XBRRevManager)itemManager).getRevs(spec);
    }

    @Override
    public Long getAllRevisionsCount() {
        return ((XBRRevManager)itemManager).getAllRevisionsCount();
    }

    @Override
    public long getRevsCount(XBCSpec spec) {
        return ((XBRRevManager)itemManager).getRevsCount(spec);
    }

}
