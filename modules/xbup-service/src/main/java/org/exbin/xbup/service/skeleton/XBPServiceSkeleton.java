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
 * @version 0.1.25 2015/03/10
 * @author ExBin Project (http://exbin.org)
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
