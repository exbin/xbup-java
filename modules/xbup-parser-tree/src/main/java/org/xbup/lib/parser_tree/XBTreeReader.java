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
import java.util.ArrayList;
import org.xbup.lib.core.block.XBBlock;
import org.xbup.lib.core.block.XBBlockDataMode;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBParserState;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.convert.XBSkipBlockListener;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 0 convertor from tokens to tree node.
 *
 * @version 0.1.23 2014/02/11
 * @author XBUP Project (http://xbup.org)
 */
public class XBTreeReader implements XBListener {

    private XBTreeNode target;
    private final boolean recursive;
    private boolean finished;
    private XBParserState parserState;
    private XBSkipBlockListener skipNode;
    private long level;

    public XBTreeReader(XBTreeNode target, boolean recursive) {
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
    public void beginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        if (finished) {
            throw new XBParseException("Block already parsed");
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
                    target.setData(null);
                }
            } else {
                target.clear();
            }

            target.setTerminationMode(terminationMode);
            parserState = XBParserState.BLOCK_BEGIN;
        } else if (parserState == XBParserState.ATTRIBUTE_PART || parserState == XBParserState.BLOCK_END) {
            if (!recursive) {
                throw new XBParseException("Parser invalid state");
            }

            if (target.getChildren() == null) {
                target.setChildren(new ArrayList<XBBlock>());
            }

            XBTreeNode node = target.newNodeInstance(target);
            node.setTerminationMode(terminationMode);
            target.getChildren().add(node);
            level++;
            target = node;
            parserState = XBParserState.BLOCK_BEGIN;
        } else {
            throw new XBParseException("Unexpected block begin event");
        }
    }

    @Override
    public void attribXB(UBNatural value) throws XBProcessingException, IOException {
        if (finished) {
            throw new XBParseException("Block already parsed");
        }

        if ((!recursive) && (!skipNode.isSkipped())) {
            skipNode.attribXB((UBNatural) value);
            return;
        }

        if (parserState == XBParserState.BLOCK_BEGIN) {
            target.setAttributesCount(0);
            parserState = XBParserState.ATTRIBUTE_PART;
        }

        if (parserState == XBParserState.ATTRIBUTE_PART) {
            target.addAttribute(new UBNat32(value));
        } else {
            throw new XBParseException("Unexpected attribute event");
        }
    }

    @Override
    public void dataXB(InputStream data) throws XBProcessingException, IOException {
        if (finished) {
            throw new XBParseException("Block already parsed");
        }

        if ((!recursive) && (!skipNode.isSkipped())) {
            skipNode.dataXB(data);
            return;
        }

        if (parserState == XBParserState.BLOCK_BEGIN) {
            parserState = XBParserState.DATA_PART;
            target.setDataMode(XBBlockDataMode.DATA_BLOCK);
            target.setData(data);
        } else {
            throw new XBParseException("Unexpected data event");
        }
    }

    @Override
    public void endXB() throws XBProcessingException, IOException {
        if (finished) {
            throw new XBParseException("Block already parsed");
        }

        if ((!recursive) && (!skipNode.isSkipped())) {
            skipNode.endXB();
            return;
        }

        if ((parserState == XBParserState.DATA_PART) || (parserState == XBParserState.ATTRIBUTE_PART) || (parserState == XBParserState.BLOCK_END)) {
            parserState = XBParserState.BLOCK_END;
            if (level > 0) {
                level--;
                target = target.getParent();
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
