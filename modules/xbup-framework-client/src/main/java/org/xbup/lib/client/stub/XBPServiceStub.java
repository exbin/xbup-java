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
package org.xbup.lib.client.stub;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.remote.XBCallHandler;
import org.xbup.lib.core.remote.XBServiceClient;
import org.xbup.lib.core.serial.param.XBPListenerSerialHandler;
import org.xbup.lib.core.serial.param.XBPProviderSerialHandler;
import org.xbup.lib.core.type.XBString;

/**
 * RPC Stub for Info extension related operations.
 *
 * @version 0.1.25 2015/02/24
 * @author XBUP Project (http://xbup.org)
 */
public class XBPServiceStub {

    public static long[] LOGIN_SERVICE_PROCEDURE = {0, 2, 0, 0, 0};
    public static long[] STOP_SERVICE_PROCEDURE = {0, 2, 0, 1, 0};
    public static long[] RESTART_SERVICE_PROCEDURE = {0, 2, 0, 2, 0};
    public static long[] PING_SERVICE_PROCEDURE = {0, 2, 0, 3, 0};

    public static long[] VERSION_INFO_PROCEDURE = {0, 2, 1, 0, 0};

    private final XBServiceClient client;

    public XBPServiceStub(XBServiceClient client) {
        this.client = client;
    }

    public int login(final String user, final char[] password) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(STOP_SERVICE_PROCEDURE));
            XBString userName = new XBString(user);
            serialInput.consist(userName);
            XBString userPass = new XBString(new String(password));
            serialInput.consist(userPass);
            serialInput.end();

            return 1;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPServiceStub.class.getName()).log(Level.SEVERE, null, ex);
        }

        return 0;
    }

    public String getVersion() {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(STOP_SERVICE_PROCEDURE));
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            XBString version = new XBString();
            serialOutput.process(version);

            return version.getValue();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPServiceStub.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public void stop() {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(STOP_SERVICE_PROCEDURE));
            serialInput.end();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPServiceStub.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean ping() {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(PING_SERVICE_PROCEDURE));
            serialInput.end();
            return true;
        } catch (XBProcessingException | IOException ex) {
//            Logger.getLogger(XBPServiceStub.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }
}
