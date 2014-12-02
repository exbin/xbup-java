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
package org.xbup.lib.core.block.declaration;

import java.io.IOException;
import java.util.List;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.catalog.XBCBlockDecl;
import org.xbup.lib.core.block.declaration.catalog.XBCFormatDecl;
import org.xbup.lib.core.block.declaration.catalog.XBCGroupDecl;
import org.xbup.lib.core.block.declaration.local.XBDFormatDecl;
import org.xbup.lib.core.block.declaration.local.XBDGroupDecl;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.base.XBCGroupRev;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.child.XBAChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBAChildOutputSerialHandler;
import org.xbup.lib.core.serial.child.XBAChildSerializable;
import org.xbup.lib.core.serial.sequence.XBASequenceSerialHandler;
import org.xbup.lib.core.serial.sequence.XBASequenceSerializable;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Representation of declaration block.
 *
 * @version 0.1 wr24.0 2014/12/02
 * @author XBUP Project (http://xbup.org)
 */
public class XBDeclaration implements XBASequenceSerializable, XBAChildSerializable {

    private XBFormatDecl format;
    private XBSerializable rootBlock;

    private UBNat32 groupsReserved = new UBNat32(0);
    private UBNat32 preserveCount = new UBNat32(0);

    public XBDeclaration(XBFormatDecl format, XBSerializable rootBlock) {
        this.format = format;
        this.rootBlock = rootBlock;
    }

    public XBDeclaration(XBFormatDecl format) {
        this(format, null);
    }

    public XBDeclaration(XBGroupDecl group) {
        this(group, null);
    }

    public XBDeclaration(XBGroupDecl group, XBSerializable rootBlock) {
        this(new XBDFormatDecl(group), rootBlock);
    }

    public XBDeclaration(XBBlockDecl blockDecl) {
        this(blockDecl, null);
    }

    public XBDeclaration(XBBlockDecl block, XBSerializable rootNode) {
        this(new XBDGroupDecl(block), rootNode);
    }

    public XBContext generateContext(XBCatalog catalog) {
        return generateContext(null, catalog);
    }

    public XBContext generateContext(XBContext parentContext, XBCatalog catalog) {
        XBContext context = new XBContext();
        context.setParent(parentContext);
        context.setStartFrom(preserveCount.getInt() + 1);
        List<XBGroup> groups = context.getGroups();

        if (format instanceof XBDFormatDecl) {
            XBDFormatDecl formatDecl = (XBDFormatDecl) format;
            for (int groupIndex = 0; groupIndex < formatDecl.getGroups().size() && groupIndex < formatDecl.getGroupsLimit() && groupIndex < groupsReserved.getInt(); groupIndex++) {
                XBGroupDecl group = formatDecl.getGroups().get(groupIndex);
                if (group instanceof XBDGroupDecl) {
                    groups.add(new XBGroup(((XBDGroupDecl) group).getBlocks()));
                } else if (group instanceof XBCGroupDecl) {
                    groups.add(convertCatalogGroup((XBCGroupDecl) group, catalog));
                }
            }
        } else if (format instanceof XBCFormatDecl) {
            XBCFormatDecl formatDecl = (XBCFormatDecl) format;
            List<XBGroupDecl> formatGroups = formatDecl.getGroups();
            for (XBGroupDecl formatGroup : formatGroups) {
                groups.add(convertCatalogGroup((XBCGroupDecl) formatGroup, catalog));
            }
        }

        return context;
    }

    public static XBGroup convertCatalogGroup(XBCGroupDecl groupDecl, XBCatalog catalog) {
        if (groupDecl != null) {
            XBGroup group = new XBGroup();
            // TODO revision
            XBCGroupRev groupSpec = ((XBCGroupDecl) groupDecl).getGroupSpec();
            List<XBBlockDecl> blocks = groupSpec != null ? catalog.getBlocks(groupSpec.getParent()) : null;
            if (blocks != null) {
                for (XBBlockDecl block : blocks) {
                    group.getBlocks().add((XBCBlockDecl) block);
                }
            }

            return group;
        }

        // TODO Fix this if it is first time and catalog is still loading
        return null;
    }

    /**
     * Returns count of currently allowed groups.
     *
     * @return count of groups
     */
    public long getGroupsCount() {
        return preserveCount.getLong() + groupsReserved.getLong() + 1;
    }

    public XBGroupDecl getGroup(int group) {
        if (group == 0) {
            return null;
        }
//        if ((group <= preserveCount)&&(parent != null)) return parent.getGroup(group);
        if ((group > preserveCount.getInt()) && (group < preserveCount.getInt() + groupsReserved.getInt() + 1) && (format != null)) {
            if (format instanceof XBDFormatDecl) {
                if (group - preserveCount.getInt() - 1 >= ((XBDFormatDecl) format).getGroups().size()) {
                    return null;
                }

                return ((XBDFormatDecl) format).getGroups().get(group - preserveCount.getInt() - 1);
            } else if (format instanceof XBCFormatDecl) {
                if (group - preserveCount.getInt() - 1 >= ((XBCFormatDecl) format).getGroups().size()) {
                    return null;
                }

                return ((XBCFormatDecl) format).getGroups().get(group - preserveCount.getInt() - 1);
            } else {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }

        return null;
    }

    @Override
    public void serializeXB(XBASequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
        serializationHandler.begin();
        serializationHandler.matchType(new XBFixedBlockType(XBBasicBlockType.DECLARATION));
        serializationHandler.attribute(groupsReserved);
        serializationHandler.attribute(preserveCount);
        if (rootBlock != null) {
            serializationHandler.child(rootBlock);
        }
        serializationHandler.end();
    }

    // TODO Will be replaced by sequence serialization
    @Override
    public void serializeFromXB(XBAChildInputSerialHandler serializationHandler) throws XBProcessingException, IOException {
        serializationHandler.pullBegin();
        serializationHandler.pullMatchingType(new XBFixedBlockType(XBBasicBlockType.DECLARATION));
        groupsReserved.setValue(serializationHandler.pullAttribute().getLong());
        preserveCount.setValue(serializationHandler.pullAttribute().getLong());
        serializationHandler.pullChild(format);
        if (rootBlock != null) {
            serializationHandler.pullChild(rootBlock);
        }
        serializationHandler.pullEnd();
    }

    // TODO Will be replaced by sequence serialization
    @Override
    public void serializeToXB(XBAChildOutputSerialHandler serializationHandler) throws XBProcessingException, IOException {
        serializationHandler.putBegin(XBBlockTerminationMode.SIZE_SPECIFIED);
        serializationHandler.putType(new XBFixedBlockType(XBBasicBlockType.BLOCK_DECLARATION_LINK));
        serializationHandler.putAttribute(groupsReserved);
        serializationHandler.putAttribute(preserveCount);
        serializationHandler.putChild(format);
        if (rootBlock != null) {
            serializationHandler.putChild(rootBlock);
        }
        serializationHandler.putEnd();
    }

    public XBFormatDecl getFormat() {
        return format;
    }

    public void setFormat(XBFormatDecl format) {
        this.format = format;
    }

    public int getGroupsReserved() {
        return groupsReserved.getInt();
    }

    public void setGroupsReserved(int groupsReserved) {
        this.groupsReserved = new UBNat32(groupsReserved);
    }

    public int getPreserveCount() {
        return preserveCount.getInt();
    }

    public void setPreserveCount(int preserveCount) {
        this.preserveCount = new UBNat32(preserveCount);
    }

    public XBSerializable getRootBlock() {
        return rootBlock;
    }

    public void setRootBlock(XBSerializable rootNode) {
        this.rootBlock = rootNode;
    }
}
