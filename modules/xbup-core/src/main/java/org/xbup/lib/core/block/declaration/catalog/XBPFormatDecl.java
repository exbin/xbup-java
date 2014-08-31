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
package org.xbup.lib.core.block.declaration.catalog;

import java.util.Arrays;
import org.xbup.lib.core.block.declaration.XBFormatDecl;

/**
 * XBUP level 1 format declaration using catalog path.
 *
 * @version 0.1.24 2014/08/29
 * @author XBUP Project (http://xbup.org)
 */
public class XBPFormatDecl implements XBFormatDecl {

    private long[] catalogPath;
    private int revision;

    public XBPFormatDecl() {
        catalogPath = null;
    }

    public XBPFormatDecl(long[] path) {
        this.catalogPath = path;
    }

    public XBPFormatDecl(Long[] path) {
        setCatalogPath(path);
    }

    public void setCatalogPath(Long[] path) {
        setCatalogPath(new long[path.length]);
        for (int i = 0; i < path.length; i++) {
            getCatalogPath()[i] = path[i];
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Arrays.hashCode(this.catalogPath);
        hash = 47 * hash + this.revision;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof XBPFormatDecl) {
            return Arrays.equals(((XBPFormatDecl) obj).catalogPath, catalogPath) && (((XBPFormatDecl) obj).revision == revision);
        }

        return super.equals(obj);
    }

    public long[] getCatalogPath() {
        return catalogPath;
    }

    public void setCatalogPath(long[] catalogPath) {
        this.catalogPath = catalogPath;
    }

    /*public XBBlockDecl getBlockType(int group, int block) {
        XBCSpecService bindService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        if ((spec.getXBIndex().intValue() > 0) || (spec.getParent().getParent() != null)) { // Not root context
            if (group == 0) {
                return catalog.getRootContext().getBlockType(group, block);
            }
            group--;
        }
        XBCSpecDef formatBind = (XBCSpecDef) bindService.findSpecDefByXB(spec, group);
        if (formatBind == null) {
            return null;
        }
        XBCSpec groupSpec = (XBCSpec) formatBind.getTarget();
        if (groupSpec == null) {
            return null;
        }
        XBCSpecDef groupBind = (XBCSpecDef) bindService.findSpecDefByXB(groupSpec, block);
        if (groupBind == null) {
            return null;
        }
        XBCSpec blockSpec = (XBCSpec) groupBind.getTarget();
        if (blockSpec == null) {
            return null;
        }
        return new XBCBlockDecl((XBCBlockSpec) blockSpec);
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
        target.beginXB(XBBlockTerminationMode.TERMINATED_BY_ZERO);
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
    } */
}
