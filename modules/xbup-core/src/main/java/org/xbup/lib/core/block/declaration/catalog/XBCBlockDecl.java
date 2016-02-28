/*
 * Copyright (C) ExBin Project
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

import org.xbup.lib.core.block.declaration.local.XBLBlockDecl;
import java.io.IOException;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.definition.XBBlockDef;
import org.xbup.lib.core.block.definition.catalog.XBCBlockDef;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.base.XBCBlockRev;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.param.XBPSequenceSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerializable;
import org.xbup.lib.core.serial.param.XBSerializationMode;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Block type declaration defined by catalog specification.
 *
 * @version 0.1.25 2015/03/13
 * @author ExBin Project (http://exbin.org)
 */
public class XBCBlockDecl implements XBBlockDecl, XBPSequenceSerializable {

    private XBCBlockRev blockSpecRev;
    private final XBCatalog catalog;

    public XBCBlockDecl(XBCBlockRev blockSpecRev, XBCatalog catalog) {
        this.blockSpecRev = blockSpecRev;
        this.catalog = catalog;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof XBCBlockDecl) {
            final XBCBlockDecl other = (XBCBlockDecl) obj;
            if (blockSpecRev == null || !this.blockSpecRev.equals(other.blockSpecRev)) {
                return false;
            }

            return blockSpecRev.getId().equals(other.blockSpecRev.getId());
        } else if (obj instanceof XBLBlockDecl) {
            Long[] catalogPath = catalog.getSpecPath(getBlockSpecRev().getParent());
            long[] objCatalogPath = ((XBLBlockDecl) obj).getCatalogPath();
            if (objCatalogPath.length != catalogPath.length) {
                return false;
            }

            for (int pathIndex = 0; pathIndex < catalogPath.length; pathIndex++) {
                if (catalogPath[pathIndex] != objCatalogPath[pathIndex]) {
                    return false;
                }
            }

            return blockSpecRev.getXBIndex().equals(((XBLBlockDecl) obj).getRevision());
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

    @Override
    public void serializeXB(XBPSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
        serializationHandler.begin();
        serializationHandler.matchType(new XBFixedBlockType(XBBasicBlockType.BLOCK_DECLARATION));
        if (serializationHandler.getSerializationMode() == XBSerializationMode.PULL) {
            Long[] catalogPath = new Long[serializationHandler.pullIntAttribute()];
            for (int pathPosition = 0; pathPosition < catalogPath.length; pathPosition++) {
                catalogPath[pathPosition] = serializationHandler.pullLongAttribute();
            }
            long revision = serializationHandler.pullLongAttribute();
            XBCBlockDecl blockDecl = (XBCBlockDecl) catalog.findBlockTypeByPath(catalogPath, (int) revision);
            blockSpecRev = blockDecl == null ? null : blockDecl.getBlockSpecRev();
        } else {
            if (blockSpecRev != null) {
                Long[] path = catalog.getSpecPath(blockSpecRev.getParent());
                serializationHandler.putAttribute(path.length - 1);
                for (Long pathIndex : path) {
                    serializationHandler.putAttribute(pathIndex);
                }

                serializationHandler.putAttribute(new UBNat32(blockSpecRev.getXBIndex()));
            }
        }
        serializationHandler.end();
    }

    public XBCBlockRev getBlockSpecRev() {
        return blockSpecRev;
    }

    public void setBlockSpecRev(XBCBlockRev blockSpecRev) {
        this.blockSpecRev = blockSpecRev;
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
