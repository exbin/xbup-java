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
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;

/**
 * XBUP level 1 block declaration using catalog path.
 *
 * @version 0.1.24 2014/08/29
 * @author XBUP Project (http://xbup.org)
 */
public class XBPBlockDecl implements XBBlockDecl {

    private long[] catalogPath;
    private int revision;

    public XBPBlockDecl() {
        catalogPath = null;
    }

    public XBPBlockDecl(long[] path) {
        this.catalogPath = path;
    }

    public XBPBlockDecl(Long[] path) {
        setCatalogPath(path);
    }

    public void setCatalogPath(Long[] path) {
        setCatalogPath(new long[path.length]);
        for (int i = 0; i < path.length; i++) {
            getCatalogPath()[i] = path[i];
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof XBPBlockDecl) {
            return Arrays.equals(((XBPBlockDecl) obj).catalogPath, catalogPath) && (((XBPBlockDecl) obj).revision == revision);
        }

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Arrays.hashCode(this.catalogPath);
        hash = 47 * hash + this.revision;
        return hash;
    }

    public XBCBlockSpec getBlockSpec(XBCatalog catalog) {
        return (XBCBlockSpec) catalog.findBlockTypeByPath(getCatalogObjectPath(), revision);
    }

    /**
     * Get catalog path as array of Long instances.
     *
     * @return the catalogPath
     */
    public Long[] getCatalogObjectPath() {
        Long[] objectPath = new Long[catalogPath.length];
        for (int i = 0; i < objectPath.length; i++) {
            objectPath[i] = catalogPath[i];
        }
        return objectPath;
    }

    public long[] getCatalogPath() {
        return catalogPath;
    }

    public void setCatalogPath(long[] catalogPath) {
        this.catalogPath = catalogPath;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    /*    public boolean produceXBT() {
     throw new UnsupportedOperationException("Not supported yet.");
     try {
     eventListener.beginXBL1(false);
     eventListener.typeXBL1(new XBL1SBBlockDecl(XBBasicBlockTypeEnum.BLOCK_CATALOG_LINK));
     eventListener.attribXBL1(new UBNat32(path.length-1));
     for (int i = 0; i < path.length; i++) {
     eventListener.attribXBL1(new UBNat32(path[i]));
     }
     eventListener.endXBL1();
     } catch (XBProcessingException ex) {
     Logger.getLogger(XBL1CFormatDecl.class.getName()).log(Level.SEVERE, null, ex);
     } catch (IOException ex) {
     Logger.getLogger(XBL1CFormatDecl.class.getName()).log(Level.SEVERE, null, ex);
     }
     } */
}
