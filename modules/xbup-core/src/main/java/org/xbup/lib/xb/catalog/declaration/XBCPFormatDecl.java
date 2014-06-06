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
package org.xbup.lib.xb.catalog.declaration;

import java.io.IOException;
import java.util.List;
import org.xbup.lib.xb.block.declaration.XBBlockDecl;
import org.xbup.lib.xb.block.declaration.XBFormatDecl;
import org.xbup.lib.xb.block.declaration.XBGroupDecl;
import org.xbup.lib.xb.catalog.XBCatalog;
import org.xbup.lib.xb.catalog.base.XBCBlockSpec;
import org.xbup.lib.xb.catalog.base.XBCFormatSpec;
import org.xbup.lib.xb.catalog.base.XBCSpec;
import org.xbup.lib.xb.catalog.base.XBCSpecDef;
import org.xbup.lib.xb.catalog.base.service.XBCNodeService;
import org.xbup.lib.xb.catalog.base.service.XBCSpecService;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.parser.basic.XBListener;
import org.xbup.lib.xb.block.XBBlockTerminationMode;
import org.xbup.lib.xb.ubnumber.type.UBNat32;

/**
 * Format declaration based on catalog path.
 *
 * @version 0.1 wr21.0 2011/12/31
 * @author XBUP Project (http://xbup.org)
 */
public class XBCPFormatDecl extends XBFormatDecl {

    private XBCatalog catalog;
    private XBCFormatSpec spec;
    private long[] path;

    /**
     * Creates a new instance of XBCPContextFormat
     */
    public XBCPFormatDecl(XBCatalog catalog, XBCFormatSpec spec) {
        this.catalog = catalog;
        this.spec = spec;
    }

    public XBCPFormatDecl(long[] formatPath) {
        this.path = formatPath;
    }

    public boolean equals(XBFormatDecl type) {
        if (type instanceof XBCPFormatDecl) {
            return (((XBCPFormatDecl) type).catalog == catalog)&&(((XBCPFormatDecl) type).spec == spec);
        } else {
            return false;
        }
    }

    @Override
    public List<XBGroupDecl> getGroups() {
       return catalog.getGroups(spec);
    }

    public long getGroupsCount() {
        XBCSpecService bindService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        Long count = bindService.getSpecDefsCount(spec);
        if (count==0) {
            return 0;
        }
        return count.longValue();
    }

    public XBBlockDecl getBlockType(int group, int block) {
        XBCSpecService bindService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        if ((spec.getXBIndex().intValue()>0)||(spec.getParent().getParent()!=null)) { // Not root context
            if (group==0) {
                return catalog.getRootContext().getBlockType(group,block);
            }
            group--;
        }
        XBCSpecDef formatBind = (XBCSpecDef) bindService.findSpecDefByXB(spec,group);
        if (formatBind == null) {
            return null;
        }
        XBCSpec groupSpec = (XBCSpec) formatBind.getTarget();
        if (groupSpec == null) {
            return null;
        }
        XBCSpecDef groupBind = (XBCSpecDef) bindService.findSpecDefByXB(groupSpec,block);
        if (groupBind == null) {
            return null;
        }
        XBCSpec blockSpec = (XBCSpec) groupBind.getTarget();
        if (blockSpec == null) {
            return null;
        }
        return new XBCSBlockDecl((XBCBlockSpec) blockSpec);
    }

    public void toXB(XBListener target) throws XBProcessingException, IOException {
        XBCNodeService nodeService = (XBCNodeService) catalog.getCatalogService(XBCNodeService.class);
        target.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        // TODO: DocumentSpecification - replace with relevant code later
        target.attribXB(new UBNat32(0));
        target.attribXB(new UBNat32(0));
        target.attribXB(new UBNat32(getGroupsCount()));
        target.attribXB(new UBNat32(1));
        target.attribXB(new UBNat32(2));
        target.beginXB(XBBlockTerminationMode.ZERO_TERMINATED);
        // TODO: Format Specification in catalog
        target.attribXB(new UBNat32(0));
        target.attribXB(new UBNat32(6));
        // TODO: UBPath type
        Long[] myPath = nodeService.getNodeXBPath(spec.getParent());
        target.attribXB(new UBNat32(myPath.length));
        for (int i = 0; i < myPath.length; i++) {
            target.attribXB(new UBNat32(myPath[i]));
        }
        target.attribXB(new UBNat32(spec.getXBIndex()));
        target.endXB();
    }

    public void setCatalog(XBCatalog catalog) {
        this.catalog = catalog;
    }
}
