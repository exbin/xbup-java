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
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.remote.XBCallHandler;
import org.xbup.lib.core.serial.param.XBPListenerSerialHandler;
import org.xbup.lib.core.serial.param.XBPProviderSerialHandler;
import org.xbup.lib.core.type.XBString;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC Stub utilities.
 *
 * @version 0.1.25 2015/03/15
 * @author XBUP Project (http://xbup.org)
 */
class XBPStubUtils {

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
