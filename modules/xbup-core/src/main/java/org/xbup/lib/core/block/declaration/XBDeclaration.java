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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.catalog.XBCFormatDecl;
import org.xbup.lib.core.block.declaration.local.XBDFormatDecl;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.basic.XBTProvider;
import org.xbup.lib.core.parser.token.event.convert.XBTListenerToEventListener;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.sequence.XBTSequenceListenerSerialHandler;
import org.xbup.lib.core.serial.sequence.XBTSequenceProviderSerialHandler;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Representation of declaration block.
 *
 * @version 0.1 wr24.0 2014/08/31
 * @author XBUP Project (http://xbup.org)
 */
public class XBDeclaration implements XBSerializable {

    private XBFormatDecl format;
    private XBSerializable rootNode;

    private int groupsReserved = 0;
    private int preserveCount = 0;

    /**
     * Returns count of currently allowed groups.
     *
     * @return count of groups
     */
    public long getGroupsCount() {
        return preserveCount + groupsReserved + 1;
    }

    /* public XBGroupDecl getGroup(int group) {
        if (group == 0) {
            return null;
        }
//        if ((group <= preserveCount)&&(parent != null)) return parent.getGroup(group);
        if ((group > preserveCount) && (group < preserveCount + groupsReserved + 1) && (format != null)) {
            if (format instanceof XBDFormatDecl) {
                if (group - preserveCount - 1 >= ((XBDFormatDecl) format).getGroups().size()) {
                    return null;
                }

                return ((XBDFormatDecl) format).getGroups().get(group - preserveCount - 1);
            } else if (format instanceof XBCFormatDecl) {
                if (group - preserveCount - 1 >= ((XBCFormatDecl) format).getGroups().size()) {
                    return null;
                }

                return ((XBCFormatDecl) format).getGroups().get(group - preserveCount - 1);
            } else {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }

        return null;
    } */

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

                    if (format instanceof XBDFormatDecl) {
                        XBTSequenceListenerSerialHandler handler = new XBTSequenceListenerSerialHandler();
                        handler.attachXBTEventListener(new XBTListenerToEventListener(listener));
                        ((XBDFormatDecl) format).serializeXB(handler);
                    } else if (format instanceof XBCFormatDecl) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    } else {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    if (rootNode != null) {
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

            // TODO remake to enumeration
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

    public XBFormatDecl getFormat() {
        return format;
    }

    public void setFormat(XBFormatDecl format) {
        this.format = format;
    }

    public int getGroupsReserved() {
        return groupsReserved;
    }

    public void setGroupsReserved(int groupsReserved) {
        this.groupsReserved = groupsReserved;
    }

    public int getPreserveCount() {
        return preserveCount;
    }

    public void setPreserveCount(int preserveCount) {
        this.preserveCount = preserveCount;
    }

    public XBSerializable getRootNode() {
        return rootNode;
    }

    public void setRootNode(XBSerializable rootNode) {
        this.rootNode = rootNode;
    }
}
