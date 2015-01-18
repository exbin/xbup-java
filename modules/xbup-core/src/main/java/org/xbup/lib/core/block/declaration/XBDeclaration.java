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
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.catalog.XBCFormatDecl;
import org.xbup.lib.core.block.declaration.local.XBLFormatDecl;
import org.xbup.lib.core.block.declaration.local.XBLGroupDecl;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.sequence.XBSerializationMode;
import org.xbup.lib.core.serial.sequence.XBTSequenceSerialHandler;
import org.xbup.lib.core.serial.sequence.XBTSequenceSerializable;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Representation of declaration block.
 *
 * @version 0.1 wr24.0 2015/01/18
 * @author XBUP Project (http://xbup.org)
 */
public class XBDeclaration implements XBTSequenceSerializable {

    private XBFormatDecl formatDecl;
    private XBFormatDecl contextFormatDecl = null;
    private XBSerializable rootBlock;
    private boolean headerMode = false;

    private UBNat32 groupsReserved = new UBNat32(0);
    private UBNat32 preserveCount = new UBNat32(0);

    public XBDeclaration(XBFormatDecl formatDecl, XBSerializable rootBlock) {
        this.formatDecl = formatDecl;
        this.rootBlock = rootBlock;
    }

    public XBDeclaration(XBFormatDecl format) {
        this(format, null);
    }

    public XBDeclaration(XBGroupDecl group) {
        this(group, null);
    }

    public XBDeclaration(XBGroupDecl group, XBSerializable rootBlock) {
        this(new XBLFormatDecl(group), rootBlock);
    }

    public XBDeclaration(XBBlockDecl blockDecl) {
        this(blockDecl, null);
    }

    public XBDeclaration(XBBlockDecl block, XBSerializable rootNode) {
        this(new XBLGroupDecl(block), rootNode);
    }

    public XBContext generateContext(XBCatalog catalog) {
        return generateContext(null, catalog);
    }

    public XBContext generateContext(XBContext parentContext, XBCatalog catalog) {
        XBContext context = new XBContext();
        context.setParent(parentContext);
        context.setStartFrom(preserveCount.getInt() + 1);
        List<XBGroup> groups = context.getGroups();

        XBFormatDecl decl = contextFormatDecl != null ? contextFormatDecl : formatDecl;
        List<XBGroupDecl> formatGroups = decl.getGroupDecls();
        for (XBGroupDecl formatGroup : formatGroups) {
            groups.add(convertCatalogGroup(formatGroup, catalog));
        }

        return context;
    }

    public static XBGroup convertCatalogGroup(XBGroupDecl groupDecl, XBCatalog catalog) {
        if (groupDecl == null) {
            return null;
        }

        XBGroup group = new XBGroup();
        group.getBlocks().addAll(groupDecl.getBlockDecls());
        return group;
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
        XBFormatDecl decl = contextFormatDecl != null ? contextFormatDecl : formatDecl;

        if (group == 0) {
            return null;
        }
//        if ((group <= preserveCount)&&(parent != null)) return parent.getGroup(group);
        if ((group > preserveCount.getInt()) && (group < preserveCount.getInt() + groupsReserved.getInt() + 1) && (decl != null)) {
            if (decl instanceof XBLFormatDecl) {
                if (group - preserveCount.getInt() - 1 >= ((XBLFormatDecl) decl).getGroupDecls().size()) {
                    return null;
                }

                return ((XBLFormatDecl) decl).getGroupDecls().get(group - preserveCount.getInt() - 1);
            } else if (decl instanceof XBCFormatDecl) {
                if (group - preserveCount.getInt() - 1 >= ((XBCFormatDecl) decl).getGroupDecls().size()) {
                    return null;
                }

                return ((XBCFormatDecl) decl).getGroupDecls().get(group - preserveCount.getInt() - 1);
            } else {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }

        return null;
    }

    @Override
    public void serializeXB(XBTSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
        serializationHandler.begin();
        serializationHandler.matchType(new XBFixedBlockType(XBBasicBlockType.DECLARATION));
        serializationHandler.attribute(groupsReserved);
        serializationHandler.attribute(preserveCount);
        if (serializationHandler.getSerializationMode() == XBSerializationMode.PULL) {
            // TODO
            serializationHandler.child(formatDecl);
            if (!headerMode) {
                serializationHandler.child(rootBlock);
            }
        } else {
            serializationHandler.child(formatDecl);
            if (!headerMode) {
                serializationHandler.child(rootBlock);
            }
        }
        if (!headerMode) {
            serializationHandler.end();
        }
    }

    public XBFormatDecl getFormat() {
        return formatDecl;
    }

    public void setFormat(XBFormatDecl format) {
        this.formatDecl = format;
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

    public XBFormatDecl getContextFormatDecl() {
        return contextFormatDecl;
    }

    public void setContextFormatDecl(XBFormatDecl contextFormatDecl) {
        this.contextFormatDecl = contextFormatDecl;
    }

    public void realignReservation() {
        // TODO more safe approach later
        groupsReserved = new UBNat32(contextFormatDecl != null ? contextFormatDecl.getGroupDecls().size() : formatDecl.getGroupDecls().size());
    }

    public boolean isHeaderMode() {
        return headerMode;
    }

    public void setHeaderMode(boolean headerMode) {
        this.headerMode = headerMode;
    }
}
