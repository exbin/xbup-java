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
package org.xbup.lib.core.block.declaration;

import java.io.IOException;
import java.io.InputStream;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTokenType;
import org.xbup.lib.core.parser.token.event.XBTEventListener;
import org.xbup.lib.core.serial.param.XBPProviderSerialHandler;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * Representation of current declaration context for block types.
 *
 * @version 0.1.25 2015/02/03
 * @author XBUP Project (http://xbup.org)
 */
public class XBLevelContext implements XBTListener, XBTEventListener {

    private int depthLevel = 0;
    private XBCatalog catalog = null;
    private XBContext context = null;
    private XBContext parentContext = null;
    private XBDeclaration declaration = null;
    private XBPProviderSerialHandler declarationBuilderListener = null;

    public XBLevelContext(XBCatalog catalog, int depthLevel) {
        this(catalog, null, depthLevel);
    }

    public XBLevelContext(XBCatalog catalog, XBContext initialContext, int depthLevel) {
        this.catalog = catalog;
        this.depthLevel = depthLevel;
        context = initialContext;
    }

    public int getDepthLevel() {
        return depthLevel;
    }

    public void setDepthLevel(int depthLevel) {
        this.depthLevel = depthLevel;
    }

    public XBContext getContext() {
        return context;
    }

    public void setContext(XBContext context) {
        this.context = context;
    }

    public boolean isDeclarationFinished() {
        return declarationBuilderListener == null;
    }

    public XBContext getParentContext() {
        return parentContext;
    }

    public void setParentContext(XBContext parentContext) {
        this.parentContext = parentContext;
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        declarationBuilderListener.putBegin(terminationMode);
    }

    @Override
    public void typeXBT(XBBlockType blockType) throws XBProcessingException, IOException {
        declarationBuilderListener.putType(blockType);
    }

    @Override
    public void attribXBT(UBNatural attribute) throws XBProcessingException, IOException {
        declarationBuilderListener.putAttribute(attribute);
    }

    @Override
    public void dataXBT(InputStream data) throws XBProcessingException, IOException {
        declarationBuilderListener.putData(data);
    }

    @Override
    public void endXBT() throws XBProcessingException, IOException {
        declarationBuilderListener.putEnd();
        verifyDeclarationFinishing();
    }

    @Override
    public void putXBTToken(XBTToken token) throws XBProcessingException, IOException {
        declarationBuilderListener.putToken(token);
        if (token.getTokenType() == XBTTokenType.END) {
            verifyDeclarationFinishing();
        }
    }

    private void verifyDeclarationFinishing() {
        if (declarationBuilderListener.isFinished()) {
            context = declaration.generateContext(context, catalog);
            declarationBuilderListener = null;
        }
    }
}
