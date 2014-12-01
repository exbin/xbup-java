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

import java.io.IOException;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.definition.XBBlockDef;
import org.xbup.lib.core.block.definition.catalog.XBCBlockDef;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.base.XBCBlockRev;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.child.XBAChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBAChildOutputSerialHandler;
import org.xbup.lib.core.serial.child.XBAChildSerializable;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Block type declaration defined by catalog specification.
 *
 * @version 0.1.24 2014/11/30
 * @author XBUP Project (http://xbup.org)
 */
public class XBCBlockDecl implements XBBlockDecl, XBAChildSerializable {

    private XBCBlockRev blockSpecRev;
    private final XBCatalog catalog;

    public XBCBlockDecl(XBCBlockRev blockSpecRev, XBCatalog catalog) {
        this.blockSpecRev = blockSpecRev;
        this.catalog = catalog;
    }

    @Override
    public void serializeFromXB(XBAChildInputSerialHandler serializationHandler) throws XBProcessingException, IOException {
        serializationHandler.begin();
        serializationHandler.matchType(new XBFixedBlockType(XBBasicBlockType.BLOCK_DECLARATION_LINK));

        UBNatural pathLength = serializationHandler.nextAttribute();
        Long[] path = new Long[pathLength.getInt()];
        for (int i = 0; i < pathLength.getInt(); i++) {
            path[i] = serializationHandler.nextAttribute().getLong();
        }

        XBCBlockDecl block = (XBCBlockDecl) catalog.findBlockTypeByPath(path, 0);
        blockSpecRev = block.getBlockSpec();
        serializationHandler.end();
    }

    @Override
    public void serializeToXB(XBAChildOutputSerialHandler serializationHandler) throws XBProcessingException, IOException {
        serializationHandler.begin(XBBlockTerminationMode.SIZE_SPECIFIED);
        serializationHandler.setType(new XBFixedBlockType(XBBasicBlockType.BLOCK_DECLARATION_LINK));
        Long[] path = catalog.getSpecPath(blockSpecRev.getParent());
        serializationHandler.addAttribute(new UBNat32(path.length - 1));
        for (Long pathIndex : path) {
            serializationHandler.addAttribute(new UBNat32(pathIndex));
        }

        serializationHandler.addAttribute(new UBNat32(blockSpecRev.getXBIndex()));
        serializationHandler.end();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof XBBlockDecl)) {
            return false;
        }

        if (obj instanceof XBCBlockDecl) {
            final XBCBlockDecl other = (XBCBlockDecl) obj;
            if (blockSpecRev == null || !this.blockSpecRev.equals(other.blockSpecRev)) {
                return false;
            }

            return blockSpecRev.getId().equals(other.blockSpecRev.getId());
        } else if (obj instanceof XBPBlockDecl) {
            Long[] catalogPath = catalog.getSpecPath(getBlockSpec().getParent());
            long[] objCatalogPath = ((XBPBlockDecl) obj).getCatalogPath();
            if (objCatalogPath.length != catalogPath.length) {
                return false;
            }

            for (int pathIndex = 0; pathIndex < catalogPath.length; pathIndex++) {
                if (catalogPath[pathIndex] != objCatalogPath[pathIndex]) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (blockSpecRev != null ? blockSpecRev.hashCode() : 0);
        return hash;
    }

    public XBCBlockRev getBlockSpec() {
        return blockSpecRev;
    }

    public void setBlockSpec(XBCBlockRev blockSpec) {
        this.blockSpecRev = blockSpec;
    }

    @Override
    public XBBlockDef getBlockDef() {
        return new XBCBlockDef(catalog, blockSpecRev.getParent());
    }

    @Override
    public long getRevision() {
        return blockSpecRev.getXBIndex();
    }
}
