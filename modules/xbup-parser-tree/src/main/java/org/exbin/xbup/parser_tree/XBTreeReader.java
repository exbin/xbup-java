/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.parser_tree;

import java.io.IOException;
import java.io.InputStream;
import org.exbin.xbup.core.block.XBBlockDataMode;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBDefaultDocument;
import org.exbin.xbup.core.block.XBDocument;
import org.exbin.xbup.core.block.XBEditableBlock;
import org.exbin.xbup.core.block.XBEditableDocument;
import org.exbin.xbup.core.parser.XBParseException;
import org.exbin.xbup.core.parser.XBParserState;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.XBListener;
import org.exbin.xbup.core.parser.basic.convert.XBSkipBlockListener;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.type.XBData;
import org.exbin.xbup.core.ubnumber.UBNatural;

/**
 * XBUP level 0 convertor from tokens to tree node.
 *
 * @version 0.2.0 2016/09/25
 * @author ExBin Project (http://exbin.org)
 */
public class XBTreeReader implements XBListener {

    private XBDocument target;
    private XBEditableBlock block;
    private final boolean recursive;
    private final boolean allowTailData;
    private boolean finished;
    private XBParserState parserState;
    private XBSkipBlockListener skipNode;
    private long level;

    public XBTreeReader(XBDocument target) {
        this(target, true, true);
    }

    public XBTreeReader(XBDocument target, boolean recursive, boolean allowTailData) {
        this.target = target;
        block = (XBEditableBlock) target.getRootBlock().orElse(null);
        this.recursive = recursive;
        this.allowTailData = allowTailData;
        finished = false;
        if (!recursive) {
            skipNode = new XBSkipBlockListener();
        }

        parserState = XBParserState.START;
        level = 0;
    }

    public XBTreeReader(XBEditableBlock block) {
        this(new XBDefaultDocument(block), true, false);
    }

    @Override
    public void beginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        if (finished) {
            throw new XBParseException("Block already parsed", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        if ((!recursive) && (!skipNode.isSkipped())) {
            skipNode.beginXB(terminationMode);
            return;
        }

        if (parserState == XBParserState.START) {
            if (!recursive) {
                if (block.getDataMode() == XBBlockDataMode.NODE_BLOCK) {
                    block.setAttributesCount(0);
                } else {
                    block.setData(new XBData());
                }
            } else {
                block.clear();
            }

            block.setTerminationMode(terminationMode);
            parserState = XBParserState.BLOCK_BEGIN;
        } else if (parserState == XBParserState.ATTRIBUTE_PART || parserState == XBParserState.BLOCK_END) {
            if (!recursive) {
                throw new XBParseException("Parser invalid state", XBProcessingExceptionType.UNKNOWN);
            }

            XBEditableBlock node = (XBEditableBlock) block.createNewChild(block.getChildrenCount());
            node.setParent(block);
            node.setTerminationMode(terminationMode);
            level++;
            block = node;
            parserState = XBParserState.BLOCK_BEGIN;
        } else {
            throw new XBParseException("Unexpected block begin event", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public void attribXB(XBAttribute value) throws XBProcessingException, IOException {
        if (finished) {
            throw new XBParseException("Block already parsed", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        if ((!recursive) && (!skipNode.isSkipped())) {
            skipNode.attribXB((UBNatural) value);
            return;
        }

        if (parserState == XBParserState.BLOCK_BEGIN) {
            block.setAttributesCount(0);
            parserState = XBParserState.ATTRIBUTE_PART;
        }

        if (parserState == XBParserState.ATTRIBUTE_PART) {
            block.setAttributeAt(value, block.getAttributesCount());
        } else {
            throw new XBParseException("Unexpected attribute event", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public void dataXB(InputStream data) throws XBProcessingException, IOException {
        if (finished) {
            throw new XBParseException("Block already parsed", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        if ((!recursive) && (!skipNode.isSkipped())) {
            skipNode.dataXB(data);
            return;
        }

        if (level == 0 && (parserState == XBParserState.ATTRIBUTE_PART || parserState == XBParserState.DATA_PART || parserState == XBParserState.BLOCK_END)) {
            if (allowTailData) {
                ((XBEditableDocument) target).setTailData(data);
                parserState = XBParserState.TAIL_DATA;
            } else {
                throw new XBParseException("Unexpected data event for tail data", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        } else {
            if (parserState == XBParserState.BLOCK_BEGIN) {
                parserState = XBParserState.DATA_PART;
                block.setDataMode(XBBlockDataMode.DATA_BLOCK);
                block.setData(data);
            } else {
                throw new XBParseException("Unexpected data event", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        }
    }

    @Override
    public void endXB() throws XBProcessingException, IOException {
        if (finished) {
            throw new XBParseException("Block already parsed", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        if ((!recursive) && (!skipNode.isSkipped())) {
            skipNode.endXB();
            return;
        }

        if (parserState == XBParserState.DATA_PART || parserState == XBParserState.ATTRIBUTE_PART || parserState == XBParserState.BLOCK_END || parserState == XBParserState.TAIL_DATA) {
            parserState = XBParserState.BLOCK_END;
            if (level > 0) {
                level--;
                block = (XBEditableBlock) block.getParentBlock().orElse(null);
                return;
            } else {
                finished = true;
                parserState = XBParserState.EOF;
                return;
            }
        }

        throw new XBParseException("Unexpected block end event", XBProcessingExceptionType.UNEXPECTED_ORDER);
    }

    public boolean isClosed() {
        return finished;
    }
}
