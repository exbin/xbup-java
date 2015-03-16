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
package org.xbup.lib.service.skeleton;

import java.io.IOException;
import java.util.List;
import org.xbup.lib.catalog.XBAECatalog;
import org.xbup.lib.catalog.entity.XBEXLanguage;
import org.xbup.lib.catalog.entity.service.XBEXLangService;
import org.xbup.lib.client.stub.XBPXLangStub;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.XBTEmptyBlock;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.remote.XBMultiProcedure;
import org.xbup.lib.core.remote.XBServiceServer;
import org.xbup.lib.core.serial.param.XBPListenerSerialHandler;
import org.xbup.lib.core.serial.param.XBPProviderSerialHandler;
import org.xbup.lib.core.stream.XBInput;
import org.xbup.lib.core.stream.XBOutput;
import org.xbup.lib.core.type.XBString;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC skeleton class for XBRXLanguage catalog items.
 *
 * @version 0.1.25 2015/03/16
 * @author XBUP Project (http://xbup.org)
 */
public class XBPXLangSkeleton {

    private final XBAECatalog catalog;

    public XBPXLangSkeleton(XBAECatalog catalog) {
        this.catalog = catalog;
    }

    public void registerProcedures(XBServiceServer remoteServer) {
        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXLangStub.CODE_LANG_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBEXLangService langService = (XBEXLangService) catalog.getCatalogService(XBEXLangService.class);
                XBEXLanguage lang = langService.getItem(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(lang == null ? XBTEmptyBlock.getEmptyBlock() : new XBString(lang.getLangCode()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXLangStub.DEFAULT_LANG_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                provider.end();

                XBEXLangService langService = (XBEXLangService) catalog.getCatalogService(XBEXLangService.class);
                XBEXLanguage lang = langService.getDefaultLang();

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(lang == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(lang.getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXLangStub.LANGS_LANG_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                provider.end();

                XBEXLangService langService = (XBEXLangService) catalog.getCatalogService(XBEXLangService.class);
                List<XBEXLanguage> items = langService.getAllItems();

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.begin();
                listener.matchType(new XBFixedBlockType());
                listener.putAttribute(items.size());
                for (XBEXLanguage lang : items) {
                    listener.putAttribute(lang.getId());
                }
                listener.end();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXLangStub.LANGSCOUNT_LANG_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                provider.end();

                XBEXLangService langService = (XBEXLangService) catalog.getCatalogService(XBEXLangService.class);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(new UBNat32(langService.getItemsCount()));
            }
        });
    }
}
