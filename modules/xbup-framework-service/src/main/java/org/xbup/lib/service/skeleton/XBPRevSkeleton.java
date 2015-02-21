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
import org.xbup.lib.catalog.entity.XBEItem;
import org.xbup.lib.catalog.entity.XBERev;
import org.xbup.lib.catalog.entity.XBESpec;
import org.xbup.lib.catalog.entity.service.XBEItemService;
import org.xbup.lib.catalog.entity.service.XBERevService;
import org.xbup.lib.catalog.entity.service.XBESpecService;
import org.xbup.lib.client.stub.XBPRevStub;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.block.declaration.local.XBLBlockDecl;
import org.xbup.lib.core.catalog.base.service.XBCItemService;
import org.xbup.lib.core.catalog.base.service.XBCRevService;
import org.xbup.lib.core.catalog.base.service.XBCSpecService;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.basic.XBTMatchingProvider;
import org.xbup.lib.core.remote.XBProcedure;
import org.xbup.lib.core.remote.XBRemoteServer;
import org.xbup.lib.core.stream.XBInput;
import org.xbup.lib.core.stream.XBOutput;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC skeleton class for XBRRev catalog items.
 *
 * @version 0.1.25 2015/02/20
 * @author XBUP Project (http://xbup.org)
 */
public class XBPRevSkeleton {

    private final XBAECatalog catalog;

    public XBPRevSkeleton(XBAECatalog catalog) {
        this.catalog = catalog;
    }

    public void registerProcedures(XBRemoteServer remoteServer) {
        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBPRevStub.FINDREV_SPEC_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                XBERevService revService = (XBERevService) catalog.getCatalogService(XBCRevService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBESpec spec = specService.getItem(index);
                index = source.matchAttribXBT().getNaturalLong();
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                XBERev rev = (XBERev) revService.findRevByXB(spec, index);
                if (rev != null) {
                    result.attribXBT(new UBNat32(rev.getId()));
                } else {
                    result.attribXBT(new UBNat32(0));
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBPRevStub.REVSCOUNT_SPEC_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                XBERevService revService = (XBERevService) catalog.getCatalogService(XBCRevService.class);
                Long index = source.matchAttribXBT().getNaturalLong();
                XBESpec spec = specService.getItem(index);
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(revService.getRevsCount(spec)));
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBPRevStub.REV_SPEC_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
                XBERevService revService = (XBERevService) catalog.getCatalogService(XBCRevService.class);
                Long itemIndex = source.matchAttribXBT().getNaturalLong();
                Long index = source.matchAttribXBT().getNaturalLong();
                source.matchEndXBT();
                XBESpec item = specService.getItem(itemIndex);
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                if (item == null) {
                    result.attribXBT(new UBNat32(0));
                } else {
                    XBERev rev = ((XBERev) revService.getRev(item, index));
                    if (rev != null) {
                        result.attribXBT(new UBNat32(rev.getId()));
                    } else {
                        result.attribXBT(new UBNat32(0));
                    }
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBPRevStub.XBLIMIT_REV_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBEItemService itemService = (XBEItemService) catalog.getCatalogService(XBCItemService.class);
                UBNat32 index = (UBNat32) source.matchAttribXBT();
                source.matchEndXBT();
                XBEItem item = itemService.getItem(index.getLong());
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                if (item != null) {
                    Long xbLimit = ((XBERev) item).getXBLimit();
                    if (xbLimit != null) {
                        result.attribXBT(new UBNat32(xbLimit));
                    } else {
                        result.attribXBT(new UBNat32(1));
                    }
                } else {
                    result.attribXBT(new UBNat32(1));
                }
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(new XBLBlockDecl(XBPRevStub.REVSCOUNT_REV_PROCEDURE)), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                XBERevService revService = (XBERevService) catalog.getCatalogService(XBCRevService.class);
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(revService.getItemsCount()));
                result.endXBT();
            }
        });

    }
}
