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
package org.xbup.lib.client;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.client.stub.XBPServiceStub;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.declaration.XBContext;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.block.declaration.local.XBLBlockDecl;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.parser.XBParserMode;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBHead;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTokenType;
import org.xbup.lib.core.parser.token.event.XBEventWriter;
import org.xbup.lib.core.parser.token.event.XBTEventListener;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.parser.token.event.convert.XBTEventTypeUndeclaringFilter;
import org.xbup.lib.core.parser.token.event.convert.XBTPrintEventFilter;
import org.xbup.lib.core.parser.token.event.convert.XBTToXBEventConvertor;
import org.xbup.lib.core.parser.token.pull.XBPullReader;
import org.xbup.lib.core.parser.token.pull.convert.XBTPrintPullFilter;
import org.xbup.lib.core.parser.token.pull.convert.XBTPullTypeDeclaringFilter;
import org.xbup.lib.core.parser.token.pull.convert.XBToXBTPullConvertor;
import org.xbup.lib.core.remote.XBCallHandler;
import org.xbup.lib.core.remote.XBServiceClient;
import org.xbup.lib.core.stream.XBInput;
import org.xbup.lib.core.stream.XBOutput;

/**
 * XBService client connection handler using TCP/IP protocol.
 *
 * @version 0.1.25 2015/03/17
 * @author XBUP Project (http://xbup.org)
 */
public class XBTCPServiceClient implements XBServiceClient {

    private XBCatalog catalog;

    private Socket socket = null;
    private XBInput currentInput = null;
    private XBOutput currentOutput = null;

    private String host;
    private int port;
    private XBPServiceStub serviceStub;

    public static long[] XBSERVICE_FORMAT = {0, 2, 0, 0};

    public XBTCPServiceClient(String host, int port) {
        this.host = host;
        this.port = port;
        serviceStub = new XBPServiceStub(this);
//        catalog = new XBL2RemoteCatalog(new XBCatalogNetServiceClient(host, port));
    }

    /* public void init() throws IOException, ConnectException {
     try {
     socket = new Socket(getHost(), getPort());
     source = new XBTDefaultMatchingProvider(new XBTPullProviderToProvider(new XBToXBTPullConvertor(new XBPullReader(getSocket().getInputStream()))));
     target = new XBTTypeFixingFilter(null, null); // new XBServiceContext()
     OutputStream oStream = getSocket().getOutputStream();
     XBHead.writeXBUPHead(oStream);
     ((XBTTypeFixingFilter) target).attachXBTListener(new XBTEventListenerToListener(new MyXBTEventListener(new XBTToXBEventConvertor(new XBEventWriter(oStream)))));
     target.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
     } catch (XBProcessingException ex) {
     Logger.getLogger(XBTCPServiceClient.class.getName()).log(Level.SEVERE, null, ex);
     }
     } */
    @Override
    public XBCallHandler procedureCall() {
        try {
            if (socket == null) {
                socket = new Socket(getHost(), getPort());
                XBHead.writeXBUPHead(socket.getOutputStream());
            }

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
                    currentInput = new XBTEventTypeUndeclaringFilter(catalog, new XBTPrintEventFilter("O", new XBTToXBEventConvertor(new XBEventWriter(socket.getOutputStream(), XBParserMode.SINGLE_BLOCK))));
                    return currentInput;
                }

                @Override
                public XBOutput getResultOutput() throws XBProcessingException, IOException {
                    if (currentInput != null) {
                        // TODO: SKIP all remaining data
                        currentInput = null;
                    }

                    currentOutput = new XBTPullTypeDeclaringFilter(catalog, new XBTPrintPullFilter("I", new XBToXBTPullConvertor(new XBPullReader(socket.getInputStream(), XBParserMode.SINGLE_BLOCK))));
                    XBTPullTypeDeclaringFilter output = (XBTPullTypeDeclaringFilter) currentOutput;
                    output.pullXBTToken(); // Pull begin
                    output.pullXBTToken(); // TODO : Pull type
                    return currentOutput;
                }

                @Override
                public void execute() throws XBProcessingException, IOException {
                    ((XBTPullTypeDeclaringFilter) currentOutput).pullXBTToken(); // TODO match that it's end token
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
     * @param user
     * @param password
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

    public String getLocalAddress() {
        return getSocket().getLocalAddress().getHostAddress();
    }

    public String getHostAddress() {
        return getSocket().getInetAddress().getHostAddress();
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

    /**
     * TODO: Temporary first end event skipping filter
     */
    public class MyXBTEventListener implements XBTEventListener {

        private XBTEventListener listener;
        private long counter;
        private boolean first;

        public MyXBTEventListener(XBTEventListener listener) {
            this.listener = listener;
            counter = 0;
            first = false;
        }

        @Override
        public void putXBTToken(XBTToken token) throws XBProcessingException, IOException {
            if (token.getTokenType() == XBTTokenType.BEGIN) {
                counter++;
            } else if (token.getTokenType() == XBTTokenType.END) {
                counter--;
                if (counter == 0) {
                    if (!first) {
                        first = true;
                        return;
                    } else {
                        listener.putXBTToken(token);
                    }
                }
            }
            listener.putXBTToken(token);
        }
    }

    /**
     * TODO: Temporary static translation of XBService format.
     */
    public class XBServiceContext extends XBContext {

        public XBServiceContext() {
            this(null);
        }

        public XBServiceContext(XBSerializable rootNode) {
            super();
            /* XBDeclaration decl = new XBDeclaration();
             decl.setRootNode(rootNode);
             decl.setFormat(new XBDFormatDecl(XBSERVICE_FORMAT));
             setDeclaration(decl); */
        }

        public XBFixedBlockType toStaticType(XBBlockType type) {
            if (type instanceof XBFixedBlockType) {
                return (XBFixedBlockType) type;
            }
            if (type instanceof XBDeclBlockType) {
                XBBlockDecl blockDecl = ((XBDeclBlockType) type).getBlockDecl();
                if (blockDecl instanceof XBLBlockDecl) {
                    Long[] path = ((XBLBlockDecl) blockDecl).getCatalogObjectPath();
                    if (path.length != 5) {
                        return new XBFixedBlockType(XBBasicBlockType.UNKNOWN_BLOCK);
                    } else {
                        return new XBFixedBlockType(path[2] + 1, path[3]);
                    }
                }
            }
            return (XBFixedBlockType) type;
        }
    }
}
