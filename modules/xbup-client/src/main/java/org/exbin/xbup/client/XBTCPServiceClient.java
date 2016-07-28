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
package org.exbin.xbup.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.exbin.xbup.client.stub.XBPServiceStub;
import org.exbin.xbup.core.catalog.XBCatalog;
import org.exbin.xbup.core.parser.XBParserMode;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.XBHead;
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.parser.token.XBTTokenType;
import org.exbin.xbup.core.parser.token.event.XBEventWriter;
import org.exbin.xbup.core.parser.token.event.convert.XBTEventTypeUndeclaringFilter;
import org.exbin.xbup.core.parser.token.event.convert.XBTPrintEventFilter;
import org.exbin.xbup.core.parser.token.event.convert.XBTToXBEventConvertor;
import org.exbin.xbup.core.parser.token.pull.XBPullReader;
import org.exbin.xbup.core.parser.token.pull.convert.XBTPrintPullFilter;
import org.exbin.xbup.core.parser.token.pull.convert.XBTPullPreLoader;
import org.exbin.xbup.core.parser.token.pull.convert.XBTPullTypeDeclaringFilter;
import org.exbin.xbup.core.parser.token.pull.convert.XBToXBTPullConvertor;
import org.exbin.xbup.core.remote.XBCallHandler;
import org.exbin.xbup.core.remote.XBServiceClient;
import org.exbin.xbup.core.stream.XBInput;
import org.exbin.xbup.core.stream.XBOutput;

/**
 * XBService client connection handler using TCP/IP protocol.
 *
 * @version 0.2.0 2016/02/20
 * @author ExBin Project (http://exbin.org)
 */
public class XBTCPServiceClient implements XBServiceClient {

    public static long[] XBSERVICE_FORMAT = {0, 2, 0, 0};
    public static long[] SERVICE_INVOCATION_SUCCESSFUL = {0, 2, 0, 0};
    public static long[] SERVICE_INVOCATION_FAILED = {0, 2, 1, 0};

    private XBCatalog catalog;

    private Socket socket = null;
    private XBInput currentInput = null;
    private XBOutput currentOutput = null;
    private boolean debugMode = false;

    private final String host;
    private final int port;
    private final XBPServiceStub serviceStub;

    private XBCallLogListener callLogListener = null;

    public XBTCPServiceClient(String host, int port) {
        this.host = host;
        this.port = port;
        serviceStub = new XBPServiceStub(this);
    }

    @Override
    public XBCallHandler procedureCall() {
        try {
            if (socket == null) {
                socket = new Socket(getHost(), getPort());
                XBHead.writeXBUPHead(socket.getOutputStream());
            }

            final OutputStream loggingOutputStream = debugMode
                    ? new XBLoggingOutputStream(socket.getOutputStream()) : socket.getOutputStream();
            final InputStream loggingInputStream = debugMode
                    ? new XBLoggingInputStream(socket.getInputStream()) : socket.getInputStream();

            if (currentInput != null) {
                // TODO: SKIP all remaining data
                currentInput = null;
            }
            if (currentOutput != null) {
                // TODO: Close all remaining data
                currentOutput = null;
            }
            return new XBCallHandler() {

                @Override
                public XBInput getParametersInput() throws XBProcessingException, IOException {
                    currentInput = new XBTEventTypeUndeclaringFilter(catalog, new XBTPrintEventFilter("O", new XBTToXBEventConvertor(new XBEventWriter(loggingOutputStream, XBParserMode.SINGLE_BLOCK))));
                    return currentInput;
                }

                @Override
                public XBOutput getResultOutput() throws XBProcessingException, IOException {
                    if (currentInput != null) {
                        // TODO: SKIP all remaining data
                        currentInput = null;
                    }

                    currentOutput = new XBTPullPreLoader(new XBTPullTypeDeclaringFilter(catalog, new XBTPrintPullFilter("I", new XBToXBTPullConvertor(new XBPullReader(loggingInputStream, XBParserMode.SINGLE_BLOCK)))));
                    XBTPullPreLoader output = (XBTPullPreLoader) currentOutput;
                    XBTToken beginToken = output.pullXBTToken();
                    if (beginToken.getTokenType() != XBTTokenType.BEGIN) {
                        throw new XBProcessingException("Begin token was expected", XBProcessingExceptionType.UNEXPECTED_ORDER);
                    }
                    XBTToken typeToken = output.pullXBTToken();
                    if (typeToken.getTokenType() != XBTTokenType.TYPE) {
                        throw new XBProcessingException("Type token was expected", XBProcessingExceptionType.UNEXPECTED_ORDER);
                    }
                    return currentOutput;
                }

                @Override
                public void execute() throws XBProcessingException, IOException {
                    XBTToken endToken = ((XBTPullPreLoader) currentOutput).pullXBTToken(); // TODO match that it's end token
                    if (endToken.getTokenType() != XBTTokenType.END) {
                        throw new XBProcessingException("End token was expected", XBProcessingExceptionType.UNEXPECTED_ORDER);
                    }

                    if (debugMode && callLogListener != null) {
                        callLogListener.callPerformed(((XBLoggingInputStream) loggingInputStream).getData(), ((XBLoggingOutputStream) loggingOutputStream).getData());
                    }
                }
            };
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBTCPServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * Performs login to the server
     *
     * @param user user
     * @param password password
     * @return 0 for OK
     */
    public int login(String user, char[] password) {
        return serviceStub.login(user, password);
    }

    public String getVersion() {
        return serviceStub.getVersion();
    }

    public void stop() {
        serviceStub.stop();
    }

    public void close() {
        try {
            getSocket().close();
        } catch (IOException ex) {
            Logger.getLogger(XBTCPServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean ping() {
        return serviceStub.ping();
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public XBCatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(XBCatalog catalog) {
        this.catalog = catalog;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public String getLocalAddress() {
        return getSocket().getLocalAddress().getHostAddress();
    }

    public String getHostAddress() {
        return getSocket().getInetAddress().getHostAddress();
    }

    public XBCallLogListener getCallLogListener() {
        return callLogListener;
    }

    public void setCallLogListener(XBCallLogListener callLogListener) {
        this.callLogListener = callLogListener;
    }

    public boolean validate() {
        try {
            final Socket callSocket = new Socket(getHost(), getPort());
            callSocket.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
