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
import org.exbin.xbup.core.block.XBBlockDataMode;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.block.XBTDefaultDocument;
import org.exbin.xbup.core.block.XBTDocument;
import org.exbin.xbup.core.parser.XBParserState;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.XBTListener;
import org.exbin.xbup.core.parser.basic.XBTProvider;

/**
 * Basic object model parser XBUP level 1 document block / tree node.
 *
 * @version 0.1.25 2015/08/12
 * @author ExBin Project (http://exbin.org)
 */
public class XBTTreeWriter implements XBTProvider {

    private final XBTDocument source;
    private XBTBlock block;

    private XBParserState state = XBParserState.BLOCK_BEGIN;
    private int attrPosition = 0;
    private int childPosition = 0;
    private XBTTreeWriter subProducer = null;

    public XBTTreeWriter(XBTDocument source) {
        this.source = source;
        this.block = source.getRootBlock().orElse(null);
    }

    public XBTTreeWriter(XBTBlock sourceBlock) {
        this(new XBTDefaultDocument(sourceBlock));
    }

    @Override
    public void produceXBT(XBTListener listener) throws XBProcessingException, IOException {
        if (subProducer != null) {
            if (!subProducer.isFinished()) {
                subProducer.produceXBT(listener);
                return;
            } else {
                subProducer = null;
            }
        }

        switch (state) {
            case BLOCK_BEGIN: {
                listener.beginXBT(block.getTerminationMode());
                state = (block.getDataMode() == XBBlockDataMode.DATA_BLOCK) ? XBParserState.DATA_PART : XBParserState.BLOCK_TYPE;
                break;
            }

            case DATA_PART: {
                listener.dataXBT(block.getData());
                state = XBParserState.BLOCK_END;
                break;
            }

            case BLOCK_TYPE: {
                listener.typeXBT(block.getBlockType());
                state = XBParserState.ATTRIBUTE_PART;
                break;
            }

            case ATTRIBUTE_PART: {
                if (attrPosition < block.getAttributesCount()) {
                    listener.attribXBT(block.getAttributeAt(attrPosition));
                    attrPosition++;
                    break;
                } else {
                    state = XBParserState.CHILDREN_PART;
                    // no break
                }
            }

            case CHILDREN_PART: {
                if (childPosition < block.getChildrenCount()) {
                    subProducer = new XBTTreeWriter(block.getChildAt(childPosition));
                    childPosition++;
                    subProducer.produceXBT(listener);
                    break;
                } else {
                    state = XBParserState.BLOCK_END;
                    // no break
                }
            }

            case BLOCK_END: {
                listener.endXBT();
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

    public boolean isFinished() {
        return state == XBParserState.EOF;
    }
}
