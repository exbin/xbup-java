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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import org.xbup.lib.catalog.XBAECatalog;
import org.xbup.lib.catalog.entity.XBENode;
import org.xbup.lib.catalog.entity.XBEXFile;
import org.xbup.lib.catalog.entity.service.XBENodeService;
import org.xbup.lib.catalog.entity.service.XBEXFileService;
import org.xbup.lib.client.stub.XBPXFileStub;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.XBTEmptyBlock;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.XBCXFile;
import org.xbup.lib.core.catalog.base.service.XBCNodeService;
import org.xbup.lib.core.catalog.base.service.XBCXFileService;
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
 * RPC skeleton class for XBRXFile catalog items.
 *
 * @version 0.1.25 2015/03/16
 * @author XBUP Project (http://xbup.org)
 */
public class XBPXFileSkeleton {

    private final XBAECatalog catalog;

    public XBPXFileSkeleton(XBAECatalog catalog) {
        this.catalog = catalog;
    }

    public void registerProcedures(XBServiceServer remoteServer) {
        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXFileStub.OWNER_FILE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBEXFileService fileService = (XBEXFileService) catalog.getCatalogService(XBCXFileService.class);
                XBEXFile file = fileService.getItem(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(file == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(file.getNode().getId()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXFileStub.FILENAME_FILE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBEXFileService fileService = (XBEXFileService) catalog.getCatalogService(XBCXFileService.class);
                XBEXFile file = fileService.getItem(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(file == null ? XBTEmptyBlock.getEmptyBlock() : new XBString(file.getFilename()));
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXFileStub.DATA_FILE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBAttribute index = provider.pullAttribute();
                provider.end();

                XBEXFileService fileService = (XBEXFileService) catalog.getCatalogService(XBCXFileService.class);
                XBEXFile file = fileService.getItem(index.getNaturalLong());

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.begin();
                listener.putData(new ByteArrayInputStream(file.getContent()));
                listener.end();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPXFileStub.NODEFILES_FILE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                long nodeId = provider.pullLongAttribute();
                provider.end();

                XBEXFileService fileService = (XBEXFileService) catalog.getCatalogService(XBCXFileService.class);
                XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
                XBENode node = nodeService.getItem(nodeId);
                List<XBCXFile> files = fileService.findFilesForNode(node);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.begin();
                listener.matchType(new XBFixedBlockType());
                listener.putAttribute(files.size());
                for (XBCXFile file : files) {
                    listener.putAttribute(file.getId());
                }
                listener.end();
            }
        });
    }
}
