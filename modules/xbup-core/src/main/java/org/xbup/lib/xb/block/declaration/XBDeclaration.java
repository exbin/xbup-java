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
package org.xbup.lib.xb.block.declaration;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.xb.block.XBBasicBlockType;
import org.xbup.lib.xb.block.XBBlockType;
import org.xbup.lib.xb.block.XBFixedBlockType;
import org.xbup.lib.xb.catalog.declaration.XBCBlockDecl;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.parser.basic.XBTListener;
import org.xbup.lib.xb.parser.basic.XBTProvider;
import org.xbup.lib.xb.block.XBBlockTerminationMode;
import org.xbup.lib.xb.parser.token.event.convert.XBTListenerToEventListener;
import org.xbup.lib.xb.serial.XBSerialHandler;
import org.xbup.lib.xb.serial.XBSerialMethod;
import org.xbup.lib.xb.serial.XBSerializable;
import org.xbup.lib.xb.serial.XBSerializationFromXB;
import org.xbup.lib.xb.serial.XBSerializationType;
import org.xbup.lib.xb.serial.child.XBTChildProviderSerialHandler;
import org.xbup.lib.xb.serial.sequence.XBTSerialSequenceListenerHandler;
import org.xbup.lib.xb.ubnumber.UBNatural;
import org.xbup.lib.xb.ubnumber.type.UBNat32;

/**
 * XBUP level 1 document head declaration.
 *
 * @version 0.1 wr23.0 2014/03/09
 * @author XBUP Project (http://xbup.org)
 */
public class XBDeclaration implements XBSerializable {

    private XBFormatDecl format;
    private long groupsReserved = 0;
    private long preserveCount = 0;
    private XBSerializationFromXB rootNode;

    /** Try to transform given type to static */
    public XBFixedBlockType toStaticType(XBBlockType type) {
        if (type instanceof XBFixedBlockType) {
            return (XBFixedBlockType) type;
        }
        if (type instanceof XBCBlockDecl) {
/*            Long[] myPath = new Long[((XBCBlockDecl) type).getCatalogPath().length];
            for (int i = 0; i < ((XBCBlockDecl) type).getCatalogPath().length; i++) {
                myPath[i] = new Long(((XBCBlockDecl) type).getCatalogPath()[i]);
            }
            XBFixedBlockType newType = new XBL1DBlockType(myPath);
            if (newType != null) {
                return newType;
            } else {
                return type;
            } */
//        } else {
//            return type;
        } else if (type instanceof XBBlockType) { // TODO: Replace dumb search for Maps
            if (format == null) {
                return null;
            }
            return ((XBFormatDecl) format).toStaticType(type);
        }
        return null;
    };

    /**
     * Returns count of currently allowed groups.
     *
     * return maximal group ID
     */
    public long getGroupsCount() {
        return preserveCount + groupsReserved + 1;
    }

    public XBGroupDecl getGroup(long group) {
        if (group == 0) {
            return null;
        }
//        if ((group <= preserveCount)&&(parent != null)) return parent.getGroup(group);
        if ((group > preserveCount)&&(group < preserveCount + groupsReserved + 1)&&(format != null)) {
            if (group - preserveCount - 1 >= format.getGroups().size()) {
                return null;
            }
            return format.getGroups().get((int) (group - preserveCount - 1));
        }
        return null;
    }

    /**
     * Returns count of currently allowed blocks in group.
     *
     * return maximal block ID
     */
    public long getBlocksCount(int group) {
        return getGroup(group).getBlocks().size();
    }

    /** Get Block Type for specification defined by IDs */
    public XBBlockDecl getBlockType(int group, int block) {
        XBGroupDecl groupDecl = getGroup(group);
        if (groupDecl == null) {
            return null;
        }
        List<XBBlockDecl> blocks = groupDecl.getBlocks();
        if (blocks == null) {
            return null;
        }
        if (block >= blocks.size()) {
            return null;
        }
        return blocks.get(block);
/*        for (Iterator<XBFormatDecl> it = formats.iterator(); it.hasNext();) {
            XBFormatDecl formatDecl = ((XBFormatDecl) it.next());
            if (formatDecl.getGroups().size() < group) {
                group -= formatDecl.getGroups().size();
            } else {
                return formatDecl.getGroups().get((int) group).getBlocks().get(block);
            }
        }
        return null; */
    }

    public XBFormatDecl getFormat() {
        return format;
    }

    public void setFormat(XBFormatDecl format) {
        this.format = format;
    }

    public long getGroupsReserved() {
        return groupsReserved;
    }

    public void setGroupsReserved(long groupsReserved) {
        this.groupsReserved = groupsReserved;
    }

    public long getPreserveCount() {
        return preserveCount;
    }

    public void setPreserveCount(long preserveCount) {
        this.preserveCount = preserveCount;
    }

    public XBSerializationFromXB getRootNode() {
        return rootNode;
    }

    public void setRootNode(XBSerializationFromXB rootNode) {
        this.rootNode = rootNode;
    }

    public XBTProvider convertToXBT() {
        return new XBTProvider() {
            
            private int position = 0;

//            private XBTListener listener;
//            private XBTListener formatListener;

            @Override
            public void produceXBT(XBTListener listener) {
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
                    XBFormatDecl format = getFormat();
                    XBTSerialSequenceListenerHandler handler = new XBTSerialSequenceListenerHandler();
                    // TODO detect serial method
                    handler.attachXBTEventListener(new XBTListenerToEventListener(listener));
                    format.serializeXB(XBSerializationType.TO_XB, 0, handler);
                    if (getRootNode() != null) {
                        // TODO getRootNode().convertToXBT().attachXBTListener(listener);
                    }
                    listener.endXBT();
                } catch (XBProcessingException | IOException ex) {
                    Logger.getLogger(XBDeclaration.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
    }

    public XBTListener convertFromXBT() {
        return new XBTListener() {

            private int parseMode = 0;
            private int counter = 0;
            private XBTListener activeListener;
            private XBTListener formatListener;
            private XBTListener rootListener;

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
                    throw new XBProcessingException("Unexpected begin");
                }
                if (rootNode != null) {
                    throw new UnsupportedOperationException("Not supported yet.");
                    // rootListener = rootNode.convertFromXBT();
                }
                format = new XBFormatDecl();
//                XBL1SerialPullConsumer consumer = new XBL1SerialPullConsumer(format.getXBSerialSequence());
                XBTChildProviderSerialHandler handler = new XBTChildProviderSerialHandler();
                // TODO Pull provider missing
                handler.attachXBTPullProvider(null);
                format.serializeXB(XBSerializationType.FROM_XB, 0, handler);
                parseMode = 1;
            }

            @Override
            public void typeXBT(XBBlockType type) throws XBProcessingException, IOException {
                if (counter > 0) {
                    activeListener.typeXBT(type);
                    return;
                }
                if (parseMode == 1) {
                    if (!((type.getGroupID().getInt() == 0)&&(type.getBlockID().getInt() == 1))) {
                        throw new XBProcessingException("Unexpected block type");
                    }
                    parseMode = 2;
                } else {
                    throw new XBProcessingException("Unexpected type event");
                }
            }

            @Override
            public void attribXBT(UBNatural value) throws XBProcessingException, IOException {
                if (counter > 0) {
                    activeListener.attribXBT(value);
                    return;
                }
                if (parseMode < 2) {
                    throw new XBProcessingException("Unexpected event order");
                }
                switch (parseMode) {
                    case 2: {
                        preserveCount = 0;
                        groupsReserved = value.getInt();
                        parseMode = 3;
                        break;
                    }
                    case 3: {
                        preserveCount = value.getInt();
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
                throw new XBProcessingException("Unexpected data block");
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
                    throw new XBProcessingException("Unexpected end of block");
                }
                parseMode = 7;
            }
        }; 
    }

    @Override
    public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
