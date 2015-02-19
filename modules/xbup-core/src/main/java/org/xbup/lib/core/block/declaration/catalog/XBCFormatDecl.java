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
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBFormatDecl;
import org.xbup.lib.core.block.declaration.XBGroupDecl;
import org.xbup.lib.core.block.declaration.local.XBLFormatDecl;
import org.xbup.lib.core.block.definition.XBFormatDef;
import org.xbup.lib.core.block.definition.catalog.XBCFormatDef;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.XBPCatalog;
import org.xbup.lib.core.catalog.base.XBCFormatRev;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.param.XBPSequenceSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerializable;
import org.xbup.lib.core.serial.param.XBSerializationMode;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 1 format declaration defined by catalog specification.
 *
 * @version 0.1.24 2014/12/05
 * @author XBUP Project (http://xbup.org)
 */
public class XBCFormatDecl implements XBFormatDecl, XBPSequenceSerializable {

    private XBCFormatRev formatSpecRev;
    private final XBCatalog catalog;

    public XBCFormatDecl() {
        this(null, new XBPCatalog());
    }

    public XBCFormatDecl(XBCFormatRev formatSpec, XBCatalog catalog) {
        this.formatSpecRev = formatSpec;
        this.catalog = catalog;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof XBCFormatDecl) {
            final XBCFormatDecl other = (XBCFormatDecl) obj;
            if (formatSpecRev == null || !this.formatSpecRev.equals(other.formatSpecRev)) {
                return false;
            }

            return formatSpecRev.getId().equals(other.formatSpecRev.getId());
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

            return formatSpecRev.getXBIndex().equals(((XBLFormatDecl) obj).getRevision());
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
            formatSpecRev = format == null ? null : format.getFormatSpec();
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
        return formatSpecRev == null ? new ArrayList<XBGroupDecl>() : catalog.getGroups(formatSpecRev.getParent());
    }

    public XBCFormatRev getFormatSpec() {
        return formatSpecRev;
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
