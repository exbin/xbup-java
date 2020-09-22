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
package org.exbin.xbup.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.exbin.xbup.client.XBLoggingInputStream;
import org.exbin.xbup.client.XBLoggingOutputStream;
import org.exbin.xbup.client.XBTCPServiceClient;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.block.declaration.catalog.XBCBlockDecl;
import org.exbin.xbup.core.block.declaration.local.XBLBlockDecl;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.service.XBCSpecService;
import org.exbin.xbup.core.parser.XBParsingException;
import org.exbin.xbup.core.parser.XBParserMode;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.basic.XBHead;
import org.exbin.xbup.core.parser.token.XBTBeginToken;
import org.exbin.xbup.core.parser.token.XBTEndToken;
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.parser.token.XBTTypeToken;
import org.exbin.xbup.core.parser.token.event.XBEventWriter;
import org.exbin.xbup.core.parser.token.event.XBTEventListener;
import org.exbin.xbup.core.parser.token.event.convert.XBTEventTypeUndeclaringFilterNoDeclaration;
import org.exbin.xbup.core.parser.token.event.convert.XBTPrintEventFilter;
import org.exbin.xbup.core.parser.token.event.convert.XBTToXBEventConvertor;
import org.exbin.xbup.core.parser.token.pull.XBPullReader;
import org.exbin.xbup.core.parser.token.pull.XBTPullProvider;
import org.exbin.xbup.core.parser.token.pull.convert.XBTPrintPullFilter;
import org.exbin.xbup.core.parser.token.pull.convert.XBTPullTypeDeclaringFilterNoDeclaration;
import org.exbin.xbup.core.parser.token.pull.convert.XBToXBTPullConvertor;
import org.exbin.xbup.core.remote.XBExecutable;
import org.exbin.xbup.core.remote.XBMultiProcedure;
import org.exbin.xbup.core.remote.XBProcedure;
import org.exbin.xbup.core.remote.XBServiceServer;
import org.exbin.xbup.service.entity.ServiceELogItem;
import org.exbin.xbup.service.entity.service.ServiceELogItemService;

/**
 * XBUP level 1 RPC server using TCP/IP networking.
 *
 * @version 0.2.1 2020/09/22
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBTCPServiceServer implements XBServiceServer {

    private final static int BACK_LOG_LIMIT = 50;

    protected XBACatalog catalog;
    private ServerSocket serverSocket;
    private boolean stop;
    private final Map<XBBlockType, XBExecutable> procMap = new HashMap<>();
    private boolean debugMode = false;
    private final EntityManager entityManager;

    /**
     * Creates a new instance of XBTCPServiceServer.
     *
     * @param entityManager entity manager
     * @param catalog catalog
     */
    public XBTCPServiceServer(EntityManager entityManager, XBACatalog catalog) {
        this.entityManager = entityManager;
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
        serverSocket = new ServerSocket(port, BACK_LOG_LIMIT, bindAddr);
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
                ServiceELogItemService logItemService = new ServiceELogItemService();
                InputStream inputStream = debugMode
                        ? new XBLoggingInputStream(socket.getInputStream()) : socket.getInputStream();
                outputStream = debugMode
                        ? new XBLoggingOutputStream(socket.getOutputStream()) : socket.getOutputStream();
                XBHead.checkXBUPHead(inputStream);
                while (!isStop()) {
                    XBTPullTypeDeclaringFilterNoDeclaration input = new XBTPullTypeDeclaringFilterNoDeclaration(catalog, new XBTPrintPullFilter("I", new XBToXBTPullConvertor(new XBPullReader(inputStream, XBParserMode.SINGLE_BLOCK))));
                    XBTEventTypeUndeclaringFilterNoDeclaration output = new XBTEventTypeUndeclaringFilterNoDeclaration(catalog, new XBTPrintEventFilter("O", new XBTToXBEventConvertor(new XBEventWriter(outputStream, XBParserMode.SINGLE_BLOCK))));
                    respondMessage(input, output);

                    if (debugMode) {
                        ServiceELogItem logItem = (ServiceELogItem) logItemService.createItem();
                        logItem.setCreated(new Date());
                        ByteArrayOutputStream requestOutputStream = new ByteArrayOutputStream();
                        ((XBLoggingInputStream) inputStream).getData().saveToStream(requestOutputStream);
                        logItem.setRequestData(requestOutputStream.toByteArray());
                        ByteArrayOutputStream responseOutputStream = new ByteArrayOutputStream();
                        ((XBLoggingOutputStream) outputStream).getData().saveToStream(responseOutputStream);
                        logItem.setResponseData(responseOutputStream.toByteArray());
                        logItemService.persistItem(logItem);
                    }
                }
            } catch (IOException | XBProcessingException ex) {
                Logger.getLogger(XBTCPServiceServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(XBTCPServiceServer.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (outputStream != null) {
                outputStream.close();
            }

        } catch (XBParsingException | IOException ex) {
            Logger.getLogger(XBTCPServiceServer.class.getName()).log(Level.SEVERE, null, ex);
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

    public void respondMessage(XBTPullTypeDeclaringFilterNoDeclaration input, XBTEventTypeUndeclaringFilterNoDeclaration output) throws IOException, XBProcessingException {
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        XBTTypePreloadingPullProvider preloading = new XBTTypePreloadingPullProvider(input);
        // TODO do proper matching later
        XBDeclBlockType blockType = (XBDeclBlockType) preloading.getBlockType();
        XBCBlockDecl blockDecl = (XBCBlockDecl) blockType.getBlockDecl();
        XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
        blockType.setBlockDecl(new XBLBlockDecl(specService.getSpecXBPath(blockDecl.getBlockSpecRev().getParent()), (int) blockDecl.getBlockSpecRev().getXBIndex()));
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
        eventListener.putXBTToken(XBTEndToken.create());
        tx.commit();
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

    @Nonnull
    public Map<XBBlockType, XBExecutable> getProcMap() {
        return procMap;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    @ParametersAreNonnullByDefault
    private class XBTTypePreloadingPullProvider implements XBTPullProvider {

        private final XBTPullProvider pullProvider;
        private XBTBeginToken beginToken;
        private XBTTypeToken typeToken;

        public XBTTypePreloadingPullProvider(XBTPullProvider pullProvider) throws XBProcessingException, IOException {
            this.pullProvider = pullProvider;
            beginToken = (XBTBeginToken) pullProvider.pullXBTToken();
            typeToken = (XBTTypeToken) pullProvider.pullXBTToken();
        }

        @Nonnull
        @Override
        public XBTToken pullXBTToken() throws XBProcessingException, IOException {
            if (typeToken == null) {
                return pullProvider.pullXBTToken();
            } else if (beginToken != null) {
                XBTToken token = beginToken;
                beginToken = null;
                return token;
            } else {
                XBTToken token = typeToken;
                typeToken = null;
                return token;
            }
        }

        @Nonnull
        public XBBlockType getBlockType() {
            return typeToken.getBlockType();
        }
    }

    @ParametersAreNonnullByDefault
    private class ExecutionEventListener implements XBTEventListener {

        private boolean started = false;
        private final XBTEventTypeUndeclaringFilterNoDeclaration output;

        public ExecutionEventListener(XBTEventTypeUndeclaringFilterNoDeclaration output) {
            this.output = output;
        }

        @Override
        public void putXBTToken(XBTToken token) throws XBProcessingException, IOException {
            if (!started) {
                output.putXBTToken(XBTBeginToken.create(XBBlockTerminationMode.SIZE_SPECIFIED)); // TODO terminated
                output.putXBTToken(XBTTypeToken.create(new XBDeclBlockType(XBTCPServiceClient.SERVICE_INVOCATION_SUCCESSFUL)));
                started = true;
            }

            output.putXBTToken(token);
        }
    }
}
