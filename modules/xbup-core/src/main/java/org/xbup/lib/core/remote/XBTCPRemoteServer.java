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
package org.xbup.lib.core.remote;

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
import org.xbup.lib.core.block.declaration.XBDBlockType;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.catalog.declaration.XBCPBlockDecl;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBHead;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.basic.XBTProducer;
import org.xbup.lib.core.parser.basic.convert.XBTDecapsulator;
import org.xbup.lib.core.parser.basic.convert.XBTProducerToProvider;
import org.xbup.lib.core.parser.basic.convert.XBTProviderToProducer;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.event.XBEventListener;
import org.xbup.lib.core.parser.token.event.XBEventWriter;
import org.xbup.lib.core.parser.token.event.convert.XBTEventListenerToListener;
import org.xbup.lib.core.parser.token.event.convert.XBTToXBEventConvertor;
import org.xbup.lib.core.parser.token.pull.XBPullReader;
import org.xbup.lib.core.parser.token.pull.XBTPullProvider;
import org.xbup.lib.core.parser.token.pull.convert.XBTProviderToPullProvider;
import org.xbup.lib.core.parser.token.pull.convert.XBTPullProviderToProvider;
import org.xbup.lib.core.parser.token.pull.convert.XBToXBTPullConvertor;
import org.xbup.lib.core.stream.XBTokenInputStream;
import org.xbup.lib.core.stream.XBTInputTokenStream;
import org.xbup.lib.core.stream.XBTStreamChecker;

/**
 * XBUP level 1 RPC server using TCP/IP networking.
 *
 * @version 0.1 wr21.0 2011/12/31
 * @author XBUP Project (http://xbup.org)
 */
public class XBTCPRemoteServer implements XBRemoteServer {

    protected XBACatalog catalog;
    private ServerSocket xbService;
    private boolean stop;
    private Map<XBBlockType, XBProcedure> procMap;

    /** Creates a new instance of XBServiceHandler */
    public XBTCPRemoteServer(XBACatalog catalog) {
        this.catalog = catalog;
        stop = false;
        procMap = new HashMap<XBBlockType, XBProcedure>();
    }

    @Override
    public void addXBProcedure(XBProcedure procedure) {
        getProcMap().put(procedure.getType(), procedure);
    }

    @Override
    public void removeXBProcedure(XBProcedure procedure) {
        getProcMap().remove(procedure.getType());
    }

    /**
     * Open service handler
     *
     * @param port the port number, or 0 to use any free port.
     * @throws IOException if an I/O error occurs when opening the socket.
     */
    public void open(int port) throws IOException {
        xbService = new ServerSocket(port);
    }

    /**
     * Open service handler
     *
     * @param port the port number, or 0 to use any free port.
     * @param bindAddr the local InetAddress the server will bind to
     * @throws IOException if an I/O error occurs when opening the socket.
     */
    public void open(int port, InetAddress bindAddr) throws IOException {
        xbService = new ServerSocket(port, 50, bindAddr);
    }

    /** Providing main loop */
    public void run() throws XBProcessingException {
        Socket socket;
        while (!isStop()) {
            try {
                socket = getXbService().accept();
                Logger.getLogger(XBTCPRemoteServer.class.getName()).log(XBHead.XB_DEBUG_LEVEL, ("Request from: "+socket.getInetAddress().getHostAddress()));
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();
                XBHead.checkXBUPHead(input);
                XBPullReader reader = new XBPullReader(input);
                XBEventWriter writer = new XBEventWriter(output);
                respondMessage(reader, writer);
                input.close();
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

    public void respondMessage(XBTokenInputStream input, XBEventListener output) throws IOException, XBProcessingException {
        XBTDecapsulator decapsulator = new XBTDecapsulator();
        // TODO Optimalization should be done here
        XBTProducer producer = new XBTProviderToProducer(new XBTPullProviderToProvider(new XBToXBTPullConvertor(input)));
        producer.attachXBTListener(decapsulator);
        XBTStreamChecker source = new XBTStreamChecker(new XBTPullInputStream(new XBTProviderToPullProvider(new XBTProducerToProvider(decapsulator))));
        XBTListener target = new XBTEventListenerToListener(new XBTToXBEventConvertor(output));
        source.beginXBT();
        XBBlockType blockType = source.typeXBT();
        // TODO: Temporary patch
        long[] path = new long[5];
        path[1] = 2;
        path[2] = blockType.getGroupID().getLong() - 1;
        path[3] = blockType.getBlockID().getLong();
        blockType = new XBDBlockType(new XBCPBlockDecl(path));
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
            throw new XBParseException("Missing procedure handler: "+handler);
        } else {
            // XBFormatDecl decl = proc.getResultDecl();
            // target = new XBTEncapsulator(new XBContext(getCatalog(), new XBCDeclaration(decl,getCatalog())), XBTDefaultEventListener.toXBTListener(new XBTToXBEventConvertor(output)));
            proc.execute(source,target);
        }
    }

    /**
     * @return the catalog
     */
    public XBACatalog getCatalog() {
        return catalog;
    }

    /**
     * @return the xbService
     */
    public ServerSocket getXbService() {
        return xbService;
    }

    /**
     * @return the stop
     */
    public boolean isStop() {
        return stop;
    }

    /**
     * @return the procMap
     */
    public Map<XBBlockType, XBProcedure> getProcMap() {
        return procMap;
    }

    /**
     * @param stop the stop to set
     */
    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public class XBTPullInputStream extends XBTInputTokenStream {

        private XBTPullProvider producer;

        public XBTPullInputStream(XBTPullProvider producer) {
            this.producer = producer;
        }

        @Override
        public XBTToken pullXBTToken() throws XBProcessingException {
            try {
                return producer.pullXBTToken();
            } catch (IOException ex) {
                Logger.getLogger(XBTCPRemoteServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        @Override
        public void reset() throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean finished() throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void skip(long tokenCount) throws XBProcessingException, IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void close() throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
