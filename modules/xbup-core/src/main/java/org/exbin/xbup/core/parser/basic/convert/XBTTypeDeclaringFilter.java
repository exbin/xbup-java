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
package org.exbin.xbup.core.parser.basic.convert;

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
import org.exbin.xbup.core.parser.basic.XBTFilter;
import org.exbin.xbup.core.parser.basic.XBTListener;
import org.exbin.xbup.core.parser.token.XBAttribute;

/**
 * Filter to convert block types from fixed types to stand-alone declared types.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBTTypeDeclaringFilter implements XBTFilter {

    private XBCatalog catalog = null;
    private XBTListener listener = null;
    private final List<XBLevelContext> contexts = new ArrayList<>();
    private XBLevelContext currentContext = null;

    private int documentDepth = 0;
    private XBBlockTerminationMode beginTerminationMode;

    /**
     * Creates a new instance.
     *
     * @param catalog catalog used for catalog types
     */
    public XBTTypeDeclaringFilter(XBCatalog catalog) {
        this.catalog = catalog;
    }

    /**
     * Creates a new instance.
     *
     * @param initialContext initial context
     * @param catalog catalog used for catalog types
     */
    public XBTTypeDeclaringFilter(XBCatalog catalog, XBContext initialContext) {
        this(catalog);
        currentContext = new XBLevelContext(catalog, initialContext, 0);
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        if (currentContext != null && !currentContext.isDeclarationFinished()) {
            currentContext.beginXBT(terminationMode);
        }

        listener.beginXBT(terminationMode);
        documentDepth++;
        beginTerminationMode = terminationMode;
    }

    @Override
    public void typeXBT(XBBlockType blockType) throws XBProcessingException, IOException {
        if (currentContext != null && !currentContext.isDeclarationFinished()) {
            currentContext.typeXBT(blockType);
        }

        if (blockType.getAsBasicType() == XBBasicBlockType.DECLARATION) {
            documentDepth++;
            contexts.add(currentContext);
            currentContext = new XBLevelContext(catalog, currentContext == null ? null : currentContext.getContext(), documentDepth);
            currentContext.beginXBT(beginTerminationMode);
            currentContext.typeXBT(blockType);
            listener.typeXBT(blockType);
        } else {
            if (blockType instanceof XBFBlockType) {
                if (((XBFBlockType) blockType).getGroupID().isZero()) {
                    if (catalog != null) {
                        listener.typeXBT(catalog.getBasicBlockType(blockType.getAsBasicType()));
                        return;
                    } else {
                        listener.typeXBT(blockType);
                        return;
                    }
                }

                XBDeclBlockType declType = currentContext.getContext().getDeclBlockType((XBFBlockType) blockType);
                if (declType == null) {
                    throw new XBProcessingException("Unable to match block type", XBProcessingExceptionType.BLOCK_TYPE_MISMATCH);
                }
                listener.typeXBT(declType);
            } else {
                listener.typeXBT(blockType);
            }
        }
    }

    @Override
    public void attribXBT(XBAttribute value) throws XBProcessingException, IOException {
        if (currentContext != null && !currentContext.isDeclarationFinished()) {
            currentContext.attribXBT(value);
        }

        listener.attribXBT(value);
    }

    @Override
    public void dataXBT(InputStream data) throws XBProcessingException, IOException {
        if (currentContext != null && !currentContext.isDeclarationFinished()) {
            currentContext.dataXBT(data);
        }

        listener.dataXBT(data);
    }

    @Override
    public void endXBT() throws XBProcessingException, IOException {
        if (currentContext != null && !currentContext.isDeclarationFinished()) {
            currentContext.endXBT();
        }

        listener.endXBT();
        documentDepth--;
        if (currentContext != null && currentContext.getDepthLevel() > documentDepth) {
            currentContext = contexts.size() > 0 ? contexts.remove(contexts.size() - 1) : null;
        }
    }

    @Override
    public void attachXBTListener(XBTListener listener) {
        this.listener = listener;
    }
}
