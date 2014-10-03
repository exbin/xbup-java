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
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.base.XBCBlockRev;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.serial.child.XBTChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildOutputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildSerializable;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Block type declaration defined by catalog specification.
 *
 * @version 0.1.24 2014/10/02
 * @author XBUP Project (http://xbup.org)
 */
public class XBCBlockDecl implements XBBlockDecl, XBTChildSerializable {

    private XBCBlockRev blockSpec;
    private final XBCatalog catalog;

    public XBCBlockDecl(XBCBlockRev blockSpec, XBCatalog catalog) {
        this.blockSpec = blockSpec;
        this.catalog = catalog;
    }

    @Override
    public void serializeFromXB(XBTChildInputSerialHandler serializationHandler) throws XBProcessingException, IOException {
        serializationHandler.begin();
        XBBlockType type = serializationHandler.getType();
        if (type.getAsBasicType() != XBBasicBlockType.BLOCK_DECLARATION_LINK) {
            throw new XBProcessingException("Unexpected block type", XBProcessingExceptionType.BLOCK_TYPE_MISMATCH);
        }

        UBNatural pathLength = serializationHandler.nextAttribute();
        Long[] path = new Long[pathLength.getInt()];
        for (int i = 0; i < pathLength.getInt(); i++) {
            path[i] = serializationHandler.nextAttribute().getLong();
        }

        XBCBlockDecl block = (XBCBlockDecl) catalog.findBlockTypeByPath(path, 0);
        blockSpec = block.getBlockSpec();
        serializationHandler.end();
    }

    @Override
    public void serializeToXB(XBTChildOutputSerialHandler serializationHandler) throws XBProcessingException, IOException {
        serializationHandler.begin(XBBlockTerminationMode.SIZE_SPECIFIED);
        serializationHandler.setType(new XBFixedBlockType(XBBasicBlockType.BLOCK_DECLARATION_LINK));
        Long[] path = catalog.getSpecPath(blockSpec.getParent());
        serializationHandler.addAttribute(new UBNat32(path.length - 1));
        for (Long pathIndex : path) {
            serializationHandler.addAttribute(new UBNat32(pathIndex));
        }

        serializationHandler.addAttribute(new UBNat32(blockSpec.getXBIndex()));
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
            if (blockSpec == null || !this.blockSpec.equals(other.blockSpec)) {
                return false;
            }

            return blockSpec.getId() != other.blockSpec.getId().longValue();
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
        hash = 79 * hash + (blockSpec != null ? blockSpec.hashCode() : 0);
        return hash;
    }

    public XBCBlockRev getBlockSpec() {
        return blockSpec;
    }

    public void setBlockSpec(XBCBlockRev blockSpec) {
        this.blockSpec = blockSpec;
    }
}
