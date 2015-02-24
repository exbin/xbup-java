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
import org.xbup.lib.core.parser.token.event.XBEventListener;
import org.xbup.lib.core.parser.token.event.XBEventWriter;
import org.xbup.lib.core.parser.token.event.convert.XBTEventListenerToListener;
import org.xbup.lib.core.parser.token.event.convert.XBTToXBEventConvertor;
import org.xbup.lib.core.parser.token.pull.XBPullProvider;
import org.xbup.lib.core.parser.token.pull.XBPullReader;
import org.xbup.lib.core.parser.token.pull.convert.XBTPullProviderToProvider;
import org.xbup.lib.core.parser.token.pull.convert.XBToXBTPullConvertor;
import org.xbup.lib.core.remote.XBProcedure;
import org.xbup.lib.core.remote.XBServiceServer;

/**
 * XBUP level 1 RPC server using TCP/IP networking.
 *
 * @version 0.1.25 2015/02/14
 * @author XBUP Project (http://xbup.org)
 */
public class XBTCPRemoteServer implements XBServiceServer {

    protected XBACatalog catalog;
    private ServerSocket xbService;
    private boolean stop;
    private final Map<XBBlockType, XBProcedure> procMap = new HashMap<>();

    /**
     * Creates a new instance of XBTCPRemoteServer.
     *
     * @param catalog catalog
     */
    public XBTCPRemoteServer(XBACatalog catalog) {
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
        xbService = new ServerSocket(port);
    }

    /**
     * Opens service handler.
     *
     * @param port the port number, or 0 to use any free port.
     * @param bindAddr the local InetAddress the server will bind to
     * @throws IOException if an I/O error occurs when opening the socket.
     */
    public void open(int port, InetAddress bindAddr) throws IOException {
        xbService = new ServerSocket(port, 50, bindAddr);
    }

    /**
     * Server main loop.
     */
    public void run() throws XBProcessingException {
        Socket socket;
        while (!isStop()) {
            try {
                socket = getXbService().accept();
                Logger.getLogger(XBTCPRemoteServer.class.getName()).log(XBHead.XB_DEBUG_LEVEL, ("Request from: " + socket.getInetAddress().getHostAddress()));
                OutputStream output;
                try (InputStream input = socket.getInputStream()) {
                    output = socket.getOutputStream();
                    XBHead.checkXBUPHead(input);
                    XBPullReader reader = new XBPullReader(input);
                    XBEventWriter writer = new XBEventWriter(output);
                    respondMessage(reader, writer);
                }
                output.close();
            } catch (XBParseException ex) {
                Logger.getLogger(XBTCPRemoteServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    public void stop() {
        setStop(true);
        try {
            getXbService().close();
        } catch (IOException ex) {
            Logger.getLogger(XBTCPRemoteServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void respondMessage(XBPullProvider input, XBEventListener output) throws IOException, XBProcessingException {
        XBTTypeDeclaringFilter decapsulator = new XBTTypeDeclaringFilter(null);
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
        }
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

    public ServerSocket getXbService() {
        return xbService;
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
}