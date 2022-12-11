/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.block.declaration.catalog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import org.exbin.xbup.core.block.XBBasicBlockType;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.block.declaration.XBFormatDecl;
import org.exbin.xbup.core.block.declaration.XBGroupDecl;
import org.exbin.xbup.core.block.declaration.local.XBLFormatDecl;
import org.exbin.xbup.core.block.definition.XBFormatDef;
import org.exbin.xbup.core.block.definition.catalog.XBCFormatDef;
import org.exbin.xbup.core.catalog.XBCatalog;
import org.exbin.xbup.core.catalog.XBPCatalog;
import org.exbin.xbup.core.catalog.base.XBCFormatRev;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.serial.param.XBPSequenceSerialHandler;
import org.exbin.xbup.core.serial.param.XBPSequenceSerializable;
import org.exbin.xbup.core.serial.param.XBSerializationMode;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * XBUP level 1 format declaration defined by catalog specification.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBCFormatDecl implements XBFormatDecl, XBPSequenceSerializable {

    private XBCFormatRev formatSpecRev;
    private final XBCatalog catalog;

    public XBCFormatDecl() {
        this(null, new XBPCatalog());
    }

    public XBCFormatDecl(XBCFormatRev formatSpecRev, XBCatalog catalog) {
        this.formatSpecRev = formatSpecRev;
        this.catalog = catalog;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof XBCFormatDecl) {
            final XBCFormatDecl other = (XBCFormatDecl) obj;
            if (formatSpecRev == null || !this.formatSpecRev.equals(other.formatSpecRev)) {
                return false;
            }

            return formatSpecRev.getId() == other.formatSpecRev.getId();
        } else if (obj instanceof XBLFormatDecl) {
            Long[] catalogPath = catalog.getSpecPath(formatSpecRev.getParent());
            long[] objCatalogPath = ((XBLFormatDecl) obj).getCatalogPath();
            if (objCatalogPath.length != catalogPath.length) {
                return false;
            }

            for (int pathIndex = 0; pathIndex < catalogPath.length; pathIndex++) {
                if (catalogPath[pathIndex] != objCatalogPath[pathIndex]) {
                    return false;
                }
            }

            return formatSpecRev.getXBIndex() == ((XBLFormatDecl) obj).getRevision();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (formatSpecRev != null ? formatSpecRev.hashCode() : 0);
        return hash;
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
        serializationHandler.begin();
        serializationHandler.matchType(new XBFixedBlockType(XBBasicBlockType.FORMAT_DECLARATION));
        if (serializationHandler.getSerializationMode() == XBSerializationMode.PULL) {
            Long[] catalogPath = new Long[serializationHandler.pullIntAttribute()];
            for (int pathPosition = 0; pathPosition < catalogPath.length; pathPosition++) {
                catalogPath[pathPosition] = serializationHandler.pullLongAttribute();
            }
            long revision = serializationHandler.pullLongAttribute();
            XBCFormatDecl format = (XBCFormatDecl) catalog.findFormatTypeByPath(catalogPath, (int) revision);
            formatSpecRev = format == null ? null : format.getFormatSpecRev();
        } else {
            Long[] path = catalog.getSpecPath(formatSpecRev.getParent());
            serializationHandler.putAttribute(path.length - 1);
            for (Long pathIndex : path) {
                serializationHandler.putAttribute(pathIndex);
            }

            serializationHandler.putAttribute(new UBNat32(formatSpecRev.getXBIndex()));
        }
        serializationHandler.end();
    }

    @Override
    public List<XBGroupDecl> getGroupDecls() {
        return formatSpecRev == null ? new ArrayList<>() : catalog.getGroups(formatSpecRev.getParent());
    }

    public XBCFormatRev getFormatSpecRev() {
        return formatSpecRev;
    }

    public void setFormatSpecRev(XBCFormatRev formatSpecRev) {
        this.formatSpecRev = formatSpecRev;
    }

    @Override
    public XBFormatDef getFormatDef() {
        return new XBCFormatDef(catalog, formatSpecRev.getParent());
    }

    @Override
    public long getRevision() {
        return formatSpecRev.getXBIndex();
    }
}
