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
package org.exbin.xbup.parser_tree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.exbin.xbup.core.block.XBBlock;
import org.exbin.xbup.core.block.XBBlockDataMode;
import org.exbin.xbup.core.block.XBDefaultDocument;
import org.exbin.xbup.core.block.XBDocument;
import org.exbin.xbup.core.parser.XBParserState;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.XBListener;
import org.exbin.xbup.core.parser.basic.XBProducer;
import org.exbin.xbup.core.parser.basic.XBProvider;

/**
 * Basic object model parser XBUP level 0 document block / tree node.
 *
 * @version 0.1.25 2015/07/26
 * @author ExBin Project (http://exbin.org)
 */
public class XBTreeWriter implements XBProvider, XBProducer {

    private final XBDocument source;
    private XBBlock block;

    private XBParserState state = XBParserState.BLOCK_BEGIN;
    private int attributePosition = 0;
    private int childPosition = 0;
    private final List<Integer> childPositions = new ArrayList<>();

    public XBTreeWriter(XBDocument source) {
        this.source = source;
        this.block = source.getRootBlock();
    }

    public XBTreeWriter(XBBlock sourceBlock) {
        this(new XBDefaultDocument(sourceBlock));
    }

    public void produceXB(XBListener listener, boolean recursive) throws XBProcessingException, IOException {
        switch (state) {
            case BLOCK_BEGIN: {
                listener.beginXB(block.getTerminationMode());
                attributePosition = 0;
                childPosition = 0;
                state = (block.getDataMode() == XBBlockDataMode.DATA_BLOCK) ? XBParserState.DATA_PART : XBParserState.ATTRIBUTE_PART;
                break;
            }

            case DATA_PART: {
                listener.dataXB(block.getData());
                state = XBParserState.BLOCK_END;
                break;
            }

            case ATTRIBUTE_PART: {
                if (attributePosition < block.getAttributesCount()) {
                    listener.attribXB(block.getAttributeAt(attributePosition));
                    attributePosition++;
                    break;
                } else {
                    state = XBParserState.CHILDREN_PART;
                    // no break
                }
            }

            case CHILDREN_PART: {
                if (recursive && childPosition < block.getChildrenCount()) {
                    block = block.getChildAt(childPosition);
                    childPositions.add(childPosition + 1);
                    state = XBParserState.BLOCK_BEGIN;
                    produceXB(listener, recursive);
                    break;
                } else {
                    state = XBParserState.BLOCK_END;
                    // no break
                }
            }

            case BLOCK_END: {
                if (!childPositions.isEmpty()) {
                    childPosition = childPositions.remove(childPositions.size() - 1);
                    block = block.getParent();
                    state = XBParserState.CHILDREN_PART;
                    listener.endXB();
                    break;
                } else {
                    if (source.getTailDataSize() > 0) {
                        listener.dataXB(source.getTailData());
                        state = XBParserState.TAIL_DATA;
                        break;
                    }
                }
                // Continuation to next case intended
            }

            case TAIL_DATA: {
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

    @Override
    public void attachXBListener(XBListener listener) {
        try {
            while (!isFinished()) {
                produceXB(listener, true);
            }
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBTreeWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
