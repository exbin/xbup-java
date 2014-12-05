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
import org.xbup.lib.core.serial.sequence.XBASequenceSerialHandler;
import org.xbup.lib.core.serial.sequence.XBASequenceSerializable;
import org.xbup.lib.core.serial.sequence.XBASerialSequenceable;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 1 group specification.
 *
 * @version 0.1.24 2014/12/05
 * @author XBUP Project (http://xbup.org)
 */
public class XBCGroupDecl implements XBGroupDecl, XBASequenceSerializable {

    private XBCGroupRev groupSpecRev;
    private final XBCatalog catalog;

    public XBCGroupDecl(XBCGroupRev groupSpec, XBCatalog catalog) {
        this.groupSpecRev = groupSpec;
        this.catalog = catalog;
    }

    public List<XBBlockDecl> getGroups() {
        return catalog.getBlocks(groupSpecRev.getParent());
    }

    @Override
    public void serializeXB(XBASequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
        serializationHandler.begin();
        serializationHandler.matchType(new XBFixedBlockType(XBBasicBlockType.GROUP_DECLARATION));
        if (serializationHandler.getSerializationMode() == XBASerialSequenceable.SerializationMode.PULL) {
            Long[] catalogPath = new Long[serializationHandler.pullAttribute().getInt()];
            int i;
            for (i = 0; i < catalogPath.length; i++) {
                catalogPath[i] = serializationHandler.pullAttribute().getLong();
            }
            long revision = serializationHandler.pullAttribute().getLong();
            XBCGroupDecl groupDecl = (XBCGroupDecl) catalog.findGroupTypeByPath(catalogPath, (int) revision);
            groupSpecRev = groupDecl == null ? null : groupDecl.getGroupSpec();
        } else {
            Long[] path = catalog.getSpecPath(groupSpecRev.getParent());
            serializationHandler.putAttribute(new UBNat32(path.length - 1));
            for (Long pathIndex : path) {
                serializationHandler.putAttribute(new UBNat32(pathIndex));
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
