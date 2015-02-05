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
package org.xbup.lib.core.parser.token.pull.convert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFBlockType;
import org.xbup.lib.core.block.declaration.XBContext;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.block.declaration.XBLevelContext;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.token.XBTBeginToken;
import org.xbup.lib.core.parser.token.XBTDataToken;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTokenType;
import org.xbup.lib.core.parser.token.XBTTypeToken;
import org.xbup.lib.core.parser.token.convert.XBTListenerToToken;
import org.xbup.lib.core.parser.token.pull.XBTPullFilter;
import org.xbup.lib.core.parser.token.pull.XBTPullProvider;
import org.xbup.lib.core.util.CopyStreamUtils;

/**
 * Filter to convert block types from fixed types to stand-alone types.
 *
 * @version 0.1.25 2015/02/04
 * @author XBUP Project (http://xbup.org)
 */
public class XBTPullTypeDeclaringFilter implements XBTPullFilter {

    private XBTPullProvider pullProvider;
    private final List<XBLevelContext> contexts = new ArrayList<>();
    private XBLevelContext currentContext = null;

    private int documentDepth = 0;
    private XBBlockTerminationMode beginTerminationMode;

    public XBTPullTypeDeclaringFilter() {
    }

    public XBTPullTypeDeclaringFilter(XBTPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    public XBTPullTypeDeclaringFilter(XBContext initialContext) {
        currentContext = new XBLevelContext(initialContext, 0);
    }

    public XBTPullTypeDeclaringFilter(XBTPullProvider pullProvider, XBContext initialContext) {
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
                CopyStreamUtils.copyInputStreamToOutputStream(inputStream, dataStream);
                currentContext.dataXBT(new ByteArrayInputStream(dataStream.toByteArray()));
                token = new XBTDataToken(new ByteArrayInputStream(dataStream.toByteArray()));
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
                    currentContext = new XBLevelContext(currentContext == null ? null : currentContext.getContext(), documentDepth);
                    currentContext.beginXBT(beginTerminationMode);
                    currentContext.typeXBT(blockType);
                } else {
                    if (blockType instanceof XBFBlockType) {
                        if (((XBFBlockType) blockType).getGroupID().isZero()) {
                            // TODO Or convert using catalog?
                            return token;
                        }

                        XBDeclBlockType declType = currentContext.getContext().getDeclBlockType((XBFBlockType) blockType);
                        if (declType == null) {
                            throw new XBProcessingException("Unable to match block type", XBProcessingExceptionType.BLOCK_TYPE_MISMATCH);
                        }

                        return new XBTTypeToken(declType);
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
