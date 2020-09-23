/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.block.declaration;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
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
import org.exbin.xbup.core.serial.basic.XBTBasicInputReceivingSerialHandler;

/**
 * Representation of the current declaration typeConvertor for block types.
 *
 * @version 0.2.0 2017/01/20
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBLevelContext implements XBTListener, XBTEventListener {

    private int depthLevel = 0;
    private XBCatalog catalog = null;
    private XBTypeConvertor typeConvertor = null;
    private XBTypeConvertor parentContext = null;
    private XBDeclaration declaration = null;
    private XBTListener declarationBuilderListener = null;

    public XBLevelContext(XBCatalog catalog, int depthLevel) {
        this(catalog, null, depthLevel, true);
    }

    public XBLevelContext(XBCatalog catalog, @Nullable XBTypeConvertor context, int depthLevel) {
        this(catalog, context, depthLevel, true);
    }

    public XBLevelContext(XBCatalog catalog, @Nullable XBTypeConvertor context, int depthLevel, boolean useDeclaration) {
        this.catalog = catalog;
        this.depthLevel = depthLevel;
        if (context != null) {
            typeConvertor = context;
        } else {
            typeConvertor = declaration;
        }

        if (useDeclaration) {
            declaration = new XBDeclaration();
            declaration.setHeaderMode(true);
            try {
                declaration.serializeRecvFromXB(new XBTBasicInputReceivingSerialHandler() {

                    @Override
                    public void process(XBTListener listener) {
                        declarationBuilderListener = listener;
                    }
                });
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBLevelContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public int getDepthLevel() {
        return depthLevel;
    }

    public void setDepthLevel(int depthLevel) {
        this.depthLevel = depthLevel;
    }

    @Nullable
    public XBTypeConvertor getContext() {
        return typeConvertor;
    }

    public void setContext(@Nullable XBContext context) {
        this.typeConvertor = context;
    }

    public boolean isDeclarationFinished() {
        return declarationBuilderListener == null;
    }

    @Nullable
    public XBTypeConvertor getParentContext() {
        return parentContext;
    }

    public void setParentContext(@Nullable XBContext parentContext) {
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

    public void replaceCatalog(XBCatalog catalog, XBContext rootContext) {
        this.catalog = catalog;
    }
}
