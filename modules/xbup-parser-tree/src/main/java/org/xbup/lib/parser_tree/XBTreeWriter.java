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
package org.xbup.lib.parser_tree;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.block.XBBlock;
import org.xbup.lib.core.block.XBBlockDataMode;
import org.xbup.lib.core.parser.XBParserState;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.XBProvider;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Basic object model parser XBUP level 0 document block / tree node.
 *
 * @version 0.1.25 2015/06/22
 * @author XBUP Project (http://xbup.org)
 */
public class XBTreeWriter implements XBProvider {

    private final XBBlock source;

    private XBParserState state = XBParserState.BLOCK_BEGIN;
    private int attrPosition = 0;
    private int childPosition = 0;
    private XBTreeWriter subProducer = null;

    public XBTreeWriter(XBBlock source) {
        this.source = source;
    }

    public void produceXB(XBListener listener, boolean recursive) throws XBProcessingException, IOException {
        if (subProducer != null) {
            if (!subProducer.isFinished()) {
                subProducer.produceXB(listener);
                return;
            } else {
                subProducer = null;
            }
        }

        switch (state) {
            case BLOCK_BEGIN: {
                listener.beginXB(source.getTerminationMode());
                state = (source.getDataMode() == XBBlockDataMode.DATA_BLOCK) ? XBParserState.DATA_PART : XBParserState.ATTRIBUTE_PART;
                break;
            }

            case DATA_PART: {
                listener.dataXB(source.getData());
                state = XBParserState.BLOCK_END;
                break;
            }

            case ATTRIBUTE_PART: {
                if (attrPosition < source.getAttributesCount()) {
                    listener.attribXB(source.getAttributeAt(attrPosition));
                    attrPosition++;
                    break;
                } else {
                    state = XBParserState.CHILDREN_PART;
                    // no break
                }
            }

            case CHILDREN_PART: {
                if (recursive && childPosition < source.getChildrenCount()) {
                    subProducer = new XBTreeWriter(source.getChildAt(childPosition));
                    childPosition++;
                    subProducer.produceXB(listener);
                    break;
                } else {
                    state = XBParserState.BLOCK_END;
                    // no break
                }
            }

            case BLOCK_END: {
                listener.endXB();
                state = XBParserState.EOF;
                break;
            }

            case EOF: {
                throw new XBProcessingException("Reading after block end", XBProcessingExceptionType.READING_AFTER_END);
            }

            default: {
                throw new XBProcessingException("Unexpected state", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        }
    }

    @Override
    public void produceXB(XBListener listener) {
        try {
            produceXB(listener, true);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBTreeNode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isFinished() {
        return state == XBParserState.EOF;
    }

    // @Override
    public void attachXBListener(XBListener listener) {
        try {
            generateXB(listener, true);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBTreeNode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void generateXB(XBListener listener, boolean recursive) throws XBProcessingException, IOException {
        listener.beginXB(source.getTerminationMode());
        if (source.getDataMode() == XBBlockDataMode.DATA_BLOCK) {
            listener.dataXB(source.getData());
        } else {
            XBAttribute[] attributes = source.getAttributes();
            for (XBAttribute attribute : attributes) {
                listener.attribXB(new UBNat32(attribute.getNaturalLong()));
            }

            if (recursive && (source.getChildren() != null)) {
                XBBlock[] children = source.getChildren();
                for (XBBlock child : children) {
                    XBTreeWriter subWriter = new XBTreeWriter(child);
                    subWriter.attachXBListener(listener);
                }
            }
        }

        listener.endXB();
    }
}
