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
package org.exbin.xbup.client.stub;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.remote.XBCallHandler;
import org.exbin.xbup.core.serial.param.XBPListenerSerialHandler;
import org.exbin.xbup.core.serial.param.XBPProviderSerialHandler;
import org.exbin.xbup.core.type.XBString;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * RPC Stub utilities.
 *
 * @version 0.1.25 2015/03/18
 * @author ExBin Project (http://exbin.org)
 */
public class XBPStubUtils {

    /**
     * Performs single RPC method passing single long attribute and receiving
     * single long result.
     *
     * @param procedureCall procedure call
     * @param type procedure type
     * @param parameter long parameter
     * @return long result
     */
    public static Long longToLongMethod(XBCallHandler procedureCall, XBBlockType type, Long parameter) {
        try {
            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(type);
            serialInput.putAttribute(parameter);
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            if (!serialOutput.pullIfEmptyBlock()) {
                UBNat32 result = new UBNat32();
                serialOutput.process(result);
                return result.getLong();
            }
            procedureCall.execute();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Performs single RPC method passing single long attribute and receiving
     * single long result.
     *
     * @param procedureCall procedure call
     * @param type procedure type
     * @param parameter long parameter
     */
    public static void longToVoidMethod(XBCallHandler procedureCall, XBBlockType type, Long parameter) {
        try {
            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(type);
            serialInput.putAttribute(parameter);
            serialInput.end();

            procedureCall.execute();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Performs single RPC method passing two long attributes and receiving
     * single long result.
     *
     * @param procedureCall procedure call
     * @param type procedure type
     * @param parameter1 first long parameter
     * @param parameter2 second long parameter
     * @return long result
     */
    public static Long twoLongsToLongMethod(XBCallHandler procedureCall, XBBlockType type, Long parameter1, Long parameter2) {
        try {
            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(type);
            serialInput.putAttribute(parameter1);
            serialInput.putAttribute(parameter2);
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            if (!serialOutput.pullIfEmptyBlock()) {
                UBNat32 result = new UBNat32();
                serialOutput.process(result);
                return result.getLong();
            }
            procedureCall.execute();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Performs single RPC method passing no arguments and receiving single long
     * result.
     *
     * @param procedureCall procedure call
     * @param type procedure type
     * @return long result
     */
    public static Long voidToLongMethod(XBCallHandler procedureCall, XBBlockType type) {
        try {
            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(type);
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            if (!serialOutput.pullIfEmptyBlock()) {
                UBNat32 result = new UBNat32();
                serialOutput.process(result);
                return result.getLong();
            }
            procedureCall.execute();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Performs single RPC method passing single long attribute and receiving
     * single string result.
     *
     * @param procedureCall procedure call
     * @param type procedure type
     * @param parameter long parameter
     * @return string result
     */
    public static String longToStringMethod(XBCallHandler procedureCall, XBBlockType type, Long parameter) {
        try {
            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(type);
            serialInput.putAttribute(parameter);
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            if (!serialOutput.pullIfEmptyBlock()) {
                XBString result = new XBString();
                serialOutput.process(result);
                return result.getValue();
            }
            procedureCall.execute();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
