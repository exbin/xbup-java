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
import org.xbup.lib.core.block.declaration.XBGroupDecl;
import org.xbup.lib.core.block.definition.XBGroupDef;

/**
 * XBUP level 1 group declaration using catalog path.
 *
 * @version 0.1.24 2014/10/02
 * @author XBUP Project (http://xbup.org)
 */
public class XBPGroupDecl implements XBGroupDecl {

    private long[] catalogPath;
    private int revision;

    public XBPGroupDecl() {
        catalogPath = null;
    }

    public XBPGroupDecl(long[] path) {
        this.catalogPath = path;
    }

    public XBPGroupDecl(Long[] path) {
        setCatalogObjectPath(path);
    }

    private void setCatalogObjectPath(Long[] path) {
        catalogPath = new long[path.length];
        for (int i = 0; i < path.length; i++) {
            catalogPath[i] = path[i];
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
        if (obj instanceof XBPGroupDecl) {
            return Arrays.equals(((XBPGroupDecl) obj).catalogPath, catalogPath) && (((XBPGroupDecl) obj).revision == revision);
        }

        return super.equals(obj);
    }

    public long[] getCatalogPath() {
        return catalogPath;
    }

    public void setCatalogPath(long[] catalogPath) {
        this.catalogPath = catalogPath;
    }

    public void setCatalogPath(Long[] path) {
        setCatalogObjectPath(path);
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
     }    public void setCatalogPath(Long[] path) {
     setCatalogObjectPath(path);
     }

     }

     public void processSpec(XBGroupSpecification spec) {
     setSpec(spec);
     processSpec();
     }
     */
    @Override
    public XBGroupDef getGroupDef() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getRevision() {
        return revision;
    }
}
