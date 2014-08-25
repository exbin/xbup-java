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

import java.util.List;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.declaration.XBGroupDecl;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;

/**
 *
 * @version 0.1.19 2010/06/02
 * @author XBUP Project (http://xbup.org)
 */
public class XBCPGroupDecl extends XBGroupDecl {

    private XBCatalog catalog;
    private XBCGroupSpec spec;

    public XBCPGroupDecl() {
        catalog = null;
        spec = null;
    }

    /** Creates a new instance of XBCTypeFormat */
    public XBCPGroupDecl(XBCatalog catalog, XBCGroupSpec spec) {
        this.catalog = catalog;
        this.spec = spec;
    }

    public boolean equals(XBGroupDecl type) {
        if (type instanceof XBCPGroupDecl) {
            return (((XBCPGroupDecl) type).getCatalog() == getCatalog())&&(((XBCPGroupDecl) type).getSpec() == getSpec());
        } else {
            return false;
        }
    }

    @Override
    public List<XBBlockDecl> getBlocks() {
       return getCatalog().getBlocks(getSpec());
    }

    public int compareTo(Object o) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

/*
    public void processSpec() {
        getBlocks().clear();
        XBGroupSpecificationBind elem;
        for (Iterator it = spec.getBlocks().iterator(); it.hasNext();) {
            elem = (XBGroupSpecificationBind) it.next();
            if (elem.getXbIndex()!=null) {
                if (getBlocks().size() <= elem.getXbIndex().intValue()) setListSize(elem.getXbIndex().intValue()+1); // TODO GetMaxXBIndex
                XBCTypeBlock typeBlock = new XBCTypeBlock(xbCatalog);
                getBlocks().set(elem.getXbIndex().intValue(),typeBlock);
                typeBlock.processSpec(elem.getHasBlock());
            }
        }
    }

    public void processSpec(XBGroupSpecification spec) {
        setSpec(spec);
        processSpec();
    }
*/

    public XBCatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(XBCatalog catalog) {
        this.catalog = catalog;
    }

    public XBCGroupSpec getSpec() {
        return spec;
    }

    public void setSpec(XBCGroupSpec spec) {
        this.spec = spec;
    }
}
