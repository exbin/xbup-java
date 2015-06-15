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
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBParserState;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.basic.convert.XBSkipBlockListener;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBTEditableBlock;
import org.xbup.lib.core.parser.token.XBAttribute;

/**
 * XBUP level 1 convertor from tokens to tree node.
 *
 * @version 0.1.24 2015/05/04
 * @author XBUP Project (http://xbup.org)
 */
public class XBTTreeReader implements XBTListener {

    private XBTEditableBlock target;
    private final boolean recursive;
    private boolean finished;
    private XBParserState parserState;
    private XBSkipBlockListener skipNode;
    private long level;

    public XBTTreeReader(XBTEditableBlock target) {
        this(target, true);
    }

    public XBTTreeReader(XBTEditableBlock target, boolean recursive) {
        this.target = target;
        this.recursive = recursive;
        finished = false;
        if (!recursive) {
            skipNode = new XBSkipBlockListener();
        }
        parserState = XBParserState.START;
        level = 0;
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode) throws XBParseException, IOException {
        if (finished) {
            throw new XBParseException("Block already parsed", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        if ((!recursive) && (!skipNode.isSkipped())) {
            skipNode.beginXB(terminationMode);
            return;
        }

        if (parserState == XBParserState.START) {
            if (!recursive) {
                if (target.getDataMode() == XBBlockDataMode.NODE_BLOCK) {
                    target.setAttributesCount(0);
                } else {
                    target.clearData();
                }
            } else {
                target.clear();
            }
            target.setTerminationMode(terminationMode);
            parserState = XBParserState.BLOCK_BEGIN;
        } else if (parserState == XBParserState.ATTRIBUTE_PART) {
            if (!recursive) {
                throw new XBParseException("Parser nonvalid state", XBProcessingExceptionType.UNKNOWN);
            }
            XBTEditableBlock node = (XBTEditableBlock) target.createNewChild(target.getChildrenCount());
            node.setTerminationMode(terminationMode);
            level++;
            target = node;
            parserState = XBParserState.START;
        } else {
            throw new XBParseException("Unexpected block begin event", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public void typeXBT(XBBlockType type) throws XBParseException, IOException {
        target.setBlockType(type);
    }

    @Override
    public void attribXBT(XBAttribute value) throws XBParseException, IOException {
        if (finished) {
            throw new XBParseException("Block already parsed", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        if ((!recursive) && (!skipNode.isSkipped())) {
            skipNode.attribXB(value);
            return;
        }

        if (parserState == XBParserState.BLOCK_BEGIN) {
            target.setAttributesCount(0);
            parserState = XBParserState.ATTRIBUTE_PART;
        }

        if (parserState == XBParserState.ATTRIBUTE_PART) {
            target.setAttributeAt(value, target.getAttributesCount());
        } else {
            throw new XBParseException("Unexpected attribute event", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public void dataXBT(InputStream data) throws XBParseException, IOException {
        if (finished) {
            throw new XBParseException("Block already parsed", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        if ((!recursive) && (!skipNode.isSkipped())) {
            skipNode.dataXB(data);
            return;
        }

        if (parserState == XBParserState.BLOCK_BEGIN) {
            parserState = XBParserState.DATA_PART;
            target.setData(data);
        } else {
            throw new XBParseException("Unexpected data event", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }
    }

    @Override
    public void endXBT() throws XBParseException, IOException {
        if (finished) {
            throw new XBParseException("Block already parsed", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        if ((!recursive) && (!skipNode.isSkipped())) {
            skipNode.endXB();
            return;
        }

        if ((parserState == XBParserState.DATA_PART) || (parserState == XBParserState.ATTRIBUTE_PART) || (parserState == XBParserState.BLOCK_BEGIN)) {
            parserState = XBParserState.BLOCK_END;
            if (level > 0) {
                parserState = XBParserState.BLOCK_END;
                level--;
                target = (XBTTreeNode) target.getParent();
            }

            if (level == 0) {
                finished = true;
                parserState = XBParserState.EOF;
            }
            return;
        }

        throw new XBParseException("Unexpected block end event", XBProcessingExceptionType.UNEXPECTED_ORDER);
    }

    public boolean isClosed() {
        return finished;
    }
}
