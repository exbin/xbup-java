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
package org.exbin.xbup.client.stub;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.remote.XBCallHandler;
import org.exbin.xbup.core.remote.XBServiceClient;
import org.exbin.xbup.core.serial.param.XBPListenerSerialHandler;
import org.exbin.xbup.core.serial.param.XBPProviderSerialHandler;
import org.exbin.xbup.core.type.XBNatural;
import org.exbin.xbup.core.type.XBString;

/**
 * RPC Stub for catalog service related operations.
 *
 * @author ExBin Project (https://exbin.org)
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
            if (procedureCall == null) {
                return -1;
            }

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(LOGIN_SERVICE_PROCEDURE));
            XBString userName = new XBString(user);
            serialInput.consist(userName);
            XBString userPass = new XBString(new String(password));
            serialInput.consist(userPass);
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            serialOutput.begin();
            serialOutput.pullType();
            int loginResult = serialOutput.pullIntAttribute();
            serialOutput.end();
            procedureCall.execute();

            return loginResult;
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
            serialInput.putType(new XBDeclBlockType(VERSION_INFO_PROCEDURE));
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            XBString version = new XBString();
            serialOutput.process(version);
            procedureCall.execute();

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
            procedureCall.execute();
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

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            XBNatural value = new XBNatural();
            serialOutput.process(value);
            procedureCall.execute();
            return true;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPServiceStub.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }
}
