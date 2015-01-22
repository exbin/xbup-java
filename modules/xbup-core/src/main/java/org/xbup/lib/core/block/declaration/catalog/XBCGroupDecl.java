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
import java.util.List;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.declaration.XBGroupDecl;
import org.xbup.lib.core.block.definition.XBGroupDef;
import org.xbup.lib.core.block.definition.catalog.XBCGroupDef;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.base.XBCGroupRev;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.param.XBPSequenceSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerializable;
import org.xbup.lib.core.serial.param.XBSerializationMode;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 1 group declaration defined by catalog specification.
 *
 * @version 0.1.24 2015/01/17
 * @author XBUP Project (http://xbup.org)
 */
public class XBCGroupDecl implements XBGroupDecl, XBPSequenceSerializable {

    private XBCGroupRev groupSpecRev;
    private final XBCatalog catalog;

    public XBCGroupDecl(XBCGroupRev groupSpec, XBCatalog catalog) {
        this.groupSpecRev = groupSpec;
        this.catalog = catalog;
    }

    @Override
    public List<XBBlockDecl> getBlockDecls() {
        return catalog.getBlocks(groupSpecRev.getParent());
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
            groupSpecRev = groupDecl == null ? null : groupDecl.getGroupSpec();
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

    public XBCGroupRev getGroupSpec() {
        return groupSpecRev;
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
