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
package org.xbup.lib.core.catalog.remote.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.catalog.XBRCatalog;
import org.xbup.lib.core.catalog.base.XBCXItemInfo;
import org.xbup.lib.core.catalog.base.XBCXFile;
import org.xbup.lib.core.catalog.base.manager.XBCLimiManager;
import org.xbup.lib.core.catalog.client.XBCatalogServiceMessage;
import org.xbup.lib.core.catalog.remote.XBRItem;
import org.xbup.lib.core.catalog.remote.XBRItemInfo;
import org.xbup.lib.core.catalog.remote.XBRItemLimi;
import org.xbup.lib.core.catalog.remote.XBRXFile;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.remote.XBServiceClient;
import org.xbup.lib.core.stream.XBStreamChecker;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Manager class for XBRLimit catalog items.
 *
 * @version 0.1 wr21.0 2011/12/30
 * @author XBUP Project (http://xbup.org)
 */
public class XBRLimiManager extends XBRDefaultManager<XBRItemLimi> implements XBCLimiManager<XBRItemLimi> {

    public XBRLimiManager(XBRCatalog catalog) {
        super(catalog);
    }

    public XBCXItemInfo getRootDir() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.ROOT_NODE_PROCEDURE);
            if (message == null) {
                return null;
            }
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long index = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return new XBRItemInfo(client,index);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<XBCXItemInfo> getSubDirs(XBCXItemInfo node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.SUBNODES_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRItemInfo) node).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            List<XBCXItemInfo> result = new ArrayList<XBCXItemInfo>();
            long count = checker.attribXB().getLong();
            for (int i = 0; i < count; i++) {
                result.add(new XBRItemInfo(client,checker.attribXB().getLong()));
            }
            checker.endXB();
            message.close();
            return result;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBCXItemInfo getSubDir(XBCXItemInfo node, long index) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.SUBNODE_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRItemInfo) node).getId()));
            listener.attribXB(new UBNat32(index));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long subnode = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            if (subnode == 0) {
                return null;
            }
            return new XBRItemInfo(client,subnode);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public long getSubDirsCount(XBCXItemInfo node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.SUBNODESCOUNT_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRItemInfo) node).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long index = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return index;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public List<XBCXFile> getFiles(XBCXItemInfo node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.SPECS_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRItemInfo) node).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            List<XBCXFile> result = new ArrayList<XBCXFile>();
            long count = checker.attribXB().getLong();
            for (int i = 0; i < count; i++) {
                result.add(new XBRXFile(client,checker.attribXB().getLong()));
            }
            checker.endXB();
            message.close();
            return result;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBCXFile getFile(XBCXItemInfo node, long index) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.SPEC_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRItemInfo) node).getId()));
            listener.attribXB(new UBNat32(index));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long subSpec = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return new XBRXFile(client,subSpec);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBCXItemInfo findDirByXBPath(Long[] xbCatalogPath) {
        XBRItemInfo node = (XBRItemInfo) getRootDir();
        for (int i = 0; i < xbCatalogPath.length; i++) {
            node = (XBRItemInfo) findSubDirByXB(node,xbCatalogPath[i]);
            if (node==null) {
                break;
            }
        }
        return node;
/*        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBCatalogServiceClient.messageTypeEnum.NODE.ordinal(), XBCatalogServiceClient.nodeMessageEnum.FINDNODE.ordinal());
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(xbCatalogPath.length));
            for (int i = 0; i < xbCatalogPath.length; i++) {
                listener.attribXB(new UBNat32(xbCatalogPath[i]));
            }
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long index = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            if (index == 0) return null;
            return new XBRDirectory(client, index);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;*/
    }

    public XBCXItemInfo findParentByXBPath(Long[] xbCatalogPath) {
        if (xbCatalogPath.length == 0) {
            return null;
        }
        XBRItemInfo dir = (XBRItemInfo) getRootDir();
        for (int i = 0; i < xbCatalogPath.length-1; i++) {
            dir = (XBRItemInfo) findSubDirByXB(dir,xbCatalogPath[i]);
            if (dir==null) {
                break;
            }
        }
        return dir;
    }

    public Long[] getDirXBPath(XBCXItemInfo node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.PATHNODE_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRItemInfo) node).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long count = checker.attribXB().getLong();
            Long[] result = new Long[(int) count];
            for (int i = 0; i < count; i++) {
                result[i] = checker.attribXB().getLong();
            }
            checker.endXB();
            message.close();
            return result;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBCXItemInfo findOwnerByXBPath(Long[] xbCatalogPath) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.FINDOWNER_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(xbCatalogPath.length));
            for (int i = 0; i < xbCatalogPath.length; i++) {
                listener.attribXB(new UBNat32(xbCatalogPath[i]));
            }
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long index = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return new XBRItemInfo(client, index);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBCXItemInfo findSubDirByXB(XBCXItemInfo node, long xbIndex) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.SUBNODE_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRItemInfo) node).getId()));
            listener.attribXB(new UBNat32(xbIndex));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long spec = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return new XBRItemInfo(client,spec);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long findMaxSubDirXB(XBCXItemInfo node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.MAXSUBNODE_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRItemInfo) node).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long index = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return index;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBCXFile findFileByXB(XBCXItemInfo node, long xbIndex) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.FINDBLOCKSPEC_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRItemInfo) node).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long index = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return new XBRXFile(client,index);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long findMaxFileXB(XBCXItemInfo node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.MAXBLOCKSPEC_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRItemInfo) node).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long index = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return index;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public long getFilesCount(XBCXItemInfo node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.SPECSCOUNT_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRItemInfo) node).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long index = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return index;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public Long getAllDirsCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.NODESCOUNT_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long index = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return index;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public long getItemsCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
