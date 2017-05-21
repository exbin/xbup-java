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
package org.exbin.xbup.core.parser.token.pull.convert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.exbin.xbup.core.block.XBBasicBlockType;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBFBlockType;
import org.exbin.xbup.core.block.declaration.XBContext;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.block.declaration.XBLevelContext;
import org.exbin.xbup.core.catalog.XBCatalog;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.token.XBTBeginToken;
import org.exbin.xbup.core.parser.token.XBTDataToken;
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.parser.token.XBTTokenType;
import org.exbin.xbup.core.parser.token.XBTTypeToken;
import org.exbin.xbup.core.parser.token.convert.XBTListenerToToken;
import org.exbin.xbup.core.parser.token.pull.XBTPullFilter;
import org.exbin.xbup.core.parser.token.pull.XBTPullProvider;
import org.exbin.xbup.core.util.StreamUtils;

/**
 * Filter to convert block types from fixed types to stand-alone types.
 *
 * TODO: Drop
 *
 * @version 0.2.0 2017/01/20
 * @author ExBin Project (http://exbin.org)
 */
public class XBTPullTypeDeclaringFilterNoDeclaration implements XBTPullFilter {

    private XBTPullProvider pullProvider;
    private XBCatalog catalog;
    private final List<XBLevelContext> contexts = new ArrayList<>();
    private XBLevelContext currentContext = null;

    private int documentDepth = 0;
    private XBBlockTerminationMode beginTerminationMode;

    public XBTPullTypeDeclaringFilterNoDeclaration(XBCatalog catalog) {
        this(catalog, null);
    }

    public XBTPullTypeDeclaringFilterNoDeclaration(XBCatalog catalog, XBTPullProvider pullProvider) {
        this.catalog = catalog;
        currentContext = new XBLevelContext(catalog, catalog.getRootContext(), 0, false);
        this.pullProvider = pullProvider;
    }

    public XBTPullTypeDeclaringFilterNoDeclaration(XBContext initialContext) {
        currentContext = new XBLevelContext(catalog, initialContext, 0, false);
    }

    public XBTPullTypeDeclaringFilterNoDeclaration(XBContext initialContext, XBTPullProvider pullProvider) {
        this(initialContext);
        this.pullProvider = pullProvider;
    }

    @Override
    public XBTToken pullXBTToken() throws XBProcessingException, IOException {
        XBTToken token = pullProvider.pullXBTToken();
        if (currentContext != null && !currentContext.isDeclarationFinished()) {
            if (token.getTokenType() == XBTTokenType.DATA) {
                InputStream inputStream = ((XBTDataToken) token).getData();
                ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
                StreamUtils.copyInputStreamToOutputStream(inputStream, dataStream);
                currentContext.dataXBT(new ByteArrayInputStream(dataStream.toByteArray()));
                token = XBTDataToken.create(new ByteArrayInputStream(dataStream.toByteArray()));
            } else {
                XBTListenerToToken.tokenToListener(token, currentContext);
            }
        }

        switch (token.getTokenType()) {
            case BEGIN: {
                documentDepth++;
                beginTerminationMode = ((XBTBeginToken) token).getTerminationMode();

                return token;
            }
            case TYPE: {
                XBBlockType blockType = ((XBTTypeToken) token).getBlockType();
                if (blockType.getAsBasicType() == XBBasicBlockType.DECLARATION) {
                    documentDepth++;
                    contexts.add(currentContext);
                    currentContext = new XBLevelContext(catalog, currentContext == null ? null : currentContext.getContext(), documentDepth, false);
                    currentContext.beginXBT(beginTerminationMode);
                    currentContext.typeXBT(blockType);
                } else {
                    if (blockType instanceof XBFBlockType) {
                        if (((XBFBlockType) blockType).getGroupID().isNaturalZero()) {
                            if (catalog != null) {
                                return XBTTypeToken.create(catalog.getBasicBlockType(blockType.getAsBasicType()));
                            } else {
                                return token;
                            }
                        }

                        XBDeclBlockType declType = currentContext.getContext().getDeclBlockType((XBFBlockType) blockType);
                        if (declType == null) {
                            throw new XBProcessingException("Unable to match block type", XBProcessingExceptionType.BLOCK_TYPE_MISMATCH);
                        }

                        return XBTTypeToken.create(declType);
                    }
                }

                return token;
            }
            case ATTRIBUTE: {
                return token;
            }
            case DATA: {
                return token;
            }
            case END: {
                documentDepth--;
                if (currentContext != null && currentContext.getDepthLevel() > documentDepth) {
                    currentContext = contexts.size() > 0 ? contexts.remove(contexts.size() - 1) : null;
                }

                return token;
            }
        }

        throw new IllegalStateException("Unexpected token type " + token.getTokenType().toString());
    }

    @Override
    public void attachXBTPullProvider(XBTPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }
}
