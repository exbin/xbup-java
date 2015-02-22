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
import org.xbup.lib.catalog.XBAECatalog;
import org.xbup.lib.catalog.entity.service.XBEItemService;
import org.xbup.lib.client.stub.XBPXIconStub;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCXFile;
import org.xbup.lib.core.catalog.base.XBCXIcon;
import org.xbup.lib.core.catalog.base.service.XBCItemService;
import org.xbup.lib.core.catalog.base.service.XBCXIconService;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.basic.XBTMatchingProvider;
import org.xbup.lib.core.remote.XBProcedure;
import org.xbup.lib.core.remote.XBServiceServer;
import org.xbup.lib.core.stream.XBInput;
import org.xbup.lib.core.stream.XBOutput;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC skeleton class for XBRXIcon catalog items.
 *
 * @version 0.1.25 2015/02/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBPXIconSkeleton {

    private final XBAECatalog catalog;

    public XBPXIconSkeleton(XBAECatalog catalog) {
        this.catalog = catalog;
    }

    public void registerProcedures(XBServiceServer remoteServer) {
        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXIconStub.OWNER_ICON_PROCEDURE), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                UBNat32 index = (UBNat32) source.matchAttribXBT();
                source.matchEndXBT();
                XBCXIconService iconService = (XBCXIconService) catalog.getCatalogService(XBCXIconService.class);
                XBCXIcon icon = (XBCXIcon) iconService.getItem(index.getLong());
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(icon.getParent().getId()));
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXIconStub.MODE_ICON_PROCEDURE), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                UBNat32 index = (UBNat32) source.matchAttribXBT();
                source.matchEndXBT();
                XBCXIconService iconService = (XBCXIconService) catalog.getCatalogService(XBCXIconService.class);
                XBCXIcon icon = (XBCXIcon) iconService.getItem(index.getLong());
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(icon.getMode().getId()));
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXIconStub.XBINDEX_ICON_PROCEDURE), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                UBNat32 index = (UBNat32) source.matchAttribXBT();
                source.matchEndXBT();
                XBCXIconService iconService = (XBCXIconService) catalog.getCatalogService(XBCXIconService.class);
                XBCXIcon icon = (XBCXIcon) iconService.getItem(index.getLong());
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0)); // icon.getXBIndex()
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXIconStub.DEFAULTITEM_ICON_PROCEDURE), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                UBNat32 index = (UBNat32) source.matchAttribXBT();
                source.matchEndXBT();
                XBCItem item = (XBCItem) itemService.getItem(index.getLong());
                XBCXIconService iconService = (XBCXIconService) catalog.getCatalogService(XBCXIconService.class);
                Long iconId = (long) 0;
                XBCXIcon icon = iconService.getDefaultIcon(item);
                if (icon != null) {
                    iconId = icon.getId();
                }
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(iconId));
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXIconStub.FILE_ICON_PROCEDURE), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                UBNat32 index = (UBNat32) source.matchAttribXBT();
                source.matchEndXBT();
                XBCXIconService iconService = (XBCXIconService) catalog.getCatalogService(XBCXIconService.class);
                XBCXIcon icon = (XBCXIcon) iconService.getItem(index.getLong());
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                if (icon != null) {
                    XBCXFile file = icon.getIconFile();
                    if (file != null) {
                        result.attribXBT(new UBNat32(file.getId()));
                    } else {
                        result.attribXBT(new UBNat32());
                    }
                } else {
                    result.attribXBT(new UBNat32());
                }
                result.endXBT();
            }
        });

    }
}
