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
import org.xbup.lib.core.block.XBBlockDataMode;
import org.xbup.lib.core.block.XBTBlock;
import org.xbup.lib.core.parser.XBParserState;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.basic.XBTProvider;

/**
 * Basic object model parser XBUP level 1 document block / tree node.
 *
 * TODO: Iteration instead of recursion
 *
 * @version 0.1.24 2014/09/30
 * @author XBUP Project (http://xbup.org)
 */
public class XBTTreeWriter implements XBTProvider {

    private final XBTBlock source;
    private XBTTreeWriter subProducer = null;
    private int attrPosition = 0;
    private int childPosition = 0;
    private XBParserState state = XBParserState.BLOCK_BEGIN;

    public XBTTreeWriter(XBTBlock source) {
        this.source = source;
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
                listener.beginXBT(source.getTerminationMode());
                state = (source.getDataMode() == XBBlockDataMode.DATA_BLOCK) ? XBParserState.DATA_PART : XBParserState.BLOCK_TYPE;
                break;
            }

            case DATA_PART: {
                listener.dataXBT(source.getData());
                state = XBParserState.BLOCK_END;
                break;
            }

            case BLOCK_TYPE: {
                listener.typeXBT(source.getBlockType());
                state = XBParserState.ATTRIBUTE_PART;
                break;
            }

            case ATTRIBUTE_PART: {
                if (attrPosition < source.getAttributesCount()) {
                    listener.attribXBT(source.getAttribute(attrPosition));
                    attrPosition++;
                    break;
                } else {
                    state = XBParserState.EXTENDED_AREA;
                    // no break
                }
            }

            case EXTENDED_AREA: {
                if (childPosition < source.getChildCount()) {
                    subProducer = new XBTTreeWriter(source.getChildAt(childPosition));
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
