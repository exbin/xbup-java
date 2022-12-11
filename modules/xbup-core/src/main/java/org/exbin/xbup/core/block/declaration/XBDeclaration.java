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
package org.exbin.xbup.core.block.declaration;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBBasicBlockType;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBDBlockType;
import org.exbin.xbup.core.block.XBFBlockType;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.block.declaration.catalog.XBCFormatDecl;
import org.exbin.xbup.core.block.declaration.local.XBLFormatDecl;
import org.exbin.xbup.core.block.declaration.local.XBLGroupDecl;
import org.exbin.xbup.core.catalog.XBCatalog;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.XBTListener;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.serial.XBSerializable;
import org.exbin.xbup.core.serial.basic.XBReceivingFinished;
import org.exbin.xbup.core.serial.basic.XBTBasicInputReceivingSerialHandler;
import org.exbin.xbup.core.serial.basic.XBTBasicOutputReceivingSerialHandler;
import org.exbin.xbup.core.serial.basic.XBTBasicReceivingSerializable;
import org.exbin.xbup.core.serial.param.XBPSequenceSerialHandler;
import org.exbin.xbup.core.serial.param.XBPSequenceSerializable;
import org.exbin.xbup.core.serial.param.XBSerializationMode;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * Representation of type declaration.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
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
    public XBDeclaration(XBFormatDecl formatDecl, @Nullable XBSerializable rootBlock) {
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
    public XBDeclaration(XBGroupDecl groupDecl, @Nullable XBSerializable rootBlock) {
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
    public XBDeclaration(XBBlockDecl blockDecl, @Nullable XBSerializable rootBlock) {
        this(new XBLGroupDecl(blockDecl), rootBlock);
    }

    public XBDeclaration(XBBlockDecl blockDecl) {
        this(blockDecl, null);
    }

    public XBContext generateContext() {
        return generateContext((XBTypeConvertor) null);
    }

    public XBContext generateContext(@Nullable XBCatalog catalog) {
        return generateContext(null, catalog);
    }

    public XBContext generateContext(@Nullable XBTypeConvertor parentContext) {
        return generateContext(parentContext, null);
    }

    public XBContext generateContext(@Nullable XBTypeConvertor parentContext, @Nullable XBCatalog catalog) {
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

    @Nullable
    public static XBGroup convertCatalogGroup(XBGroupDecl groupDecl) {
        return convertCatalogGroup(groupDecl, null);
    }

    @Nullable
    public static XBGroup convertCatalogGroup(@Nullable XBGroupDecl groupDecl, @Nullable XBCatalog catalog) {
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

    @Nullable
    @Override
    public XBDeclBlockType getDeclBlockType(XBFBlockType fixedType) {
        XBGroup group = getGroupForId(0);
        if (group != null) {
            XBBlockDecl blockDecl = group.getBlockForId(fixedType.getBlockID().getInt());
            return blockDecl != null ? new XBDeclBlockType(blockDecl) : null;
        }

        return null;
    }

    @Nullable
    @Override
    public XBFixedBlockType getFixedBlockType(XBDBlockType declType) {
        return getFixedBlockType(declType.getBlockDecl(), -1);
    }

    @Nullable
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

    @Nullable
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

    @Nonnull
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

    @Nullable
    public XBSerializable getRootBlock() {
        return rootBlock;
    }

    public void setRootBlock(@Nullable XBSerializable rootNode) {
        this.rootBlock = rootNode;
    }

    @Nullable
    public XBDeclaration getParent() {
        return parent;
    }

    public void setParent(@Nullable XBDeclaration parent) {
        this.parent = parent;
    }

    public void realignReservation(@Nullable XBCatalog catalog) {
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

    @ParametersAreNonnullByDefault
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

            if (null != processingState) {
                switch (processingState) {
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
