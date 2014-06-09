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
package org.xbup.lib.xb.parser.tree;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.xbup.lib.xb.block.XBBlockDataMode;
import org.xbup.lib.xb.block.XBBlockType;
import org.xbup.lib.xb.block.XBTBlock;
import org.xbup.lib.xb.parser.XBParseException;
import org.xbup.lib.xb.parser.XBParserState;
import org.xbup.lib.xb.parser.XBProcessingExceptionType;
import org.xbup.lib.xb.parser.basic.XBTListener;
import org.xbup.lib.xb.parser.basic.convert.XBEventSkipNode;
import org.xbup.lib.xb.block.XBBlockTerminationMode;
import org.xbup.lib.xb.ubnumber.UBNatural;
import org.xbup.lib.xb.ubnumber.type.UBNat32;

/**
 * XBUP level 1 convertor from tokens to tree node.
 *
 * @version 0.1 wr23.0 2014/02/20
 * @author XBUP Project (http://xbup.org)
 */
public class XBTTreeReader implements XBTListener {

    private XBTTreeNode target;
    private final boolean recursive;
    private boolean finished;
    private XBParserState parserState;
    private XBEventSkipNode skipNode;
    private long level;

    public XBTTreeReader(XBTTreeNode target, boolean recursive) {
        this.target = target;
        this.recursive = recursive;
        finished = false;
        if (!recursive) {
            skipNode = new XBEventSkipNode();
        }
        parserState = XBParserState.START;
        level = 0;
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode) throws XBParseException, IOException {
        if (finished) {
            throw new XBParseException("Block already parsed");
        }

        if ((!recursive) && (!skipNode.isClosed())) {
            skipNode.beginXB(terminationMode);
            return;
        }

        if (parserState == XBParserState.START) {
            if (!recursive) {
                if (target.getDataMode() == XBBlockDataMode.NODE_BLOCK) {
                    if (target.getAttributes() != null) {
                        target.getAttributes().clear();
                    }
                } else {
                    target.setData(null);
                }
            } else {
                target.clear();
            }
            target.setTerminationMode(terminationMode);
            parserState = XBParserState.BLOCK_BEGIN;
        } else if (parserState == XBParserState.ATTRIBUTE_PART) {
            if (!recursive) {
                throw new XBParseException("Parser nonvalid state");
            }
            if (target.getChildren() == null) {
                target.setChildren(new ArrayList<XBTBlock>());
            }
            XBTTreeNode node = (XBTTreeNode) target.newNodeInstance(null);
            node.setTerminationMode(terminationMode);
            target.getChildren().add(node);
            level++;
            target = node;
            parserState = XBParserState.START;
        } else {
            throw new XBParseException("Unexpected block begin event");
        }
    }

    @Override
    public void typeXBT(XBBlockType type) throws XBParseException, IOException {
        target.setBlockType(type);
    }

    @Override
    public void attribXBT(UBNatural value) throws XBParseException, IOException {
        if (finished) {
            throw new XBParseException("Block already parsed");
        }

        if ((!recursive) && (!skipNode.isClosed())) {
            skipNode.attribXB(value);
            return;
        }

        if (parserState == XBParserState.BLOCK_BEGIN) {
            if (target.getAttributes() == null) {
                target.setAttributes(new ArrayList<UBNatural>());
            }
            parserState = XBParserState.ATTRIBUTE_PART;
        }

        if (parserState == XBParserState.ATTRIBUTE_PART) {
            target.getAttributes().add(new UBNat32(value));
        } else {
            throw new XBParseException("Unexpected attribute event");
        }
    }

    @Override
    public void dataXBT(InputStream data) throws XBParseException, IOException {
        if (finished) {
            throw new XBParseException("Block already parsed");
        }

        if ((!recursive) && (!skipNode.isClosed())) {
            skipNode.dataXB(data);
            return;
        }

        if (parserState == XBParserState.BLOCK_BEGIN) {
            parserState = XBParserState.DATA;
            target.setData(data);
        } else {
            throw new XBParseException("Unexpected data event");
        }
    }

    @Override
    public void endXBT() throws XBParseException, IOException {
        if (finished) {
            throw new XBParseException("Block already parsed");
        }

        if ((!recursive) && (!skipNode.isClosed())) {
            skipNode.endXB();
            return;
        }

        if ((parserState == XBParserState.DATA) || (parserState == XBParserState.BLOCK_BEGIN)) {
            parserState = XBParserState.BLOCK_END;
            if (level > 0) {
                parserState = XBParserState.BLOCK_END;
                level--;
                target = (XBTTreeNode) target.getParent();
                if (level == 0) {
                    finished = true;
                    parserState = XBParserState.EOF;
                }
                return;
            }
        }

        throw new XBParseException("Unexpected block end event", XBProcessingExceptionType.UNEXPECTED_ORDER);
    }

    public boolean isClosed() {
        return finished;
    }
}
