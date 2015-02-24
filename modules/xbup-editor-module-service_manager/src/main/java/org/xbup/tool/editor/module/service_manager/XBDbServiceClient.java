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
package org.xbup.tool.editor.module.service_manager;

import java.io.IOException;
import java.net.Socket;
import javax.persistence.EntityManagerFactory;
import org.xbup.lib.client.XBCatalogServiceClient;
import org.xbup.lib.client.XBCatalogServiceMessage;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.declaration.XBContext;
import org.xbup.lib.core.block.declaration.XBDeclaration;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.block.declaration.local.XBLBlockDecl;
import org.xbup.lib.core.block.declaration.local.XBLFormatDecl;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTokenType;
import org.xbup.lib.core.parser.token.event.XBTEventListener;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.parser.basic.convert.XBTDefaultMatchingProvider;
import org.xbup.lib.core.remote.XBCallHandler;

/**
 * Fake XBService client using localhost database.
 *
 * @version 0.1.25 2015/02/24
 * @author XBUP Project (http://xbup.org)
 */
public class XBDbServiceClient implements XBCatalogServiceClient {

    private EntityManagerFactory entityManagerFactory;
//    private XBL2CatalogHandler catalog;
    private XBTDefaultMatchingProvider source;
    private XBTListener target;

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
     * @throws java.io.IOException
     */
    @Override
    public int login(String user, char[] password) throws IOException {
        return 0;
    }

    public XBDbServiceClient(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
//        catalog = new XBL2RemoteCatalog(new XBCatalogNetServiceClient(host, port));
    }

    /*    public XBL2CatalogHandler getCatalog() {
     return catalog;
     } */
    @Override
    public String getVersion() {
        return "0.1.25";
    }

    public void stop() {
    }

    @Override
    public void close() {
    }

    @Override
    public boolean ping() {
        return true;
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
        return "localhost";
    }

    @Override
    public int getPort() {
        return 0;
    }

    @Override
    public String getLocalAddress() {
        return "localhost";
    }

    @Override
    public String getHostAddress() {
        return "localhost";
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public Socket getSocket() {
        return null;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    @Override
    public XBCatalogServiceMessage executeProcedure(long[] procedureId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBCallHandler procedureCall() {
        throw new UnsupportedOperationException("Not supported yet.");
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
     * TODO: Temporary static translation of XBService format
     */
    public class XBServiceContext extends XBContext {

        public XBServiceContext() {
            this(null);
        }

        public XBServiceContext(XBSerializable rootNode) {
            super();
            XBDeclaration decl = new XBDeclaration(new XBLFormatDecl());
            decl.setRootBlock(rootNode);
            // TODO decl.setFormat(new XBLFormatDecl(XBSERVICE_FORMAT));
            // setDeclaration(decl);
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
