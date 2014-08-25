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

import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.declaration.XBContext;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCFormatSpec;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;
import org.xbup.lib.core.catalog.base.XBCRev;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.XBCSpecDef;
import org.xbup.lib.core.catalog.base.service.XBCSpecService;

/**
 * Context based on path declaration.
 *
 * @version 0.1.21 2011/12/31
 * @author XBUP Project (http://xbup.org)
 */
public class XBCPContext extends XBContext {

    private XBCatalog catalog;
    private XBCFormatSpec spec;

    /**
     * Creates a new instance of XBCPContext
     */
    public XBCPContext(XBCatalog catalog, XBCFormatSpec spec) {
        super(null);
        this.catalog = catalog;
        this.spec = spec;
    }

    public long getGroupsCount() {
        XBCSpecService bindService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        Long count = bindService.getSpecDefsCount(spec); //findMaxBindXB
        if (count==0) {
            return 0;
        }
        return count.longValue();
    }

//    @Override
    public long getBlocksCount(long group) {
        XBCSpecService bindService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        XBCSpecDef bind = bindService.getSpecDefByOrder(spec,group);
        if (bind==null) {
            return 0;
        }
        XBCRev rev = (XBCRev) bind.getTarget();
        if (rev == null) {
            return 0;
        }
        XBCGroupSpec block = (XBCGroupSpec) rev.getParent();
        if (block==null) {
            return 0;
        }
        Long result = bindService.getSpecDefsCount(block); //findMaxBindXB(block);
        if (result==null) {
            return 0;
        }
        return result.longValue();
    }

    @Override
    public XBBlockDecl getBlockType(int group, int block) {
        XBCSpecService bindService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        if (group == 0) {
            return super.getBlockType(group, block);
        }
        if (spec == null) {
            return null;
        }
        if ((spec.getXBIndex().intValue()>0)) { // Not root context
            if (group==0) {
                return catalog.getRootContext().getBlockType(group,block);
            }
            group--;
        } else if (spec.getParent() != null) {
            if (spec.getParent().getParent() != null) {
                if (group == 0) {
                    return catalog.getRootContext().getBlockType(group, block);
                }
                group--;
            }
        }
        XBCSpecDef formatBind = (XBCSpecDef) bindService.findSpecDefByXB(spec,group);
        if (formatBind == null) {
            return null;
        }
        XBCRev rev = (XBCRev) formatBind.getTarget();
        if (rev == null) {
            return null;
        }
        XBCSpec groupSpec = (XBCSpec) rev.getParent();
        if (groupSpec == null) {
            return null;
        }
        XBCSpecDef groupBind = (XBCSpecDef) bindService.findSpecDefByXB(groupSpec,block);
        if (groupBind == null) {
            return null;
        }
        rev = (XBCRev) groupBind.getTarget();
        if (rev == null) {
            return null;
        }
        XBCSpec blockSpec = (XBCSpec) rev.getParent();
        if (blockSpec == null) {
            return null;
        }
        return new XBCSBlockDecl((XBCBlockSpec) blockSpec);
    }
}
