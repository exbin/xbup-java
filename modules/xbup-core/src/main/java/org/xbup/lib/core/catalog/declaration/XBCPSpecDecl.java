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

import org.xbup.lib.core.block.declaration.XBContext;
import org.xbup.lib.core.catalog.XBCatalog;

/**
 *
 * @version 0.1.19 2010/06/02
 * @author XBUP Project (http://xbup.org)
 */
public class XBCPSpecDecl extends XBContext {

    private XBCatalog xbCatalog;
    private long specId;

    /**
     * Creates a new instance of XBCPContextSpec
     */
    public XBCPSpecDecl() {
        super(null);
        xbCatalog = null;
    }

    public XBCPSpecDecl(XBCatalog xbCatalog) {
        super(xbCatalog);
        this.xbCatalog = xbCatalog;
    }
/*
    public XBCPContextSpec(XBCatalog xbCatalog, XBCPContextSpec spec) {
        this.xbCatalog = xbCatalog;
        getGroups().clear();
        for (Iterator it = spec.getGroups().iterator(); it.hasNext();) getGroups().add((XBCTypeGroup) it.next());
    }

    public XBCPContextSpec(XBCatalog xbCatalog, XBFormatSpecification spec) {
        this.xbCatalog = xbCatalog;
        processSpec(spec);
    }
*/
    public XBCatalog getXbCatalog() {
        return xbCatalog;
    }

    public void setXbCatalog(XBCatalog xbCatalog) {
        this.xbCatalog = xbCatalog;
    }
/*
    public void processSpec() {
        getGroups().clear();
        getGroups().add(xbCatalog.getRootGroup());
        if (spec!=null) {
            XBFormatSpecificationBind elem;
            for (Iterator it = spec.getGroups().iterator();it.hasNext();) {
                elem = (XBFormatSpecificationBind) it.next();
                if (elem.getXbIndex()!=null) {
                    if (getGroups().size() <= elem.getXbIndex().intValue()+1) setListSize(elem.getXbIndex().intValue()+2); // TODO GetMaxXBIndex
                    XBCTypeGroup typeGroup = new XBCTypeGroup(xbCatalog);
                    getGroups().set(elem.getXbIndex().intValue()+1,typeGroup);
                    typeGroup.processSpec(elem.getHasGroup());
                }
            }
        }
    }

    public void processSpec(XBFormatSpecification spec) {
        this.setSpec(spec);
        processSpec();
    }

    public XBFormatSpecification getSpec() {
        return spec;
    }

    public void setSpec(XBFormatSpecification spec) {
        this.spec = spec;
    }

    public XBCTypeBlock getBlockSpec(int groupIndex, int blockIndex) {
        if (getGroups().size()<=groupIndex) return null;
        XBCTypeGroup typeGroup = (XBCTypeGroup) getGroups().get(groupIndex);
        if (typeGroup==null) return null;
        if (typeGroup.getBlocks().size()<=blockIndex) return null;
        return (XBCTypeBlock) typeGroup.getBlocks().get(blockIndex);
    }
 */
}
