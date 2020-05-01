/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.block.declaration.catalog;

import java.io.IOException;
import javax.annotation.Nullable;
import org.exbin.xbup.core.block.XBBasicBlockType;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.block.declaration.XBBlockDecl;
import org.exbin.xbup.core.block.declaration.local.XBLBlockDecl;
import org.exbin.xbup.core.block.definition.XBBlockDef;
import org.exbin.xbup.core.block.definition.catalog.XBCBlockDef;
import org.exbin.xbup.core.catalog.XBCatalog;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.serial.param.XBPSequenceSerialHandler;
import org.exbin.xbup.core.serial.param.XBPSequenceSerializable;
import org.exbin.xbup.core.serial.param.XBSerializationMode;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

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
    public boolean equals(@Nullable Object obj) {
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
