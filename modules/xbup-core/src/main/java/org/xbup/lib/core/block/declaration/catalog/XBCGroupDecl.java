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
import org.xbup.lib.core.block.declaration.XBGroupDecl;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.base.XBCGroupRev;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.serial.child.XBTChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildOutputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildSerializable;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 1 group specification.
 *
 * @version 0.1.24 2014/09/02
 * @author XBUP Project (http://xbup.org)
 */
public class XBCGroupDecl implements XBGroupDecl, XBTChildSerializable {

    private XBCGroupRev groupSpec;
    private final XBCatalog catalog;

    public XBCGroupDecl(XBCGroupRev groupSpec, XBCatalog catalog) {
        this.groupSpec = groupSpec;
        this.catalog = catalog;
    }

    @Override
    public void serializeFromXB(XBTChildInputSerialHandler serializationHandler) throws XBProcessingException, IOException {
        serializationHandler.begin();
        XBBlockType type = serializationHandler.getType();
        if (type.getAsBasicType() != XBBasicBlockType.GROUP_DECLARATION_LINK) {
            throw new XBProcessingException("Unexpected block type", XBProcessingExceptionType.BLOCK_TYPE_MISMATCH);
        }

        UBNatural pathLength = serializationHandler.nextAttribute();
        Long[] path = new Long[pathLength.getInt()];
        for (int i = 0; i < pathLength.getInt(); i++) {
            path[i] = serializationHandler.nextAttribute().getLong();
        }

        XBCGroupDecl group = (XBCGroupDecl) catalog.findGroupTypeByPath(path, 0);
        groupSpec = group.getGroupSpec();
        serializationHandler.end();
    }

    @Override
    public void serializeToXB(XBTChildOutputSerialHandler serializationHandler) throws XBProcessingException, IOException {
        serializationHandler.begin(XBBlockTerminationMode.SIZE_SPECIFIED);
        serializationHandler.setType(new XBFixedBlockType(XBBasicBlockType.GROUP_DECLARATION_LINK));
        Long[] path = catalog.getSpecPath(groupSpec.getParent());
        serializationHandler.addAttribute(new UBNat32(path.length - 1));
        for (Long pathIndex : path) {
            serializationHandler.addAttribute(new UBNat32(pathIndex));
        }

        serializationHandler.addAttribute(new UBNat32(groupSpec.getXBIndex()));
        serializationHandler.end();
    }

    public XBCGroupRev getGroupSpec() {
        return groupSpec;
    }
}
