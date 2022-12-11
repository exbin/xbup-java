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
 * @author ExBin Project (https://exbin.org)
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
