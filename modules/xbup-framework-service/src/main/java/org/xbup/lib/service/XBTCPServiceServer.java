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
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFBlockType;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.block.declaration.local.XBLBlockDecl;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBHead;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.basic.XBTProducer;
import org.xbup.lib.core.parser.basic.convert.XBTDefaultMatchingProvider;
import org.xbup.lib.core.parser.basic.convert.XBTProducerToProvider;
import org.xbup.lib.core.parser.basic.convert.XBTProviderToProducer;
import org.xbup.lib.core.parser.basic.convert.XBTTypeDeclaringFilter;
import org.xbup.lib.core.parser.token.XBTBeginToken;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTypeToken;
import org.xbup.lib.core.parser.token.event.XBEventWriter;
import org.xbup.lib.core.parser.token.event.convert.XBTEventListenerToListener;
import org.xbup.lib.core.parser.token.event.convert.XBTEventTypeUndeclaringFilter;
import org.xbup.lib.core.parser.token.event.convert.XBTToXBEventConvertor;
import org.xbup.lib.core.parser.token.pull.XBPullReader;
import org.xbup.lib.core.parser.token.pull.XBTPullProvider;
import org.xbup.lib.core.parser.token.pull.convert.XBTPullProviderToProvider;
import org.xbup.lib.core.parser.token.pull.convert.XBTPullTypeDeclaringFilter;
import org.xbup.lib.core.parser.token.pull.convert.XBToXBTPullConvertor;
import org.xbup.lib.core.remote.XBProcedure;
import org.xbup.lib.core.remote.XBServiceServer;

/**
 * XBUP level 1 RPC server using TCP/IP networking.
 *
 * @version 0.1.25 2015/02/25
 * @author XBUP Project (http://xbup.org)
 */
public class XBTCPServiceServer implements XBServiceServer {

    protected XBACatalog catalog;
    private ServerSocket serverSocket;
    private boolean stop;
    private final Map<XBBlockType, XBProcedure> procMap = new HashMap<>();

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
        while (!isStop()) {
            try {
                socket = getServerSocket().accept();
                Logger.getLogger(XBTCPServiceServer.class.getName()).log(XBHead.XB_DEBUG_LEVEL, ("Request from: " + socket.getInetAddress().getHostAddress()));
                OutputStream outputStream;
                try (InputStream inputStream = socket.getInputStream()) {
                    outputStream = socket.getOutputStream();
                    XBHead.checkXBUPHead(inputStream);
                    XBTPullTypeDeclaringFilter input = new XBTPullTypeDeclaringFilter(catalog, new XBToXBTPullConvertor(new XBPullReader(inputStream)));
                    XBTEventTypeUndeclaringFilter output = new XBTEventTypeUndeclaringFilter(catalog, new XBTToXBEventConvertor(new XBEventWriter(outputStream)));
                    respondMessage(input, output);
                }
                outputStream.close();
            } catch (XBParseException ex) {
                Logger.getLogger(XBTCPServiceServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException e) {
                System.err.println(e);
            }
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
        XBProcedure proc = getProcMap().get(preloading.getBlockType());
        if (proc != null) {
            proc.execute(preloading, output);
        } else {
            // TODO Exception processing
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /*XBTTypeDeclaringFilter decapsulator = new XBTTypeDeclaringFilter(null);
         // TODO Optimalization should be done here
         XBTProducer producer = new XBTProviderToProducer(new XBTPullProviderToProvider(new XBToXBTPullConvertor(input)));
         producer.attachXBTListener(decapsulator);
         XBTDefaultMatchingProvider source = new XBTDefaultMatchingProvider(new XBTProducerToProvider(decapsulator));
         XBTListener target = new XBTEventListenerToListener(new XBTToXBEventConvertor(output));
         source.matchBeginXBT();
         XBBlockType blockType = source.matchTypeXBT();
         // TODO: Temporary patch
         long[] path = new long[5];
         path[1] = 2;
         path[2] = ((XBFBlockType) blockType).getGroupID().getLong() - 1;
         path[3] = ((XBFBlockType) blockType).getBlockID().getLong();
         blockType = new XBDeclBlockType(new XBLBlockDecl(path));
         XBProcedure proc = getProcMap().get(blockType);
         if (proc == null) {
         // TODO: Return NOT FOUND
         String handler = "";
         for (int i = 0; i < path.length; i++) {
         if (i > 0) {
         handler += ",";
         }
         handler += String.valueOf(path[i]);
         }
         throw new XBParseException("Missing procedure handler: " + handler);
         } else {
         // XBFormatDecl decl = proc.getResultDecl();
         // target = new XBTEncapsulator(new XBContext(getCatalog(), new XBCDeclaration(decl,getCatalog())), XBTDefaultEventListener.toXBTListener(new XBTToXBEventConvertor(output)));
         proc.execute(source, target);
         } */
    }

    @Override
    public void addXBProcedure(XBBlockType procedureType, XBProcedure procedure) {
        getProcMap().put(procedureType, procedure);
    }

    @Override
    public void removeXBProcedure(XBBlockType procedureType) {
        getProcMap().remove(procedureType);
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

    public Map<XBBlockType, XBProcedure> getProcMap() {
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
}
