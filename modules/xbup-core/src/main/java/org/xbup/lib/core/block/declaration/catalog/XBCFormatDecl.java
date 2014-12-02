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
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBFormatDecl;
import org.xbup.lib.core.block.declaration.XBGroupDecl;
import org.xbup.lib.core.block.definition.XBFormatDef;
import org.xbup.lib.core.block.definition.catalog.XBCFormatDef;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.base.XBCFormatRev;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.serial.child.XBTChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildOutputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildSerializable;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 1 format declaration represented by catalog.
 *
 * @version 0.1.24 2014/11/30
 * @author XBUP Project (http://xbup.org)
 */
public class XBCFormatDecl implements XBFormatDecl, XBTChildSerializable {

    private XBCFormatRev formatSpecRev;
    private final XBCatalog catalog;

    public XBCFormatDecl(XBCFormatRev formatSpec, XBCatalog catalog) {
        this.formatSpecRev = formatSpec;
        this.catalog = catalog;
    }

    public List<XBGroupDecl> getGroups() {
        return formatSpecRev == null ? new ArrayList<XBGroupDecl>() : catalog.getGroups(formatSpecRev.getParent());
    }

    @Override
    public void serializeFromXB(XBTChildInputSerialHandler serializationHandler) throws XBProcessingException, IOException {
        serializationHandler.pullBegin();
        XBBlockType type = serializationHandler.pullType();
        if (type.getAsBasicType() != XBBasicBlockType.FORMAT_DECLARATION) {
            throw new XBProcessingException("Unexpected block type", XBProcessingExceptionType.BLOCK_TYPE_MISMATCH);
        }

        long groupLimit = serializationHandler.pullAttribute().getLong();
        Long[] catalogPath = new Long[serializationHandler.pullAttribute().getInt()];
        int i;
        for (i = 0; i < catalogPath.length; i++) {
            catalogPath[i] = serializationHandler.pullAttribute().getLong();
        }
        long revision = serializationHandler.pullAttribute().getLong();

        XBCFormatDecl format = (XBCFormatDecl) catalog.findFormatTypeByPath(catalogPath, (int) revision);
        formatSpecRev = format == null ? null : format.getFormatSpec();

        /* TODO
         if (type.getAsBasicType() != XBBasicBlockType.FORMAT_DECLARATION_LINK) {
         throw new XBProcessingException("Unexpected block type", XBProcessingExceptionType.BLOCK_TYPE_MISMATCH);
         }

         UBNatural pathLength = serializationHandler.pullAttribute();
         Long[] path = new Long[pathLength.getInt()];
         for (int i = 0; i < pathLength.getInt(); i++) {
         path[i] = serializationHandler.pullAttribute().getLong();
         }

         XBCFormatDecl format = (XBCFormatDecl) catalog.findFormatTypeByPath(path, 0);
         formatSpecRev = format == null ? null : format.getFormatSpec(); */
        serializationHandler.pullEnd();
    }

    @Override
    public void serializeToXB(XBTChildOutputSerialHandler serializationHandler) throws XBProcessingException, IOException {
        serializationHandler.putBegin(XBBlockTerminationMode.SIZE_SPECIFIED);
        serializationHandler.putType(new XBFixedBlockType(XBBasicBlockType.FORMAT_DECLARATION_LINK));
        Long[] path = catalog.getSpecPath(formatSpecRev.getParent());
        serializationHandler.putAttribute(new UBNat32(path.length - 1));
        for (Long pathIndex : path) {
            serializationHandler.putAttribute(new UBNat32(pathIndex));
        }

        serializationHandler.putAttribute(new UBNat32(formatSpecRev.getXBIndex()));
        serializationHandler.putEnd();
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
