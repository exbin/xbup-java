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
package org.exbin.xbup.core.block.declaration;

import java.io.IOException;
import java.io.InputStream;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.catalog.XBCatalog;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.basic.XBTListener;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.parser.token.XBTTokenType;
import org.exbin.xbup.core.parser.token.convert.XBTListenerToToken;
import org.exbin.xbup.core.parser.token.event.XBTEventListener;
import org.exbin.xbup.core.serial.basic.XBReceivingFinished;

/**
 * Representation of the current declaration typeConvertor for block types.
 *
 * @version 0.1.25 2015/02/26
 * @author ExBin Project (http://exbin.org)
 */
public class XBLevelContext implements XBTListener, XBTEventListener {

    private int depthLevel = 0;
    private XBCatalog catalog = null;
    private XBTypeConvertor typeConvertor = null;
    private XBTypeConvertor parentContext = null;
    private XBDeclaration declaration = null;
    private XBTListener declarationBuilderListener = null;

    public XBLevelContext(XBCatalog catalog, int depthLevel) {
        this(catalog, null, depthLevel);
    }

    public XBLevelContext(XBCatalog catalog, XBTypeConvertor context, int depthLevel) {
        this.catalog = catalog;
        this.depthLevel = depthLevel;
        if (context != null) {
            typeConvertor = context;
        } else {
            typeConvertor = declaration;
        }

//        declaration = new XBDeclaration();
//        declaration.setHeaderMode(true);
//        try {
//            declaration.serializeRecvFromXB(new XBTBasicInputReceivingSerialHandler() {
//
//                @Override
//                public void process(XBTListener listener) {
//                    declarationBuilderListener = listener;
//                }
//            });
//        } catch (XBProcessingException | IOException ex) {
//            Logger.getLogger(XBLevelContext.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public int getDepthLevel() {
        return depthLevel;
    }

    public void setDepthLevel(int depthLevel) {
        this.depthLevel = depthLevel;
    }

    public XBTypeConvertor getContext() {
        return typeConvertor;
    }

    public void setContext(XBContext context) {
        this.typeConvertor = context;
    }

    public boolean isDeclarationFinished() {
        return declarationBuilderListener == null;
    }

    public XBTypeConvertor getParentContext() {
        return parentContext;
    }

    public void setParentContext(XBContext parentContext) {
        this.parentContext = parentContext;
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        declarationBuilderListener.beginXBT(terminationMode);
    }

    @Override
    public void typeXBT(XBBlockType blockType) throws XBProcessingException, IOException {
        declarationBuilderListener.typeXBT(blockType);
    }

    @Override
    public void attribXBT(XBAttribute attribute) throws XBProcessingException, IOException {
        declarationBuilderListener.attribXBT(attribute);
    }

    @Override
    public void dataXBT(InputStream data) throws XBProcessingException, IOException {
        declarationBuilderListener.dataXBT(data);
    }

    @Override
    public void endXBT() throws XBProcessingException, IOException {
        declarationBuilderListener.endXBT();
        verifyDeclarationFinishing();
    }

    @Override
    public void putXBTToken(XBTToken token) throws XBProcessingException, IOException {
        XBTListenerToToken.tokenToListener(token, declarationBuilderListener);
        if (token.getTokenType() == XBTTokenType.END) {
            verifyDeclarationFinishing();
        }
    }

    private void verifyDeclarationFinishing() {
        if (((XBReceivingFinished) declarationBuilderListener).isFinished()) {
            typeConvertor = declaration.generateContext(parentContext, catalog);
            declarationBuilderListener = null;
        }
    }
}
