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
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.catalog.XBCBlockDecl;
import org.xbup.lib.core.block.declaration.catalog.XBCFormatDecl;
import org.xbup.lib.core.block.declaration.catalog.XBCGroupDecl;
import org.xbup.lib.core.block.declaration.local.XBDFormatDecl;
import org.xbup.lib.core.block.declaration.local.XBDGroupDecl;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.basic.XBTConsumer;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.basic.XBTProvider;
import org.xbup.lib.core.parser.token.event.convert.XBTListenerToEventListener;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.basic.XBTBasicInputSerialHandler;
import org.xbup.lib.core.serial.basic.XBTBasicOutputSerialHandler;
import org.xbup.lib.core.serial.basic.XBTBasicSerializable;
import org.xbup.lib.core.serial.sequence.XBSerialSequence;
import org.xbup.lib.core.serial.sequence.XBTSequenceListenerSerialHandler;
import org.xbup.lib.core.serial.sequence.XBTSequenceProviderSerialHandler;
import org.xbup.lib.core.serial.sequence.XBTSequenceSerialHandler;
import org.xbup.lib.core.serial.sequence.XBTSequenceSerializable;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Representation of declaration block.
 *
 * @version 0.1 wr24.0 2014/10/02
 * @author XBUP Project (http://xbup.org)
 */
public class XBDeclaration implements XBTSequenceSerializable {

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
        this(new XBDFormatDecl(), rootBlock);
        ((XBDFormatDecl) format).getGroups().add(group);
    }

    public XBDeclaration(XBBlockDecl blockDecl) {
        this(blockDecl, null);
    }

    public XBDeclaration(XBBlockDecl block, XBSerializable rootNode) {
        this(new XBDGroupDecl(), rootNode);
        ((XBDGroupDecl) ((XBDFormatDecl) format).getGroups().get(0)).getBlocks().add(block);
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
            List<XBBlockDecl> blocks = catalog.getBlocks(((XBCGroupDecl) groupDecl).getGroupSpec().getParent());
            for (XBBlockDecl block : blocks) {
                group.getBlocks().add((XBCBlockDecl) block);
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
    public void serializeXB(XBTSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
        XBSerialSequence seq = new XBSerialSequence();
        seq.append(getDeclarationSerializationSeq());
        seq.consist(rootBlock);
        serializationHandler.sequenceXB(seq);
        // TODO serialize extensions
    }

    public XBSerialSequence getDeclarationSerializationSeq() throws XBProcessingException, IOException {
        XBSerialSequence seq = new XBSerialSequence();
        seq.setXBBlockType(new XBFixedBlockType(XBBasicBlockType.DECLARATION));
        seq.join(groupsReserved);
        seq.join(preserveCount);
        seq.consist(format);
        return seq;
    }

    public void serializeDeclaration(XBTSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
        serializationHandler.sequenceXB(getDeclarationSerializationSeq());
    }

    public class BasicSerializer implements XBTBasicSerializable {

        @Override
        public void serializeFromXB(XBTBasicInputSerialHandler serializationHandler) throws XBProcessingException, IOException {
            serializationHandler.attachXBTConsumer(new XBTConsumer() {

                @Override
                public void attachXBTProvider(XBTProvider provider) {
                    XBTListenerImpl listener = new XBTListenerImpl();

                    while (listener.parseMode < 7) {
                        try {
                            provider.produceXBT(listener);
                        } catch (XBProcessingException | IOException ex) {
                            Logger.getLogger(XBDeclaration.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

                class XBTListenerImpl implements XBTListener {

                    // TODO remake to enumeration
                    private int parseMode = 0;
                    private int counter = 0;
                    private XBTListener activeListener;
                    private XBTListener formatListener;
                    private XBTListener rootListener;

                    public XBTListenerImpl() {
                    }

                    @Override
                    public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
                        if (counter > 0) {
                            activeListener.beginXBT(terminationMode);
                            return;
                        }
                        if (parseMode == 4) {
                            counter = 1;
                            activeListener = formatListener;
                            activeListener.beginXBT(terminationMode);
                            return;
                        }
                        if (parseMode == 5) {
                            counter = 1;
                            activeListener = rootListener;
                            activeListener.beginXBT(terminationMode);
                            return;
                        }
                        if (parseMode > 0) {
                            throw new XBProcessingException("Unexpected begin", XBProcessingExceptionType.UNEXPECTED_ORDER);
                        }

                        if (rootBlock != null) {
                            throw new UnsupportedOperationException("Not supported yet.");
                            // rootListener = rootBlock.convertFromXBT();
                        }

                        if (format instanceof XBDFormatDecl) {
                            XBTSequenceProviderSerialHandler handler = new XBTSequenceProviderSerialHandler();
                            handler.attachXBTPullProvider(null);
                            ((XBDFormatDecl) format).serializeXB(handler);
                        } else if (format instanceof XBCFormatDecl) {
                            throw new UnsupportedOperationException("Not supported yet.");
                        } else {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        parseMode = 1;
                    }

                    @Override
                    public void typeXBT(XBBlockType type) throws XBProcessingException, IOException {
                        if (counter > 0) {
                            activeListener.typeXBT(type);
                            return;
                        }
                        if (parseMode == 1) {
                            if (type.getAsBasicType() != XBBasicBlockType.DECLARATION) {
                                throw new XBProcessingException("Unexpected block type", XBProcessingExceptionType.BLOCK_TYPE_MISMATCH);
                            }

                            parseMode = 2;
                        } else {
                            throw new XBProcessingException("Unexpected type event", XBProcessingExceptionType.UNEXPECTED_ORDER);
                        }
                    }

                    @Override
                    public void attribXBT(UBNatural value) throws XBProcessingException, IOException {
                        if (counter > 0) {
                            activeListener.attribXBT(value);
                            return;
                        }
                        if (parseMode < 2) {
                            throw new XBProcessingException("Unexpected event order", XBProcessingExceptionType.UNEXPECTED_ORDER);
                        }
                        switch (parseMode) {
                            case 2: {
                                preserveCount = new UBNat32();
                                groupsReserved = new UBNat32(value);
                                parseMode = 3;
                                break;
                            }
                            case 3: {
                                preserveCount = new UBNat32(value);
                                parseMode = 4;
                                break;
                            }
                        }
                    }

                    @Override
                    public void dataXBT(InputStream data) throws XBProcessingException, IOException {
                        if (counter > 0) {
                            activeListener.dataXBT(data);
                            return;
                        }

                        throw new XBProcessingException("Unexpected data block", XBProcessingExceptionType.UNEXPECTED_ORDER);
                    }

                    @Override
                    public void endXBT() throws XBProcessingException, IOException {
                        if (counter > 0) {
                            activeListener.endXBT();
                            counter--;
                            if (counter == 0) {
                                parseMode += 1;
                            }
                            return;
                        }
                        if (parseMode != 6) {
                            throw new XBProcessingException("Unexpected end of block", XBProcessingExceptionType.UNEXPECTED_ORDER);
                        }

                        parseMode = 7;
                    }
                }
            });
        }

        @Override
        public void serializeToXB(XBTBasicOutputSerialHandler serializationHandler) throws XBProcessingException, IOException {
            serializationHandler.attachXBTProvider(new XBTProvider() {

//            private int position = 0;
//            private XBTListener listener;
//            private XBTListener formatListener;
                @Override
                public void produceXBT(final XBTListener listener) {
                    // TODO: Correct later to single event per execution
                    try {
                        listener.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                        listener.typeXBT(new XBFixedBlockType(XBBasicBlockType.DECLARATION));
                        /*            long groupCount = 0;
                         XBFormatDecl format = getFormat();
                         if (format instanceof XBL1CFormatDecl) {
                         Long[] myPath = new Long[((XBL1CFormatDecl) format).getCatalogPath().length];
                         for (int i = 0; i < ((XBL1CFormatDecl) format).getCatalogPath().length; i++) {
                         myPath[i] = new Long(((XBL1CFormatDecl) format).getCatalogPath()[i]);
                         }
                         XBEFormatSpec spec = ((XBL1Catalog) catalog).findFormatSpecByPath(myPath);
                         groupCount = catalog.getSpecManager().getBindsCount(spec);
                         } else {
                         if (format.getGroupsLimit() > 0) {
                         groupCount = format.getGroups().size();
                         } else groupCount = 0;
                         } */
                        listener.attribXBT(new UBNat32(getGroupsReserved())); // GroupsReserver
                        listener.attribXBT(new UBNat32(getPreserveCount())); // PreserveCount

                        if (format instanceof XBDFormatDecl) {
                            XBTSequenceListenerSerialHandler handler = new XBTSequenceListenerSerialHandler();
                            handler.attachXBTEventListener(new XBTListenerToEventListener(listener));
                            ((XBDFormatDecl) format).serializeXB(handler);
                        } else if (format instanceof XBCFormatDecl) {
                            throw new UnsupportedOperationException("Not supported yet.");
                        } else {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        if (rootBlock != null) {
                            if (rootBlock instanceof XBTBasicSerializable) {
                                throw new UnsupportedOperationException("Not supported yet.");
                                // TODO ((XBTBasicSerializable) rootBlock).serializeToXB(serializationHandler);
                                // TODO getRootBlock().convertToXBT().attachXBTListener(listener);
                            }

                            listener.endXBT();
                        }
                    } catch (XBProcessingException | IOException ex) {
                        Logger.getLogger(XBDeclaration.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }
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
