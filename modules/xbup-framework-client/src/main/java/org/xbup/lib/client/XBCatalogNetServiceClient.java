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
import org.xbup.lib.core.parser.basic.convert.XBTTypeUndeclaringFilter;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.declaration.XBContext;
import org.xbup.lib.core.block.declaration.local.XBLFormatDecl;
import org.xbup.lib.core.block.declaration.local.XBLGroupDecl;
import org.xbup.lib.core.catalog.XBPCatalog;
import org.xbup.lib.core.parser.basic.XBMatchingProvider;
import org.xbup.lib.core.parser.basic.XBProvider;
import org.xbup.lib.core.parser.basic.convert.XBDefaultMatchingProvider;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.parser.token.XBToken;
import org.xbup.lib.core.parser.token.XBTokenType;
import org.xbup.lib.core.parser.token.event.XBEventListener;
import org.xbup.lib.core.parser.token.event.XBEventWriter;
import org.xbup.lib.core.parser.token.event.convert.XBEventListenerToListener;
import org.xbup.lib.core.parser.token.event.convert.XBTEventListenerToListener;
import org.xbup.lib.core.parser.token.event.convert.XBTToXBEventConvertor;
import org.xbup.lib.core.parser.token.pull.XBPullProvider;
import org.xbup.lib.core.parser.token.pull.XBPullReader;
import org.xbup.lib.core.parser.token.pull.convert.XBPullProviderToProvider;
import org.xbup.lib.core.serial.XBPSerialReader;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBService catalog client using IP networking.
 *
 * @version 0.1.25 2015/02/24
 * @author XBUP Project (http://xbup.org)
 */
public class XBCatalogNetServiceClient extends XBTCPServiceClient implements XBCatalogServiceClient {

    public XBCatalogNetServiceClient(String host, int port) {
        super(host, port);
        XBPCatalog catalog = new XBPCatalog();
        catalog.addFormatDecl(getContextFormatDecl());
        catalog.generateContext();
        setCatalog(catalog);
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

    /**
     * Returns local format declaration when catalog or service is not
     * available.
     *
     * @return local format declaration
     */
    private XBLFormatDecl getContextFormatDecl() {
        XBPSerialReader reader = new XBPSerialReader(ClassLoader.class.getResourceAsStream("/org/xbup/lib/client/resources/catalog_service_format.xb"));
        XBLGroupDecl groupDecl = new XBLGroupDecl();
        reader.read(groupDecl);
        return new XBLFormatDecl(groupDecl);
    }

    private class XBCatalogSocketMessage implements XBCatalogServiceMessage {

        private Socket socket;
        private XBEventListener output;
        private XBPullProvider input;

        private XBCatalogSocketMessage(Socket socket, long[] procedureId) {
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
            XBTTypeUndeclaringFilter encapsulator = new XBTTypeUndeclaringFilter(null, null);
            //new MyXBTConversible(procedureId)));
//            XBTTypeUndeclaringFilter encapsulator = new XBTTypeUndeclaringFilter(new XBL1Context(getCatalog(),decl));
            encapsulator.attachXBTListener(new XBTEventListenerToListener(new XBTToXBEventConvertor(output))); // new MyXBL0EventListener(new XBTToXBEventConvertor(

            /*            source = new XBL1StreamChecker(new XBL0ToL1StreamConvertor(new XBPullReader(xbService.getInputStream())));
             target = new XBTTypeUndeclaringFilter(new XBServiceContext());
             OutputStream oStream = xbService.getOutputStream();
             XBPullWriter.writeXBUPHead(oStream);
             ((XBTTypeUndeclaringFilter) target).attachXBTListener(XBTDefaultEventListener.toXBTListener(new MyXBL1EventListener(new XBTToXBEventConvertor(new XBPullWriter(oStream)))));
             target.beginXBT(false); */
        }

        @Override
        public XBEventListener getXBOutputStream() {
            return output;
        }

        @Override
        public XBPullProvider getXBInputStream() {
            return input;
        }

        @Override
        public XBListener getXBOutput() {
            return new XBEventListenerToListener(output);
        }

        @Override
        public XBMatchingProvider getXBInput() {
            return new MyChecker(new XBPullProviderToProvider(input));
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

    private class MyChecker extends XBDefaultMatchingProvider {

        private boolean headChecked;

        public MyChecker(XBProvider stream) {
            super(stream);
            headChecked = false;
        }

        @Override
        public XBAttribute matchAttribXB() throws XBProcessingException, IOException {
            if (!headChecked) {
                checkHead();
            }
            return super.matchAttribXB();
        }

        @Override
        public void matchAttribXB(XBAttribute value) throws XBProcessingException, IOException {
            if (!headChecked) {
                checkHead();
            }
            super.matchAttribXB(value);
        }

        @Override
        public XBBlockTerminationMode matchBeginXB() throws XBProcessingException, IOException {
            if (!headChecked) {
                checkHead();
            }
            return super.matchBeginXB();
        }

        @Override
        public void matchBeginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
            if (!headChecked) {
                checkHead();
            }
            super.matchBeginXB(terminationMode);
        }

        @Override
        public InputStream matchDataXB() throws XBProcessingException, IOException {
            if (!headChecked) {
                checkHead();
            }
            return super.matchDataXB();
        }

        @Override
        public void matchDataXB(InputStream data) throws XBProcessingException, IOException {
            if (!headChecked) {
                checkHead();
            }
            super.matchDataXB(data);
        }

        @Override
        public void matchEndXB() throws XBProcessingException, IOException {
            if (!headChecked) {
                checkHead();
            }
            super.matchEndXB();
        }

        private void checkHead() {
            try {
                super.matchBeginXB();
                super.matchAttribXB(new UBNat32(0));
                super.matchAttribXB(new UBNat32(0));
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBCatalogNetServiceClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            headChecked = true;
        }
    }

    /**
     * TODO: This is class skiping some end events (except first) as temporary
     * patch for improper implementation
     */
    private class MyXBOutputStream implements XBEventListener {

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
    }
}
