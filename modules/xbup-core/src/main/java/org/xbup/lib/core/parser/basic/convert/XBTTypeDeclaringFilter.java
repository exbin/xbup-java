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
package org.xbup.lib.core.parser.basic.convert;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBTFilter;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBFBlockType;
import org.xbup.lib.core.block.declaration.XBContext;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.block.declaration.XBLevelContext;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * Filter to convert block types from fixed types to stand-alone declared types.
 *
 * @version 0.1.25 2015/02/07
 * @author XBUP Project (http://xbup.org)
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
    public void attribXBT(UBNatural value) throws XBProcessingException, IOException {
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
