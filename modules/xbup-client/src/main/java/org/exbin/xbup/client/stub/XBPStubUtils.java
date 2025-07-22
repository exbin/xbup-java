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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.remote.XBCallHandler;
import org.exbin.xbup.core.serial.param.XBPListenerSerialHandler;
import org.exbin.xbup.core.serial.param.XBPProviderSerialHandler;
import org.exbin.xbup.core.type.XBString;
import org.exbin.xbup.core.ubnumber.type.UBNat32;
import org.exbin.xbup.core.util.StreamUtils;

/**
 * RPC Stub utilities.
 *
 * TODO: Should be replaced with something less stupid
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBPStubUtils {

    private XBPStubUtils() {
    }

    /**
     * Performs single RPC method passing single long attribute and receiving
     * single long result.
     *
     * @param procedureCall procedure call
     * @param type procedure type
     * @param parameter long parameter
     * @return long result
     */
    @Nullable
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
            Logger.getLogger(XBPStubUtils.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(XBPStubUtils.class.getName()).log(Level.SEVERE, null, ex);
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
    @Nullable
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
            Logger.getLogger(XBPStubUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Performs single RPC method passing three long attributes and receiving
     * single long result.
     *
     * @param procedureCall procedure call
     * @param type procedure type
     * @param parameter1 first long parameter
     * @param parameter2 second long parameter
     * @param parameter3 third long parameter
     * @return long result
     */
    @Nullable
    public static Long threeLongsToLongMethod(XBCallHandler procedureCall, XBBlockType type, Long parameter1, Long parameter2, Long parameter3) {
        try {
            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(type);
            serialInput.putAttribute(parameter1);
            serialInput.putAttribute(parameter2);
            serialInput.putAttribute(parameter3);
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            if (!serialOutput.pullIfEmptyBlock()) {
                UBNat32 result = new UBNat32();
                serialOutput.process(result);
                return result.getLong();
            }
            procedureCall.execute();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPStubUtils.class.getName()).log(Level.SEVERE, null, ex);
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
    @Nullable
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
            Logger.getLogger(XBPStubUtils.class.getName()).log(Level.SEVERE, null, ex);
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
    @Nullable
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
            Logger.getLogger(XBPStubUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Nonnull
    public static InputStream voidToDataMethod(XBCallHandler procedureCall, XBBlockType type) {
        try {
            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(type);
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            serialOutput.begin();
            InputStream pullData = serialOutput.pullData();
            // TODO avoid copy
            ByteArrayOutputStream streamCopy = new ByteArrayOutputStream();
            StreamUtils.copyInputStreamToOutputStream(pullData, streamCopy);
            serialOutput.end();
            procedureCall.execute();
            return new ByteArrayInputStream(streamCopy.toByteArray());
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPStubUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
