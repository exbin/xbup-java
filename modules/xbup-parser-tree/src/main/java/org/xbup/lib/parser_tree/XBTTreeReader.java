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
import java.io.InputStream;
import org.xbup.lib.core.block.XBBlockDataMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBParserState;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.basic.convert.XBSkipBlockListener;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBTDefaultDocument;
import org.xbup.lib.core.block.XBTDocument;
import org.xbup.lib.core.block.XBTEditableBlock;
import org.xbup.lib.core.block.XBTEditableDocument;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.type.XBData;

/**
 * XBUP level 1 convertor from tokens to tree node.
 *
 * @version 0.1.24 2015/08/14
 * @author XBUP Project (http://xbup.org)
 */
public class XBTTreeReader implements XBTListener {

    private XBTDocument target;
    private XBTEditableBlock block;
    private final boolean recursive;
    private final boolean allowExtendedArea;
    private boolean finished;
    private XBParserState parserState;
    private XBSkipBlockListener skipNode;
    private long level;

    public XBTTreeReader(XBTDocument target) {
        this(target, true, true);
    }

    public XBTTreeReader(XBTDocument target, boolean recursive, boolean allowExtendedArea) {
        this.target = target;
        block = (XBTEditableBlock) target.getRootBlock();
        this.recursive = recursive;
        this.allowExtendedArea = allowExtendedArea;
        finished = false;
        if (!recursive) {
            skipNode = new XBSkipBlockListener();
        }

        parserState = XBParserState.START;
        level = 0;
    }

    public XBTTreeReader(XBTEditableBlock block) {
        this(new XBTDefaultDocument(block), true, false);
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
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
        } else if (parserState == XBParserState.ATTRIBUTE_PART || parserState == XBParserState.BLOCK_TYPE || parserState == XBParserState.BLOCK_END) {
            if (!recursive) {
                throw new XBParseException("Parser invalid state", XBProcessingExceptionType.UNKNOWN);
            }
            XBTEditableBlock node = (XBTEditableBlock) block.createNewChild(block.getChildrenCount());
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
    public void typeXBT(XBBlockType type) throws XBProcessingException, IOException {
        if (parserState != XBParserState.BLOCK_BEGIN) {
            throw new XBParseException("Unexpected type", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        parserState = XBParserState.BLOCK_TYPE;
        block.setBlockType(type);
    }

    @Override
    public void attribXBT(XBAttribute value) throws XBProcessingException, IOException {
        if (finished) {
            throw new XBParseException("Block already parsed", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        if ((!recursive) && (!skipNode.isSkipped())) {
            skipNode.attribXB(value);
            return;
        }

        if (parserState == XBParserState.BLOCK_BEGIN || parserState == XBParserState.BLOCK_TYPE) {
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
    public void dataXBT(InputStream data) throws XBProcessingException, IOException {
        if (finished) {
            throw new XBParseException("Block already parsed", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        if ((!recursive) && (!skipNode.isSkipped())) {
            skipNode.dataXB(data);
            return;
        }

        if (level == 0 && (parserState == XBParserState.ATTRIBUTE_PART || parserState == XBParserState.BLOCK_TYPE || parserState == XBParserState.DATA_PART || parserState == XBParserState.BLOCK_END)) {
            if (allowExtendedArea) {
                ((XBTEditableDocument) target).setExtendedArea(data);
                parserState = XBParserState.EXTENDED_AREA;
            } else {
                throw new XBParseException("Unexpected data event for extended area", XBProcessingExceptionType.UNEXPECTED_ORDER);
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
    public void endXBT() throws XBProcessingException, IOException {
        if (finished) {
            throw new XBParseException("Block already parsed", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        if ((!recursive) && (!skipNode.isSkipped())) {
            skipNode.endXB();
            return;
        }

        if (parserState == XBParserState.DATA_PART || parserState == XBParserState.ATTRIBUTE_PART || parserState == XBParserState.BLOCK_TYPE || parserState == XBParserState.BLOCK_END || parserState == XBParserState.EXTENDED_AREA) {
            parserState = XBParserState.BLOCK_END;
            if (level > 0) {
                level--;
                block = (XBTEditableBlock) block.getParent();
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
