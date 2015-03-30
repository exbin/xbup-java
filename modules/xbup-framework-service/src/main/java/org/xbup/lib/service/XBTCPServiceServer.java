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
package org.xbup.lib.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.xbup.lib.client.XBTCPServiceClient.SERVICE_INVOCATION_SUCCESSFUL;
import static org.xbup.lib.client.stub.XBPInfoStub.NODE_INFO_PROCEDURE;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.block.declaration.catalog.XBCBlockDecl;
import org.xbup.lib.core.block.declaration.local.XBLBlockDecl;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.catalog.base.service.XBCSpecService;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBParserMode;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBHead;
import org.xbup.lib.core.parser.token.XBTBeginToken;
import org.xbup.lib.core.parser.token.XBTEndToken;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTypeToken;
import org.xbup.lib.core.parser.token.event.XBEventWriter;
import org.xbup.lib.core.parser.token.event.XBTEventListener;
import org.xbup.lib.core.parser.token.event.convert.XBTEventTypeUndeclaringFilter;
import org.xbup.lib.core.parser.token.event.convert.XBTPrintEventFilter;
import org.xbup.lib.core.parser.token.event.convert.XBTToXBEventConvertor;
import org.xbup.lib.core.parser.token.pull.XBPullReader;
import org.xbup.lib.core.parser.token.pull.XBTPullProvider;
import org.xbup.lib.core.parser.token.pull.convert.XBTPrintPullFilter;
import org.xbup.lib.core.parser.token.pull.convert.XBTPullTypeDeclaringFilter;
import org.xbup.lib.core.parser.token.pull.convert.XBToXBTPullConvertor;
import org.xbup.lib.core.remote.XBExecutable;
import org.xbup.lib.core.remote.XBMultiProcedure;
import org.xbup.lib.core.remote.XBProcedure;
import org.xbup.lib.core.remote.XBServiceServer;

/**
 * XBUP level 1 RPC server using TCP/IP networking.
 *
 * @version 0.1.25 2015/03/30
 * @author XBUP Project (http://xbup.org)
 */
public class XBTCPServiceServer implements XBServiceServer {

    protected XBACatalog catalog;
    private ServerSocket serverSocket;
    private boolean stop;
    private final Map<XBBlockType, XBExecutable> procMap = new HashMap<>();

    /**
     * Creates a new instance of XBTCPRemoteServer.
     *
     * @param catalog catalog
     */
    public XBTCPServiceServer(XBACatalog catalog) {
        this.catalog = catalog;
        stop = false;
    }

    /**
     * Opens service handler.
     *
     * @param port the port number, or 0 to use any free port.
     * @throws IOException if an I/O error occurs when opening the socket.
     */
    public void open(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    /**
     * Opens service handler.
     *
     * @param port the port number, or 0 to use any free port.
     * @param bindAddr the local InetAddress the server will bind to
     * @throws IOException if an I/O error occurs when opening the socket.
     */
    public void open(int port, InetAddress bindAddr) throws IOException {
        serverSocket = new ServerSocket(port, 50, bindAddr);
    }

    /**
     * Server main loop.
     */
    public void run() throws XBProcessingException {
        Socket socket;
        try {
            socket = getServerSocket().accept();
            Logger.getLogger(XBTCPServiceServer.class.getName()).log(XBHead.XB_DEBUG_LEVEL, ("Request from: " + socket.getInetAddress().getHostAddress()));
            OutputStream outputStream = null;
            try {
                InputStream inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                XBHead.checkXBUPHead(inputStream);
                while (!isStop()) {
                    XBTPullTypeDeclaringFilter input = new XBTPullTypeDeclaringFilter(catalog, new XBTPrintPullFilter("I", new XBToXBTPullConvertor(new XBPullReader(inputStream, XBParserMode.SINGLE_BLOCK))));
                    XBTEventTypeUndeclaringFilter output = new XBTEventTypeUndeclaringFilter(catalog, new XBTPrintEventFilter("O", new XBTToXBEventConvertor(new XBEventWriter(outputStream, XBParserMode.SINGLE_BLOCK))));
                    respondMessage(input, output);
                }
            } catch (IOException | XBProcessingException ex) {
                Logger.getLogger(XBTCPServiceServer.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (outputStream != null) {
                outputStream.close();
            }

        } catch (XBParseException ex) {
            Logger.getLogger(XBTCPServiceServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void stop() {
        setStop(true);
        try {
            getServerSocket().close();
        } catch (IOException ex) {
            Logger.getLogger(XBTCPServiceServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void respondMessage(XBTPullTypeDeclaringFilter input, XBTEventTypeUndeclaringFilter output) throws IOException, XBProcessingException {
        XBTTypePreloadingPullProvider preloading = new XBTTypePreloadingPullProvider(input);
        // TODO do proper matching later
        XBDeclBlockType blockType = (XBDeclBlockType) preloading.getBlockType();
        XBCBlockDecl blockDecl = (XBCBlockDecl) blockType.getBlockDecl();
        XBCSpecService specService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        blockType.setBlockDecl(new XBLBlockDecl(specService.getSpecXBPath(blockDecl.getBlockSpecRev().getParent()), blockDecl.getBlockSpecRev().getXBIndex().intValue()));
        XBExecutable executable = procMap.get(blockType);

        XBTEventListener eventListener = new ExecutionEventListener(output);

        if (executable instanceof XBProcedure) {
            ((XBProcedure) executable).execute(preloading, eventListener);
        } else if (executable instanceof XBMultiProcedure) {
            ((XBMultiProcedure) executable).execute(blockType, preloading, eventListener);
        } else {
            // TODO Exception processing
            throw new UnsupportedOperationException("Not supported yet.");
        }
        eventListener.putXBTToken(new XBTEndToken());
    }

    @Override
    public void addXBProcedure(XBBlockType procedureType, XBExecutable procedure) {
        procMap.put(procedureType, procedure);
    }

    @Override
    public void removeXBProcedure(XBBlockType procedureType) {
        procMap.remove(procedureType);
    }

    public XBACatalog getCatalog() {
        return catalog;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public boolean isStop() {
        return stop;
    }

    public Map<XBBlockType, XBExecutable> getProcMap() {
        return procMap;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    private class XBTTypePreloadingPullProvider implements XBTPullProvider {

        private final XBTPullProvider pullProvider;
        private XBTBeginToken beginToken;
        private XBTTypeToken typeToken;

        public XBTTypePreloadingPullProvider(XBTPullProvider pullProvider) throws XBProcessingException, IOException {
            this.pullProvider = pullProvider;
            beginToken = (XBTBeginToken) pullProvider.pullXBTToken();
            typeToken = (XBTTypeToken) pullProvider.pullXBTToken();
        }

        @Override
        public XBTToken pullXBTToken() throws XBProcessingException, IOException {
            if (typeToken == null) {
                return pullProvider.pullXBTToken();
            } else {
                if (beginToken != null) {
                    XBTToken token = beginToken;
                    beginToken = null;
                    return token;
                } else {
                    XBTToken token = typeToken;
                    typeToken = null;
                    return token;
                }
            }
        }

        public XBBlockType getBlockType() {
            return typeToken.getBlockType();
        }
    }

    private class ExecutionEventListener implements XBTEventListener {

        private boolean started = false;
        private final XBTEventTypeUndeclaringFilter output;

        public ExecutionEventListener(XBTEventTypeUndeclaringFilter output) {
            this.output = output;
        }

        @Override
        public void putXBTToken(XBTToken token) throws XBProcessingException, IOException {
            if (!started) {
                output.putXBTToken(new XBTBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED)); // TODO terminated
                output.putXBTToken(new XBTTypeToken(new XBDeclBlockType(SERVICE_INVOCATION_SUCCESSFUL)));
                started = true;
            }

            output.putXBTToken(token);
        }
    }
}
