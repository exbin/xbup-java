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
package org.xbup.lib.core.parser.token.pull;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.SortedMap;
import java.util.TreeMap;
import org.xbup.lib.core.block.declaration.XBContext;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.token.XBAttributeToken;
import org.xbup.lib.core.parser.token.XBBeginToken;
import org.xbup.lib.core.parser.token.XBDataToken;
import org.xbup.lib.core.parser.token.XBTAttributeToken;
import org.xbup.lib.core.parser.token.XBTBeginToken;
import org.xbup.lib.core.parser.token.XBTDataToken;
import org.xbup.lib.core.parser.token.XBTEndToken;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBToken;
import org.xbup.lib.core.stream.XBTokenInputStream;

/**
 * XBUP level 1 pull reader.
 *
 * @version 0.1.23 2014/02/22
 * @author XBUP Project (http://xbup.org)
 */
public class XBTPullReader extends XBTokenInputStream implements Closeable, XBTPullProvider {

    private final XBPullReader pullReader;

    // Structure for typeTarget
    private class TypeTargetItem {

        public int skip;
        public XBContext context;
    };

    private final XBCatalog catalog;

    // List of TypeContexts for current tree path levels.
    private final SortedMap<Integer, XBContext> typeMap;
    // List of TypeContexts for later use.
    private final SortedMap<Integer, TypeTargetItem> typeTarget;

    private XBContext currentContext;
    private int currentGroup;
    private int currentType;
    private int attrMode;

    public XBTPullReader(XBCatalog catalog) {
        this.catalog = catalog;
        pullReader = new XBPullReader();
        typeMap = new TreeMap<>();
        typeTarget = new TreeMap<>();
        resetParser();
    }

    /**
     * Reset source and all parsing properties.
     */
    private void resetParser() {
        pullReader.reset();
        typeMap.clear();
        currentContext = catalog.getRootContext();
        typeTarget.clear();
    }

    @Override
    public void reset() {
        resetParser();
    }

    /**
     * Open byte input stream.
     *
     * @param stream
     * @throws IOException
     */
    public void open(InputStream stream) throws IOException {
        pullReader.open(stream);
    }

    @Override
    public void close() throws IOException {
        pullReader.close();
    }

    @Override
    public boolean finished() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void skip(long tokenCount) throws XBProcessingException, IOException {
        for (long i = 0; i < tokenCount; i++) {
            pullXBToken();
        }
    }

    /**
     * Returns current parsing context.
     *
     * @return current context
     */
    public XBContext getCurrentContext() {
        return currentContext;
    }

    /**
     * Overrides handlings for level 0 pull processing.
     *
     * @return Next XBEvent
     * @throws IOException
     */
    @Override
    public XBToken pullXBToken() throws IOException, XBProcessingException {
        XBToken item = pullReader.pullXBToken();
        switch (item.getTokenType()) {
            case BEGIN: {
                attrMode = 0;
                break;
            }

            case ATTRIBUTE: {
                if (attrMode == 0) {
                    currentGroup = ((XBAttributeToken) item).getAttribute().getInt();
                    attrMode = 1;
                } else if (attrMode == 1) {
                    currentType = ((XBAttributeToken) item).getAttribute().getInt();
                    attrMode = 2;
                    // TODO: Block type processing
                } else {
                    if (currentGroup == 0) {
                        // TODO: Basic blocks processing
                    }

                    attrMode = 3;
                }

                break;
            }

            case DATA:
                break;

            case END: {
                // update type context
                if (typeMap.get(pullReader.getLevel()) != null) {
                    typeMap.remove(pullReader.getLevel());
                    if (typeMap.lastKey() != null) {
                        currentContext = typeMap.get(typeMap.lastKey());
                    } else {
                        currentContext = catalog.getRootContext();
                    }
                }

                break;
            }

            default:
                throw new XBParseException("Unexpected pull item type");
        }
        return item;
    }

    /**
     * Returns next item in XBUP level 1 parsing.
     *
     * @return Next XBL1Event
     */
    @Override
    public XBTToken pullXBTToken() throws IOException, XBProcessingException {
        XBToken item = pullXBToken();
        switch (item.getTokenType()) {
            case BEGIN:
                return new XBTBeginToken(((XBBeginToken) item).getTerminationMode());

            case DATA:
                return new XBTDataToken(((XBDataToken) item).getData());

            case END:
                return new XBTEndToken();

            case ATTRIBUTE: {
                while (attrMode < 2) {
                    pullXBToken();
                }

                if (attrMode == 2) {
                    throw new UnsupportedOperationException("Not supported yet.");
                    // TODO return new XBTTypeToken(currentContext.getBlockType(currentGroup,currentType));
                } else {
                    return new XBTAttributeToken(((XBAttributeToken) item).getAttribute());
                }
            }

            default:
                throw new XBParseException("Unexpected pull item type", XBProcessingExceptionType.UNKNOWN);
        }
    }
}
