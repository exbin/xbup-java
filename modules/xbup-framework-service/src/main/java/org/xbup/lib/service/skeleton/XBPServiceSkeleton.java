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
import org.xbup.lib.client.stub.XBPServiceStub;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.basic.XBTMatchingProvider;
import org.xbup.lib.core.remote.XBProcedure;
import org.xbup.lib.core.remote.XBServiceServer;
import org.xbup.lib.core.serial.param.XBPListenerSerialHandler;
import org.xbup.lib.core.stream.XBInput;
import org.xbup.lib.core.stream.XBOutput;
import org.xbup.lib.core.type.XBString;
import org.xbup.lib.core.ubnumber.type.UBNat32;
import org.xbup.lib.service.XBCatalogNetServiceServer;

/**
 * RPC skeleton class for XBRItemInfo catalog items.
 *
 * @version 0.1.25 2015/02/26
 * @author XBUP Project (http://xbup.org)
 */
public class XBPServiceSkeleton {

    private final XBCatalogNetServiceServer server;

    public XBPServiceSkeleton(XBCatalogNetServiceServer server) {
        this.server = server;
    }

    public void registerProcedures(XBServiceServer remoteServer) {
        remoteServer.addXBProcedure(new XBDeclBlockType(XBPServiceStub.STOP_SERVICE_PROCEDURE), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                source.matchEndXBT();
                server.performStop();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPServiceStub.PING_SERVICE_PROCEDURE), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
                source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPServiceStub.LOGIN_SERVICE_PROCEDURE), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                XBTListener result = (XBTListener) resultInput;
//            XBString loginName = new XBString();
//            XBString loginPass = new XBString();
//            XBL1SerialPullConsumer pullConsumer = new XBL1SerialPullConsumer(loginName);
/*            pullConsumer.attachXBL1PullProvider((XBL1PullProvider) source.getStream());
                 pullConsumer.processXBL1Pulls();
                 pullConsumer = new XBL1SerialPullConsumer(loginPass);
                 pullConsumer.attachXBL1PullProvider((XBL1PullProvider) source.getStream());
                 pullConsumer.processXBL1Pulls();*/
//            source.matchEndXBT();
                result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(0));
                result.attribXBT(new UBNat32(1)); // Ok
                result.endXBT();
            }
        });

        remoteServer.addXBProcedure(new XBDeclBlockType(XBPServiceStub.VERSION_INFO_PROCEDURE), new XBProcedure() {

            @Override
            public void execute(XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                XBString versionString = new XBString(server.getVersion());
                XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(resultInput);
                serialInput.process(versionString);

                /* XBTMatchingProvider source = (XBTMatchingProvider) parameters;
                 XBTListener result = (XBTListener) resultInput;
                 source.matchEndXBT();
                 result.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                 result.attribXBT(new UBNat32(0));
                 result.attribXBT(new UBNat32(0));
                 result.attribXBT(new UBNat32(0));
                 result.attribXBT(new UBNat32(1));
                 result.endXBT(); */
            }
        });
    }
}
