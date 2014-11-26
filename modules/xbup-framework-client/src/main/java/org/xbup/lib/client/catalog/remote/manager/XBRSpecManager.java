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
package org.xbup.lib.client.catalog.remote.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.client.catalog.XBRCatalog;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCFormatSpec;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.XBCSpecDef;
import org.xbup.lib.core.catalog.base.XBCSpecDefType;
import org.xbup.lib.core.catalog.base.manager.XBCSpecManager;
import org.xbup.lib.client.XBCatalogServiceMessage;
import org.xbup.lib.client.catalog.remote.XBRBlockCons;
import org.xbup.lib.client.catalog.remote.XBRBlockJoin;
import org.xbup.lib.client.catalog.remote.XBRBlockListCons;
import org.xbup.lib.client.catalog.remote.XBRBlockListJoin;
import org.xbup.lib.client.catalog.remote.XBRBlockSpec;
import org.xbup.lib.client.catalog.remote.XBRFormatCons;
import org.xbup.lib.client.catalog.remote.XBRFormatJoin;
import org.xbup.lib.client.catalog.remote.XBRFormatSpec;
import org.xbup.lib.client.catalog.remote.XBRGroupCons;
import org.xbup.lib.client.catalog.remote.XBRGroupJoin;
import org.xbup.lib.client.catalog.remote.XBRGroupSpec;
import org.xbup.lib.client.catalog.remote.XBRItem;
import org.xbup.lib.client.catalog.remote.XBRNode;
import org.xbup.lib.client.catalog.remote.XBRSpec;
import org.xbup.lib.client.catalog.remote.XBRSpecDef;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.remote.XBServiceClient;
import org.xbup.lib.core.stream.XBStreamChecker;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Manager class for XBRSpec catalog items.
 *
 * @version 0.1.22 2013/08/31
 * @author XBUP Project (http://xbup.org)
 */
public class XBRSpecManager extends XBRDefaultManager<XBRSpec> implements XBCSpecManager<XBRSpec> {

    public XBRSpecManager(XBRCatalog catalog) {
        super(catalog);
    }

    @Override
    public Long getAllSpecsCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.SPECSCOUNT_SPEC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            Long index = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return index;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRSpecManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRSpecManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Long getAllFormatSpecsCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.FORMATSPECSCOUNT_SPEC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            Long index = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return index;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRSpecManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRSpecManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Long getAllGroupSpecsCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.GROUPSPECSCOUNT_SPEC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            Long index = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return index;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRSpecManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRSpecManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Long getAllBlockSpecsCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.BLOCKSPECSCOUNT_SPEC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            Long index = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return index;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRSpecManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRSpecManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Long[] getSpecXBPath(XBCSpec node) {
        ArrayList<Long> list = new ArrayList<Long>();
        XBCNode parent = node.getParent();
        while (parent != null) {
            if (parent.getParent() != null) {
                if (parent.getXBIndex() == null) {
                    return null;
                }
                list.add(0, parent.getXBIndex());
            }
            parent = (XBCNode) parent.getParent();
        }
        list.add(node.getXBIndex());
        return (Long[]) list.toArray(new Long[list.size()]);
    }

    @Override
    public List<XBCSpec> getSpecs(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.SPECS_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            List<XBCSpec> result = new ArrayList<XBCSpec>();
            long count = checker.attribXB().getLong();
            for (int i = 0; i < count; i++) {
                result.add(new XBRSpec(client,checker.attribXB().getLong()));
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

    @Override
    public XBRSpec getSpec(XBCNode node, long index) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.SPEC_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.attribXB(new UBNat32(index));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long subSpec = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return new XBRSpec(client,subSpec);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XBRFormatSpec getFormatSpec(XBCNode node, long index) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.FORMATSPEC_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.attribXB(new UBNat32(index));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long spec = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return new XBRFormatSpec(client,spec);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<XBCFormatSpec> getFormatSpecs(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.FORMATSPECS_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            List<XBCFormatSpec> result = new ArrayList<XBCFormatSpec>();
            long count = checker.attribXB().getLong();
            for (int i = 0; i < count; i++) {
                result.add(new XBRFormatSpec(client,checker.attribXB().getLong()));
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

    @Override
    public XBRBlockSpec getBlockSpec(XBCNode node, long index) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.BLOCKSPEC_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.attribXB(new UBNat32(index));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long spec = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return new XBRBlockSpec(client,spec);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<XBCBlockSpec> getBlockSpecs(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.BLOCKSPECS_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            List<XBCBlockSpec> result = new ArrayList<XBCBlockSpec>();
            long count = checker.attribXB().getLong();
            for (int i = 0; i < count; i++) {
                result.add(new XBRBlockSpec(client,checker.attribXB().getLong()));
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

    @Override
    public XBRGroupSpec getGroupSpec(XBCNode node, long index) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.GROUPSPEC_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.attribXB(new UBNat32(index));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long spec = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return new XBRGroupSpec(client,spec);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<XBCGroupSpec> getGroupSpecs(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.GROUPSPECS_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            List<XBCGroupSpec> result = new ArrayList<XBCGroupSpec>();
            long count = checker.attribXB().getLong();
            for (int i = 0; i < count; i++) {
                result.add(new XBRGroupSpec(client,checker.attribXB().getLong()));
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

    @Override
    public XBRBlockSpec findBlockSpecByXB(XBCNode node, long xbIndex) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.FINDBLOCKSPEC_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long index = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return new XBRBlockSpec(client,index);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Long findMaxBlockSpecXB(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.MAXBLOCKSPEC_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
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
    public XBRGroupSpec findGroupSpecByXB(XBCNode node, long xbIndex) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.FINDGROUPSPEC_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long index = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return new XBRGroupSpec(client,index);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Long findMaxGroupSpecXB(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.MAXGROUPSPEC_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
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
    public XBRFormatSpec findFormatSpecByXB(XBCNode node, long xbIndex) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.FINDFORMATSPEC_NODE_PROCEDURE);
            if (message == null) {
                return null;
            }
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.attribXB(new UBNat32(xbIndex));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long index = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return new XBRFormatSpec(client,index);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Long findMaxFormatSpecXB(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.MAXFORMATSPEC_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
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
    public long getFormatSpecsCount(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.FORMATSPECSCOUNT_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
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

    @Override
    public long getGroupSpecsCount(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.GROUPSPECSCOUNT_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
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

    @Override
    public long getBlockSpecsCount(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.BLOCKSPECSCOUNT_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
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

    @Override
    public long getSpecsCount(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.SPECSCOUNT_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
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

    @Override
    public long getDefsCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.BINDSCOUNT_BIND_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            Long index = checker.attribXB().getLong();
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

    @Override
    public XBRSpecDef getSpecDefByOrder(XBCSpec spec, long index) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.BIND_SPEC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRSpec) spec).getId()));
            listener.attribXB(new UBNat32(index));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long bind = checker.attribXB().getLong();
            long bindType = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            if (bindType == 0) {
                if (spec instanceof XBCFormatSpec) {
                    return new XBRFormatJoin(client,bind);
                }
                if (spec instanceof XBCGroupSpec) {
                    return new XBRGroupJoin(client,bind);
                }
                if (spec instanceof XBCBlockSpec) {
                    return new XBRBlockJoin(client,bind);
                }
                return null;
            } else {
                if (spec instanceof XBCFormatSpec) {
                    return new XBRFormatCons(client,bind);
                }
                if (spec instanceof XBCGroupSpec) {
                    return new XBRGroupCons(client,bind);
                }
                if (spec instanceof XBCBlockSpec) {
                    return new XBRBlockCons(client,bind);
                }
                return null;
            }
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRSpecManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRSpecManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XBRSpecDef findSpecDefByXB(XBCSpec spec, long xbIndex) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.FINDBIND_SPEC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRSpec) spec).getId()));
            listener.attribXB(new UBNat32(xbIndex));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long bind = checker.attribXB().getLong();
            int bindType = checker.attribXB().getInt();
            checker.endXB();
            message.close();
            if (spec instanceof XBCFormatSpec) {
                return bindType == 0 ? new XBRFormatCons(client,bind) : new XBRFormatJoin(client,bind);
            } else if (spec instanceof XBCGroupSpec) {
                return bindType == 0 ? new XBRGroupCons(client,bind) : new XBRGroupJoin(client,bind);
            } else if (spec instanceof XBCBlockSpec) {
                switch (bindType) {
                    case 0: return new XBRBlockCons(client,bind);
                    case 1: return new XBRBlockJoin(client,bind);
                    case 2: return new XBRBlockListCons(client,bind);
                    case 3: return new XBRBlockListJoin(client,bind);
                }
            } else {
                return new XBRSpecDef(client,bind);
            }
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRSpecManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRSpecManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<XBCSpecDef> getSpecDefs(XBCSpec spec) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.BINDS_SPEC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRSpec) spec).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            List<XBCSpecDef> result = new ArrayList<XBCSpecDef>();
            long count = checker.attribXB().getLong();
            for (int i = 0; i < count; i++) {
                result.add(new XBRSpecDef(client,checker.attribXB().getLong()));
            }
            checker.endXB();
            message.close();
            return result;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRSpecManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRSpecManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public long getSpecDefsCount(XBCSpec spec) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.BINDSCOUNT_SPEC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRSpec) spec).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long index = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return index;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRSpecManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRSpecManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public XBCSpecDef getSpecDef(long itemId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBCSpecDef createSpecDef(XBCSpec spec, XBCSpecDefType type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBCBlockSpec createBlockSpec() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBCGroupSpec createGroupSpec() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBCFormatSpec createFormatSpec() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
