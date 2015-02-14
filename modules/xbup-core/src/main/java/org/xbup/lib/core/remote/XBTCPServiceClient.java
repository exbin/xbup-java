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
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.declaration.XBContext;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.block.declaration.local.XBLBlockDecl;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBHead;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.basic.convert.XBTTypeFixingFilter;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTokenType;
import org.xbup.lib.core.parser.token.event.XBEventWriter;
import org.xbup.lib.core.parser.token.event.XBTEventListener;
import org.xbup.lib.core.parser.token.event.convert.XBTEventListenerToListener;
import org.xbup.lib.core.parser.token.event.convert.XBTToXBEventConvertor;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.parser.basic.convert.XBTDefaultMatchingProvider;
import org.xbup.lib.core.parser.token.pull.XBPullReader;
import org.xbup.lib.core.parser.token.pull.convert.XBTPullProviderToProvider;
import org.xbup.lib.core.parser.token.pull.convert.XBToXBTPullConvertor;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBService client connection handler using TCP/IP protocol.
 *
 * @version 0.1.25 2015/02/14
 * @author XBUP Project (http://xbup.org)
 */
public class XBTCPServiceClient implements XBServiceClient {

    private Socket socket;
//    private XBL2CatalogHandler catalog;
    private XBTDefaultMatchingProvider source;
    private XBTListener target;
    private String host;
    private int port;

    /*
     private enum commandTypeEnum {
     SERVICE, INFO, CATALOG
     };
     private enum serviceCommandEnum {
     STOP, PING, LOGIN, RESTART
     };
     private enum serviceInfoEnum {
     VERSION
     };
     */
    /**
     * Performs login to the server
     *
     * @param user
     * @param password
     * @return
     * @throws IOException
     */
    @Override
    public int login(String user, char[] password) throws IOException {
        try {
            init();
            target.typeXBT(new XBDeclBlockType(new XBLBlockDecl(LOGIN_SERVICE_PROCEDURE)));
            /*            XBString userName = new XBString(user);
             XBTSerialEventProducer eventSerializer = new XBTSerialEventProducer(userName);
             eventSerializer.attachXBTEventListener(XBTDefaultEventListener.getXBTEventListener(target));
             eventSerializer.generateXBTEvents();
             XBString userPass = new XBString(new String(password));
             eventSerializer = new XBTSerialEventProducer(userPass);
             eventSerializer.attachXBTEventListener(XBTDefaultEventListener.getXBTEventListener(target));
             eventSerializer.generateXBTEvents(); */
            target.endXBT();
            source.matchBeginXBT();
            source.matchTypeXBT(new XBFixedBlockType(XBBasicBlockType.UNKNOWN_BLOCK)); // TODO
            UBNat32 response = (UBNat32) source.matchAttribXBT();
            source.matchEndXBT();
            close();
            return response.getInt();
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBTCPServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public XBTCPServiceClient(String host, int port) {
        this.host = host;
        this.port = port;
//        catalog = new XBL2RemoteCatalog(new XBCatalogNetServiceClient(host, port));
    }

    @Override
    public void init() throws IOException, ConnectException {
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
    }

    @Override
    public String getVersion() {
        try {
            init();
            target.typeXBT(new XBDeclBlockType(new XBLBlockDecl(VERSION_INFO_PROCEDURE)));
            target.endXBT();
            source.matchBeginXBT();
            source.matchTypeXBT(new XBFixedBlockType(XBBasicBlockType.UNKNOWN_BLOCK)); // TODO
            UBNat32 majorVersion = (UBNat32) source.matchAttribXBT();
            UBNat32 minorVersion = (UBNat32) source.matchAttribXBT();
            source.matchEndXBT();
            close();
            return majorVersion.getInt() + "." + minorVersion.getInt();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBTCPServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void stop() {
        try {
            init();
            target.typeXBT(new XBDeclBlockType(new XBLBlockDecl(STOP_SERVICE_PROCEDURE)));
            target.endXBT();
            source.matchBeginXBT();
            source.matchTypeXBT(new XBFixedBlockType(XBBasicBlockType.UNKNOWN_BLOCK)); // TODO
            source.matchEndXBT();
            close();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBTCPServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void close() {
        try {
            getSocket().close();
        } catch (IOException ex) {
            Logger.getLogger(XBTCPServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void ping() {
        try {
            init();
            target.typeXBT(new XBDeclBlockType(new XBLBlockDecl(PING_SERVICE_PROCEDURE)));
            target.endXBT();
            source.matchBeginXBT();
            source.matchTypeXBT(new XBFixedBlockType(XBBasicBlockType.UNKNOWN_BLOCK)); // TODO
            source.matchEndXBT();
            close();
        } catch (ConnectException ex) {
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBTCPServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*
     public void respondMessage(XBL0InputStream input, XBL0OutputStream output) throws IOException, XBProcessingException {

     UBNat32 type = (UBNat32) source.attribXBT();
     UBNat32 command = (UBNat32) source.attribXBT();
     switch (commandTypeEnum.values()[type.toInt()]) {
     case SERVICE: { // System commands
     switch (serviceCommandEnum.values()[command.toInt()]) {
     case STOP: {
     System.out.println("Service is shutting down.");
     break;
     }
     default: throw new XBProcessingException("Unsupported command");
     }
     break;
     }
     case INFO: {
     switch (serviceInfoEnum.values()[command.toInt()]) {
     case VERSION: {
     break;
     }
     default: throw new XBProcessingException("Unsupported command");
     }
     break;
     }
     default: throw new XBProcessingException("Unexpected command");
     }
     source.endXBT();
     output.close();
     input.close();
     }
     */

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getLocalAddress() {
        return getSocket().getLocalAddress().getHostAddress();
    }

    @Override
    public String getHostAddress() {
        return getSocket().getInetAddress().getHostAddress();
    }

    @Override
    public boolean validate() {
        ping();
        if (getSocket() != null) {
            close();
        }
        return (getSocket() != null);
    }

    @Override
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
