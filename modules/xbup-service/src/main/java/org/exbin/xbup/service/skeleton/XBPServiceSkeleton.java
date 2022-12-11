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
package org.exbin.xbup.service.skeleton;

import java.io.IOException;
import org.exbin.xbup.client.stub.XBPServiceStub;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.block.XBTEmptyBlock;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.remote.XBMultiProcedure;
import org.exbin.xbup.core.remote.XBServiceServer;
import org.exbin.xbup.core.serial.param.XBPListenerSerialHandler;
import org.exbin.xbup.core.serial.param.XBPProviderSerialHandler;
import org.exbin.xbup.core.stream.XBInput;
import org.exbin.xbup.core.stream.XBOutput;
import org.exbin.xbup.core.type.XBString;
import org.exbin.xbup.service.XBCatalogNetServiceServer;

/**
 * RPC skeleton class for catalog service control.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBPServiceSkeleton {

    private final XBCatalogNetServiceServer server;

    public XBPServiceSkeleton(XBCatalogNetServiceServer server) {
        this.server = server;
    }

    public void registerProcedures(XBServiceServer remoteServer) {
        remoteServer.addXBProcedure(new XBDeclBlockType(XBPServiceStub.STOP_SERVICE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                provider.end();

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.process(XBTEmptyBlock.getEmptyBlock());

                server.performStop();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPServiceStub.PING_SERVICE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                provider.end();

                // server.ping();
                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.begin();
                listener.matchType();
                listener.putAttribute(0);
                listener.end();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPServiceStub.LOGIN_SERVICE_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                XBString loginName = new XBString();
                provider.consist(loginName);
                XBString loginPass = new XBString();
                provider.consist(loginPass);
                provider.end();

                int loginResult = server.login(loginName.getValue(), loginPass.getValue() != null ? loginPass.getValue().toCharArray() : null);

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                listener.begin();
                listener.putType(new XBFixedBlockType());
                listener.putAttribute(loginResult);
                listener.end();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPServiceStub.VERSION_INFO_PROCEDURE), new XBMultiProcedure() {
            @Override
            public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                provider.begin();
                provider.matchType(blockType);
                provider.end();

                XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                XBString versionString = new XBString(server.getVersion());
                listener.process(versionString);
            }
        });
    }
}
