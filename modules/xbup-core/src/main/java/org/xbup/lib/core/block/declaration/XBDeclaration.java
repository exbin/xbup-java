/*
 * Copyright (C) ExBin Project
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
import java.io.InputStream;
import java.util.List;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBDBlockType;
import org.xbup.lib.core.block.XBFBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.catalog.XBCFormatDecl;
import org.xbup.lib.core.block.declaration.local.XBLFormatDecl;
import org.xbup.lib.core.block.declaration.local.XBLGroupDecl;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.basic.XBTBasicInputReceivingSerialHandler;
import org.xbup.lib.core.serial.basic.XBTBasicOutputReceivingSerialHandler;
import org.xbup.lib.core.serial.basic.XBTBasicReceivingSerializable;
import org.xbup.lib.core.serial.basic.XBReceivingFinished;
import org.xbup.lib.core.serial.param.XBPSequenceSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerializable;
import org.xbup.lib.core.serial.param.XBSerializationMode;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Representation of type declaration.
 *
 * @version 0.2.0 2015/09/19
 * @author ExBin Project (http://exbin.org)
 */
public class XBDeclaration implements XBPSequenceSerializable, XBTBasicReceivingSerializable, XBTypeConvertor {

    private XBDeclaration parent = null;
    private XBFormatDecl formatDecl;
    private XBSerializable rootBlock;
    private boolean headerMode = false;

    private UBNat32 groupsReserved = new UBNat32(0);
    private UBNat32 preserveCount = new UBNat32(0);

    /**
     * Creates new empty instance of type declaration.
     */
    public XBDeclaration() {
        this(new XBLFormatDecl());
    }

    /**
     * Creates new instance of type declaration for format declaration and root
     * block.
     *
     * @param formatDecl format declaration
     * @param rootBlock root block
     */
    public XBDeclaration(XBFormatDecl formatDecl, XBSerializable rootBlock) {
        this.formatDecl = formatDecl;
        this.rootBlock = rootBlock;
    }

    public XBDeclaration(XBFormatDecl format) {
        this(format, null);
    }

    /**
     * Creates new instance of type declaration constructing declaration from
     * single group declaration.
     *
     * @param groupDecl group declaration
     * @param rootBlock root block
     */
    public XBDeclaration(XBGroupDecl groupDecl, XBSerializable rootBlock) {
        this(new XBLFormatDecl(groupDecl), rootBlock);
    }

    public XBDeclaration(XBGroupDecl group) {
        this(group, null);
    }

    /**
     * Creates new instance of type declaration constructing declaration from
     * single block declaration.
     *
     * @param blockDecl block declaration
     * @param rootBlock root block
     */
    public XBDeclaration(XBBlockDecl blockDecl, XBSerializable rootBlock) {
        this(new XBLGroupDecl(blockDecl), rootBlock);
    }

    public XBDeclaration(XBBlockDecl blockDecl) {
        this(blockDecl, null);
    }

    public XBContext generateContext() {
        return generateContext((XBTypeConvertor) null);
    }

    public XBContext generateContext(XBCatalog catalog) {
        return generateContext(null, catalog);
    }

    public XBContext generateContext(XBTypeConvertor parentContext) {
        return generateContext(parentContext, null);
    }

    public XBContext generateContext(XBTypeConvertor parentContext, XBCatalog catalog) {
        XBContext context = new XBContext();
        context.setParent(parentContext);
        context.setStartFrom(preserveCount.getInt() + 1);

        List<XBGroup> groups = context.getGroups();
        XBFormatDecl decl = formatDecl;
        if (decl instanceof XBLFormatDecl) {
            if (catalog != null && ((XBLFormatDecl) decl).getFormatDef() == null) {
                XBFormatDecl catalogFormatDecl = catalog.findFormatTypeByPath(((XBLFormatDecl) decl).getCatalogPathAsClassArray(), (int) decl.getRevision());
                if (catalogFormatDecl != null) {
                    decl = catalogFormatDecl;
                }
            }
        }

        if (decl != null) {
            List<XBGroupDecl> formatGroups = decl.getGroupDecls();
            for (XBGroupDecl formatGroup : formatGroups) {
                groups.add(convertCatalogGroup(formatGroup, catalog));
            }
        }

        return context;
    }

    public static XBGroup convertCatalogGroup(XBGroupDecl groupDecl) {
        return convertCatalogGroup(groupDecl, null);
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

    @Override
    public XBDeclBlockType getDeclBlockType(XBFBlockType fixedType) {
        XBGroup group = getGroupForId(0);
        if (group != null) {
            XBBlockDecl blockDecl = group.getBlockForId(fixedType.getBlockID().getInt());
            return blockDecl != null ? new XBDeclBlockType(blockDecl) : null;
        }

        return null;
    }

    @Override
    public XBFixedBlockType getFixedBlockType(XBDBlockType declType) {
        return getFixedBlockType(declType.getBlockDecl(), -1);
    }

    @Override
    public XBFixedBlockType getFixedBlockType(XBBlockDecl blockDecl, int groupIdLimit) {
        // Simple search for first match
        for (int groupId = 0; groupId < getGroupsCount(); groupId++) {
            XBGroup group = getGroupForId(groupId);
            if (group != null) {
                int blockId = 0;
                for (XBBlockDecl matchedDecl : group.getBlocks()) {
                    if (matchedDecl.equals(blockDecl)) {
                        return new XBFixedBlockType(groupId, blockId);
                    }

                    blockId++;
                }
            }
        }

        return null;
    }

    @Override
    public XBGroup getGroupForId(int groupId) {
        XBFormatDecl decl = formatDecl;

        if (groupId == 0) {
            return null;
        }

        if ((groupId <= preserveCount.getInt()) && (parent != null)) {
            return parent.getGroupForId(groupId);
        }

        if ((groupId > preserveCount.getInt()) && (groupId < preserveCount.getInt() + groupsReserved.getInt() + 1) && (decl != null)) {
            if (decl instanceof XBLFormatDecl) {
                if (groupId - preserveCount.getInt() - 1 >= ((XBLFormatDecl) decl).getGroupDecls().size()) {
                    return null;
                }

                return new XBGroup(((XBLFormatDecl) decl).getGroupDecls().get(groupId - preserveCount.getInt() - 1).getBlockDecls());
            } else if (decl instanceof XBCFormatDecl) {
                if (groupId - preserveCount.getInt() - 1 >= ((XBCFormatDecl) decl).getGroupDecls().size()) {
                    return null;
                }

                return new XBGroup(((XBCFormatDecl) decl).getGroupDecls().get(groupId - preserveCount.getInt() - 1).getBlockDecls());
            } else {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }

        return null;
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
        serializationHandler.begin();
        serializationHandler.matchType(new XBFixedBlockType(XBBasicBlockType.DECLARATION));
        serializationHandler.attribute(groupsReserved);
        serializationHandler.attribute(preserveCount);
        if (serializationHandler.getSerializationMode() == XBSerializationMode.PULL) {
            if (!serializationHandler.pullIfEmptyBlock()) {
                if (formatDecl == null) {
                    // TODO Create XBL always?
                    formatDecl = new XBLFormatDecl();
                }
                serializationHandler.consist(formatDecl);
            } else {
                formatDecl = null;
            }
            if (!headerMode) {
                serializationHandler.consist(rootBlock);
            }
        } else {
            serializationHandler.consist(formatDecl);
            if (!headerMode) {
                serializationHandler.consist(rootBlock);
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

    public XBDeclaration getParent() {
        return parent;
    }

    public void setParent(XBDeclaration parent) {
        this.parent = parent;
    }

    public void realignReservation(XBCatalog catalog) {
        XBFormatDecl decl = formatDecl;
        if (formatDecl.getFormatDef() == null && catalog != null && formatDecl instanceof XBLFormatDecl) {
            XBFormatDecl catalogFormatDecl = catalog.findFormatTypeByPath(((XBLFormatDecl) formatDecl).getCatalogPathAsClassArray(), (int) formatDecl.getRevision());
            if (catalogFormatDecl != null) {
                decl = catalogFormatDecl;
            }
        }

        groupsReserved = new UBNat32(decl.getGroupDecls().size());
    }

    public boolean isHeaderMode() {
        return headerMode;
    }

    public void setHeaderMode(boolean headerMode) {
        this.headerMode = headerMode;
    }

    /**
     * Enumeration of receiving processing states.
     */
    private enum RecvProcessingState {

        START, BEGIN, TYPE, GROUPS_RESERVED, PRESERVE_COUNT, FORMAT_DECLARATION, ROOT_NODE, END
    }

    @Override
    public void serializeRecvFromXB(XBTBasicInputReceivingSerialHandler serializationHandler) throws XBProcessingException, IOException {
        serializationHandler.process(new RecvSerialization());
    }

    private class RecvSerialization implements XBTListener, XBReceivingFinished {

        private RecvProcessingState processingState = RecvProcessingState.START;
        private XBTListener activeListener = null;

        @Override
        public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
            if (processingState == RecvProcessingState.GROUPS_RESERVED || processingState == RecvProcessingState.PRESERVE_COUNT) {
                if (formatDecl == null) {
                    // TODO Create XBL always?
                    formatDecl = new XBLFormatDecl();
                }
                ((XBTBasicReceivingSerializable) formatDecl).serializeRecvFromXB(new XBTBasicInputReceivingSerialHandler() {

                    @Override
                    public void process(XBTListener listener) {
                        activeListener = listener;
                    }
                });
                processingState = RecvProcessingState.PRESERVE_COUNT;
            } else if (processingState == RecvProcessingState.FORMAT_DECLARATION) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            if (activeListener != null) {
                activeListener.beginXBT(terminationMode);
                return;
            }

            if (processingState == RecvProcessingState.BEGIN) {
                throw new XBProcessingException("Unexpected token: begin", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }

            processingState = RecvProcessingState.BEGIN;
        }

        @Override
        public void typeXBT(XBBlockType type) throws XBProcessingException, IOException {
            if (activeListener != null) {
                activeListener.typeXBT(type);
                return;
            }

            if (processingState != RecvProcessingState.BEGIN) {
                throw new XBProcessingException("Unexpected token: type", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }

            processingState = RecvProcessingState.TYPE;
        }

        @Override
        public void attribXBT(XBAttribute value) throws XBProcessingException, IOException {
            if (activeListener != null) {
                activeListener.attribXBT(value);
                return;
            }

            if (null != processingState) switch (processingState) {
                case TYPE:
                    groupsReserved.setValue(value.getNaturalLong());
                    processingState = RecvProcessingState.GROUPS_RESERVED;
                    break;
                case GROUPS_RESERVED:
                    preserveCount.setValue(value.getNaturalLong());
                    processingState = RecvProcessingState.PRESERVE_COUNT;
                    break;
                default:
                    throw new XBProcessingException("Unexpected token: attribute", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        }

        @Override
        public void dataXBT(InputStream data) throws XBProcessingException, IOException {
            if (activeListener != null) {
                activeListener.dataXBT(data);
                return;
            }

            throw new XBProcessingException("Unexpected token: data", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        @Override
        public void endXBT() throws XBProcessingException, IOException {
            if (activeListener != null) {
                activeListener.endXBT();
                if (((XBReceivingFinished) activeListener).isFinished()) {
                    if (processingState == RecvProcessingState.PRESERVE_COUNT) {
                        activeListener = null;
                        processingState = RecvProcessingState.FORMAT_DECLARATION;
                    } else {
                        processingState = RecvProcessingState.ROOT_NODE;
                        activeListener = null;
                    }
                }

                return;
            }

            processingState = RecvProcessingState.END;
        }

        @Override
        public boolean isFinished() {
            return processingState == RecvProcessingState.END || (processingState == RecvProcessingState.FORMAT_DECLARATION && headerMode);
        }
    }

    @Override
    public void serializeRecvToXB(XBTBasicOutputReceivingSerialHandler serializationHandler) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
