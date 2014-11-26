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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBHead;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.convert.XBTTypeReliantor;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.token.XBToken;
import org.xbup.lib.core.parser.token.XBTokenType;
import org.xbup.lib.core.parser.token.event.XBEventListener;
import org.xbup.lib.core.parser.token.event.XBEventWriter;
import org.xbup.lib.core.parser.token.event.convert.XBEventListenerToListener;
import org.xbup.lib.core.parser.token.event.convert.XBTEventListenerToListener;
import org.xbup.lib.core.parser.token.event.convert.XBTToXBEventConvertor;
import org.xbup.lib.core.parser.token.pull.XBPullReader;
import org.xbup.lib.core.remote.XBServiceClient;
import org.xbup.lib.core.remote.XBTCPServiceClient;
import org.xbup.lib.core.stream.XBTokenInputStream;
import org.xbup.lib.core.stream.XBTokenOutputStream;
import org.xbup.lib.core.stream.XBStreamChecker;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBService catalog client using IP networking.
 *
 * @version 0.1.21 2011/12/10
 * @author XBUP Project (http://xbup.org)
 */
public class XBCatalogNetServiceClient extends XBTCPServiceClient implements XBCatalogServiceClient {

//    private String host;
//    private int port;
//    private Socket socket;
//    private XBL2CatalogHandler catalog;

    public XBCatalogNetServiceClient(String host, int port) {
        super(host, port);
/*        this.host = host;
        this.port = port;
        this.socket = null; */
//        catalog = new XBL2RemoteCatalog(this);
    }

    @Override
    public XBCatalogServiceMessage executeProcedure(long[] procedureId) {
        // Temporary exclusion semaphor
        if (getSocket() != null) {
            while (!getSocket().isClosed()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(XBCatalogNetServiceClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        try {
            Socket socket = new Socket(getHost(), getPort());
            return new XBCatalogSocketMessage(socket, procedureId);
        } catch (UnknownHostException ex) {
            System.out.println("Unable to find host for catalog service");
//            Logger.getLogger(XBCatalogNetServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ConnectException ex) {
            System.out.println("Unable to connect to catalog service");
//            Logger.getLogger(XBCatalogNetServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("Unable to connect to catalog service");
//            Logger.getLogger(XBCatalogNetServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean validate() {
        try {
            XBCatalogServiceMessage message = executeProcedure(XBServiceClient.PING_SERVICE_PROCEDURE); // PING
            if (message == null) {
                return false;
            }
            message.getXBOutput().endXB();
            XBStreamChecker checker = message.getXBInput();
            checker.endXB();
            message.close();
            return true;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBCatalogNetServiceClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * @return the catalog
     */
/*    public XBL2CatalogHandler getCatalog() {
        return catalog;
    } */

    /**
     * @param catalog the catalog to set
     */
/*    public void setCatalog(XBL2CatalogHandler catalog) {
        this.catalog = catalog;
    } */

    private class XBCatalogSocketMessage implements XBCatalogServiceMessage {

        private Socket socket;
        private XBTokenOutputStream output;
        private XBTokenInputStream input;

        private XBCatalogSocketMessage(Socket socket, long[] procedureId) {
/*            System.out.print("Message (");
            if (messageType >= 0) {
                System.out.print(XBCatalogServiceClient.messageTypeEnum.values()[messageType]);
                System.out.print(", ");
                switch (XBCatalogServiceClient.messageTypeEnum.values()[messageType]) {
                    case BIND: {
                        System.out.print(XBCatalogServiceClient.bindMessageEnum.values()[messageId]);
                        break;
                    }
                    case DESC: {
                        System.out.print(XBCatalogServiceClient.descMessageEnum.values()[messageId]);
                        break;
                    }
                    case ITEM: {
                        System.out.print(XBCatalogServiceClient.itemMessageEnum.values()[messageId]);
                        break;
                    }
                    case LANG: {
                        System.out.print(XBCatalogServiceClient.langMessageEnum.values()[messageId]);
                        break;
                    }
                    case NAME: {
                        System.out.print(XBCatalogServiceClient.nameMessageEnum.values()[messageId]);
                        break;
                    }
                    case NODE: {
                        System.out.print(XBCatalogServiceClient.nodeMessageEnum.values()[messageId]);
                        break;
                    }
                    case SPEC: {
                        System.out.print(XBCatalogServiceClient.specMessageEnum.values()[messageId]);
                        break;
                    }
                    case REV: {
                        System.out.print(XBCatalogServiceClient.revMessageEnum.values()[messageId]);
                        break;
                    }
                    default: {
                        System.out.print(messageId);
                        break;
                    }
                }
            } else System.out.print(messageType + ", " + messageId);
            System.out.println(")"); */
            try {
                this.socket = socket;
                input = new XBPullReader(socket.getInputStream());
                OutputStream oStream = socket.getOutputStream();
                XBEventWriter writer = new XBEventWriter(oStream);
                output = new MyXBOutputStream(writer);
                XBHead.writeXBUPHead(oStream);
            } catch (IOException ex) {
                Logger.getLogger(XBCatalogNetServiceClient.class.getName()).log(Level.SEVERE, null, ex);
            }
//            XBL1Declaration decl = new XBL1Declaration();
//            decl.setFormat(new XBL1FormatDecl(XBServiceClient.XBSERVICE_FORMAT));
//            decl.setRootNode(new MyXBL1Conversible(procedureId));
            // TODO encapsulator
            XBTTypeReliantor encapsulator = new XBTTypeReliantor(null, null);
            //new MyXBTConversible(procedureId)));
//            XBTTypeReliantor encapsulator = new XBTTypeReliantor(new XBL1Context(getCatalog(),decl));
            encapsulator.attachXBTListener(new XBTEventListenerToListener(new XBTToXBEventConvertor(output))); // new MyXBL0EventListener(new XBTToXBEventConvertor(

/*            source = new XBL1StreamChecker(new XBL0ToL1StreamConvertor(new XBPullReader(xbService.getInputStream())));
            target = new XBTTypeReliantor(new XBServiceContext());
            OutputStream oStream = xbService.getOutputStream();
            XBPullWriter.writeXBUPHead(oStream);
            ((XBTTypeReliantor) target).attachXBTListener(XBTDefaultEventListener.toXBTListener(new MyXBL1EventListener(new XBTToXBEventConvertor(new XBPullWriter(oStream)))));
            target.beginXBT(false); */
        }

        @Override
        public XBTokenOutputStream getXBOutputStream() {
            return output;
        }

        @Override
        public XBTokenInputStream getXBInputStream() {
            return input;
        }

        @Override
        public XBListener getXBOutput() {
            return new XBEventListenerToListener(output);
        }

        @Override
        public XBStreamChecker getXBInput() {
            return new MyChecker(input);
        }

        @Override
        public void close() {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(XBCatalogNetServiceClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

/*    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    } */

    private class MyChecker extends XBStreamChecker {

        private boolean headChecked;

        public MyChecker(XBTokenInputStream stream) {
            super(stream);
            headChecked = false;
        }

        @Override
        public UBNatural attribXB() throws XBProcessingException, IOException {
            if (!headChecked) {
                checkHead();
            }
            return super.attribXB();
        }

        @Override
        public void attribXB(UBNatural value) throws XBProcessingException, IOException {
            if (!headChecked) {
                checkHead();
            }
            super.attribXB(value);
        }

        @Override
        public XBBlockTerminationMode beginXB() throws XBProcessingException, IOException {
            if (!headChecked) {
                checkHead();
            }
            return super.beginXB();
        }

        @Override
        public void beginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
            if (!headChecked) {
                checkHead();
            }
            super.beginXB(terminationMode);
        }

        @Override
        public InputStream dataXB() throws XBProcessingException, IOException {
            if (!headChecked) {
                checkHead();
            }
            return super.dataXB();
        }

        @Override
        public void dataXB(InputStream data) throws XBProcessingException, IOException {
            if (!headChecked) {
                checkHead();
            }
            super.dataXB(data);
        }

        @Override
        public void endXB() throws XBProcessingException, IOException {
            if (!headChecked) {
                checkHead();
            }
            super.endXB();
        }

        private void checkHead() {
            try {
                super.beginXB();
                super.attribXB(new UBNat32(0));
                super.attribXB(new UBNat32(0));
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBCatalogNetServiceClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            headChecked = true;
        }
    }

    /** TODO: This is class skiping some end events (except first) as temporary patch for improper implementation */
    private class MyXBOutputStream extends XBTokenOutputStream {

        private final XBEventListener listener;
        private long depth;
        private long counter;

        public MyXBOutputStream(XBEventListener listener) {
            this.listener = listener;
            depth = 0;
            counter = 0;
        }

        @Override
        public void putXBToken(XBToken token) throws XBProcessingException, IOException {
            if (token.getTokenType() == XBTokenType.BEGIN) {
                depth++;
            } else if (token.getTokenType() == XBTokenType.END) {
                depth--;
                if (depth == 1) {
                    counter++;
                    if (counter == 2) {
                        return;
                    }
                } else if (depth == 0 && counter >= 2) {
                    listener.putXBToken(token);
                }
            }

            listener.putXBToken(token);
        }

        @Override
        public void close() throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void flush() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    /*
    private class MyXBTConversible implements XBTConversible {

        private long[] procedureId;

        public MyXBTConversible(long[] procedureId) {
            this.procedureId = procedureId;
        }

        @Override
        public XBTProvider convertToXBT() {
            return new XBTProvider() {

                public void attachXBTListener(XBTListener listener) {
                    // TODO
                    try {
                        listener.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                        listener.typeXBT(new XBDBlockType(new XBCPBlockDecl(procedureId)));
                    } catch (XBProcessingException | IOException ex) {
                        Logger.getLogger(XBCatalogNetServiceClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                private int position = 0;
                
                @Override
                public void produceXBT(XBTListener listener) {
                    try {
                        switch (position) {
                            case 0: {
                                listener.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
                                break;
                            }
                            case 1: {
                                listener.typeXBT(new XBDBlockType(new XBCPBlockDecl(procedureId)));
                                break;
                            }
                        }
                        position++;
                    } catch (XBProcessingException | IOException ex) {
                        Logger.getLogger(XBCatalogNetServiceClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
        }
    } */
}
