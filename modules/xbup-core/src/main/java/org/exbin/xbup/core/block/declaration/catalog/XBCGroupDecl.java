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
import java.util.List;
import javax.annotation.Nullable;
import org.exbin.xbup.core.block.XBBasicBlockType;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.block.declaration.XBBlockDecl;
import org.exbin.xbup.core.block.declaration.XBGroupDecl;
import org.exbin.xbup.core.block.declaration.local.XBLGroupDecl;
import org.exbin.xbup.core.block.definition.XBGroupDef;
import org.exbin.xbup.core.block.definition.catalog.XBCGroupDef;
import org.exbin.xbup.core.catalog.XBCatalog;
import org.exbin.xbup.core.catalog.base.XBCGroupRev;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.serial.param.XBPSequenceSerialHandler;
import org.exbin.xbup.core.serial.param.XBPSequenceSerializable;
import org.exbin.xbup.core.serial.param.XBSerializationMode;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * XBUP level 1 group declaration defined by catalog specification.
 *
 * @version 0.1.24 2015/01/17
 * @author ExBin Project (http://exbin.org)
 */
public class XBCGroupDecl implements XBGroupDecl, XBPSequenceSerializable {

    private XBCGroupRev groupSpecRev;
    private final XBCatalog catalog;

    public XBCGroupDecl(XBCGroupRev groupSpec, XBCatalog catalog) {
        this.groupSpecRev = groupSpec;
        this.catalog = catalog;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof XBCGroupDecl) {
            final XBCGroupDecl other = (XBCGroupDecl) obj;
            if (groupSpecRev == null || !this.groupSpecRev.equals(other.groupSpecRev)) {
                return false;
            }

            return groupSpecRev.getId().equals(other.groupSpecRev.getId());
        } else if (obj instanceof XBLGroupDecl) {
            Long[] catalogPath = catalog.getSpecPath(groupSpecRev.getParent());
            long[] objCatalogPath = ((XBLGroupDecl) obj).getCatalogPath();
            if (objCatalogPath.length != catalogPath.length) {
                return false;
            }

            for (int pathIndex = 0; pathIndex < catalogPath.length; pathIndex++) {
                if (catalogPath[pathIndex] != objCatalogPath[pathIndex]) {
                    return false;
                }
            }

            return groupSpecRev.getXBIndex().equals(((XBLGroupDecl) obj).getRevision());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (groupSpecRev != null ? groupSpecRev.hashCode() : 0);
        return hash;
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
        serializationHandler.begin();
        serializationHandler.matchType(new XBFixedBlockType(XBBasicBlockType.GROUP_DECLARATION));
        if (serializationHandler.getSerializationMode() == XBSerializationMode.PULL) {
            Long[] catalogPath = new Long[serializationHandler.pullIntAttribute()];
            for (int pathPosition = 0; pathPosition < catalogPath.length; pathPosition++) {
                catalogPath[pathPosition] = serializationHandler.pullLongAttribute();
            }
            long revision = serializationHandler.pullLongAttribute();
            XBCGroupDecl groupDecl = (XBCGroupDecl) catalog.findGroupTypeByPath(catalogPath, (int) revision);
            groupSpecRev = groupDecl == null ? null : groupDecl.getGroupSpecRev();
        } else {
            Long[] path = catalog.getSpecPath(groupSpecRev.getParent());
            serializationHandler.putAttribute(path.length - 1);
            for (Long pathIndex : path) {
                serializationHandler.putAttribute(pathIndex);
            }

            serializationHandler.putAttribute(new UBNat32(groupSpecRev.getXBIndex()));
        }
        serializationHandler.end();
    }

    public XBCGroupRev getGroupSpecRev() {
        return groupSpecRev;
    }

    public void setGroupSpecRev(XBCGroupRev groupSpecRev) {
        this.groupSpecRev = groupSpecRev;
    }

    @Override
    public List<XBBlockDecl> getBlockDecls() {
        return catalog.getBlocks(groupSpecRev.getParent());
    }

    @Override
    public XBGroupDef getGroupDef() {
        return new XBCGroupDef(catalog, groupSpecRev.getParent());
    }

    @Override
    public long getRevision() {
        return groupSpecRev.getXBIndex();
    }
}
