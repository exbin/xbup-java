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
package org.xbup.lib.core.catalog.declaration;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.block.XBTBlock;
import org.xbup.lib.core.block.XBTEditableBlock;
import org.xbup.lib.core.block.declaration.XBParamDecl;
import org.xbup.lib.core.block.definition.XBBlockDef;
import org.xbup.lib.core.block.definition.XBRevisionDef;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCRev;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.XBCSpecDef;
import org.xbup.lib.core.catalog.base.service.XBCRevService;
import org.xbup.lib.core.catalog.base.service.XBCSpecService;
import org.xbup.lib.core.serial.XBSerializable;

/**
 * XBUP level 1 block definition based on catalog specification.
 *
 * @version 0.1.21 2011/12/31
 * @author XBUP Project (http://xbup.org)
 */
public class XBCBlockDef implements XBBlockDef, XBSerializable {

    private XBCBlockSpec blockSpec;
    private XBACatalog catalog;

    public XBCBlockDef(XBACatalog catalog, XBCBlockSpec blockSpec) {
        this.catalog = catalog;
        this.blockSpec = blockSpec;
    }

    /**
     * @return the revisionDefs
     */
    @Override
    public List<XBRevisionDef> getRevisionDefs() {
        XBCRevService<?> revService = (XBCRevService<?>) catalog.getCatalogService(XBCRevService.class);
        List<XBCRev> revList = revService.getRevs(blockSpec);
        return new XBRevisionList(revList);
    }

    @Override
    public XBParamDecl getParamDecl(int index) {
        XBCSpecService bindService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        XBCSpecDef bind = bindService.getSpecDefByOrder(blockSpec, index);
        if (bind == null) {
            return null;
        }
        return new XBCParamDecl(catalog, new XBCSBlockDecl(bind.getTarget()));
    }

    @Override
    public List<XBParamDecl> getParamDecls() {
        XBCSpecService<?> bindService = (XBCSpecService<?>) catalog.getCatalogService(XBCSpecService.class);
        List<XBCSpecDef> binds = bindService.getSpecDefs((XBCSpec) blockSpec);
        List<XBParamDecl> result = new ArrayList<XBParamDecl>();
        for (int i = 0; i < binds.size(); i++) {
            XBCSpecDef bind = binds.get(i);
            result.add(new XBCParamDecl(catalog, new XBCSBlockDecl(bind.getTarget())));
        }
        return result;
    }

    @Override
    public long getParamCount() {
        XBCSpecService bindService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        return bindService.getSpecDefsCount(blockSpec);
    }

    @Override
    public XBTBlock getParameter(XBTBlock block, int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setParameter(XBTEditableBlock block, int index, XBTBlock parameter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getParametersCount(XBTBlock block) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return the blockSpec
     */
    public XBCBlockSpec getBlockSpec() {
        return blockSpec;
    }

    /**
     * @return the catalog
     */
    public XBACatalog getCatalog() {
        return catalog;
    }

    private class XBRevisionList extends AbstractList<XBRevisionDef> {

        private final List<XBCRev> revList;

        private XBRevisionList(List<XBCRev> revList) {
            this.revList = revList;
        }

        @Override
        public XBRevisionDef get(int index) {
            return new XBRevisionDef(revList.get(index).getXBLimit());
        }

        @Override
        public int size() {
            return revList.size();
        }
    };
}
