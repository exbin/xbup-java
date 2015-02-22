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
package org.xbup.lib.client.stub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.client.XBCatalogServiceClient;
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
import org.xbup.lib.client.catalog.remote.XBRRev;
import org.xbup.lib.client.catalog.remote.XBRSpec;
import org.xbup.lib.client.catalog.remote.XBRSpecDef;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCFormatSpec;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.XBCSpecDef;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.XBMatchingProvider;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC stub class for XBRSpec catalog items.
 *
 * @version 0.1.25 2015/02/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBPSpecStub implements XBPManagerStub<XBRSpec> {

    public static long[] SPEC_NODE_PROCEDURE = {0, 2, 4, 4, 0};
    public static long[] SPECS_NODE_PROCEDURE = {0, 2, 4, 5, 0};
    public static long[] FORMATSPEC_NODE_PROCEDURE = {0, 2, 4, 6, 0};
    public static long[] FORMATSPECS_NODE_PROCEDURE = {0, 2, 4, 7, 0};
    public static long[] GROUPSPEC_NODE_PROCEDURE = {0, 2, 4, 8, 0};
    public static long[] GROUPSPECS_NODE_PROCEDURE = {0, 2, 4, 9, 0};
    public static long[] BLOCKSPEC_NODE_PROCEDURE = {0, 2, 4, 10, 0};
    public static long[] BLOCKSPECS_NODE_PROCEDURE = {0, 2, 4, 11, 0};
    public static long[] FINDBLOCKSPEC_NODE_PROCEDURE = {0, 2, 4, 16, 0};
    public static long[] MAXBLOCKSPEC_NODE_PROCEDURE = {0, 2, 4, 17, 0};
    public static long[] FINDGROUPSPEC_NODE_PROCEDURE = {0, 2, 4, 18, 0};
    public static long[] MAXGROUPSPEC_NODE_PROCEDURE = {0, 2, 4, 19, 0};
    public static long[] FINDFORMATSPEC_NODE_PROCEDURE = {0, 2, 4, 20, 0};
    public static long[] MAXFORMATSPEC_NODE_PROCEDURE = {0, 2, 4, 21, 0};
    public static long[] SPECSCOUNT_NODE_PROCEDURE = {0, 2, 4, 22, 0};
    public static long[] BLOCKSPECSCOUNT_NODE_PROCEDURE = {0, 2, 4, 23, 0};
    public static long[] GROUPSPECSCOUNT_NODE_PROCEDURE = {0, 2, 4, 24, 0};
    public static long[] FORMATSPECSCOUNT_NODE_PROCEDURE = {0, 2, 4, 25, 0};

    public static long[] BIND_SPEC_PROCEDURE = {0, 2, 5, 0, 0};
    public static long[] BINDS_SPEC_PROCEDURE = {0, 2, 5, 1, 0};
    public static long[] BINDSCOUNT_SPEC_PROCEDURE = {0, 2, 5, 2, 0};
    public static long[] FINDBIND_SPEC_PROCEDURE = {0, 2, 5, 3, 0};
    public static long[] FORMATSPECSCOUNT_SPEC_PROCEDURE = {0, 2, 5, 5, 0};
    public static long[] GROUPSPECSCOUNT_SPEC_PROCEDURE = {0, 2, 5, 6, 0};
    public static long[] BLOCKSPECSCOUNT_SPEC_PROCEDURE = {0, 2, 5, 7, 0};
    public static long[] SPECSCOUNT_SPEC_PROCEDURE = {0, 2, 5, 8, 0};

    public static long[] TARGET_BIND_PROCEDURE = {0, 2, 6, 0, 0};
    public static long[] BINDSCOUNT_BIND_PROCEDURE = {0, 2, 6, 1, 0};

    private final XBCatalogServiceClient client;

    public XBPSpecStub(XBCatalogServiceClient client) {
        this.client = client;
    }

    public XBRRev getTarget(long specDefId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(TARGET_BIND_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(specDefId));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long target = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            if (target == 0) {
                return null;
            }
            return new XBRRev(client, target);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long getAllSpecsCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(SPECSCOUNT_SPEC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            Long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long getAllFormatSpecsCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(FORMATSPECSCOUNT_SPEC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            Long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long getAllGroupSpecsCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(GROUPSPECSCOUNT_SPEC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            Long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long getAllBlockSpecsCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(BLOCKSPECSCOUNT_SPEC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            Long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long[] getSpecXBPath(XBCSpec node) {
        ArrayList<Long> list = new ArrayList<>();
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

    public List<XBCSpec> getSpecs(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(SPECS_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            List<XBCSpec> result = new ArrayList<>();
            long count = checker.matchAttribXB().getNaturalLong();
            for (int i = 0; i < count; i++) {
                result.add(new XBRSpec(client, checker.matchAttribXB().getNaturalLong()));
            }
            checker.matchEndXB();
            message.close();
            return result;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRSpec getSpec(XBCNode node, long index) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(SPEC_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.attribXB(new UBNat32(index));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long subSpec = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return new XBRSpec(client, subSpec);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRFormatSpec getFormatSpec(XBCNode node, long index) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(FORMATSPEC_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.attribXB(new UBNat32(index));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long spec = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return new XBRFormatSpec(client, spec);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<XBCFormatSpec> getFormatSpecs(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(FORMATSPECS_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            List<XBCFormatSpec> result = new ArrayList<>();
            long count = checker.matchAttribXB().getNaturalLong();
            for (int i = 0; i < count; i++) {
                result.add(new XBRFormatSpec(client, checker.matchAttribXB().getNaturalLong()));
            }
            checker.matchEndXB();
            message.close();
            return result;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRBlockSpec getBlockSpec(XBCNode node, long index) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(BLOCKSPEC_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.attribXB(new UBNat32(index));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long spec = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return new XBRBlockSpec(client, spec);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<XBCBlockSpec> getBlockSpecs(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(BLOCKSPECS_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            List<XBCBlockSpec> result = new ArrayList<>();
            long count = checker.matchAttribXB().getNaturalLong();
            for (int i = 0; i < count; i++) {
                result.add(new XBRBlockSpec(client, checker.matchAttribXB().getNaturalLong()));
            }
            checker.matchEndXB();
            message.close();
            return result;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRGroupSpec getGroupSpec(XBCNode node, long index) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(GROUPSPEC_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.attribXB(new UBNat32(index));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long spec = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return new XBRGroupSpec(client, spec);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<XBCGroupSpec> getGroupSpecs(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(GROUPSPECS_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            List<XBCGroupSpec> result = new ArrayList<>();
            long count = checker.matchAttribXB().getNaturalLong();
            for (int i = 0; i < count; i++) {
                result.add(new XBRGroupSpec(client, checker.matchAttribXB().getNaturalLong()));
            }
            checker.matchEndXB();
            message.close();
            return result;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRBlockSpec findBlockSpecByXB(XBCNode node, long xbIndex) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(FINDBLOCKSPEC_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return new XBRBlockSpec(client, index);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long findMaxBlockSpecXB(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(MAXBLOCKSPEC_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRGroupSpec findGroupSpecByXB(XBCNode node, long xbIndex) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(FINDGROUPSPEC_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return new XBRGroupSpec(client, index);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long findMaxGroupSpecXB(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(MAXGROUPSPEC_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRFormatSpec findFormatSpecByXB(XBCNode node, long xbIndex) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(FINDFORMATSPEC_NODE_PROCEDURE);
            if (message == null) {
                return null;
            }
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.attribXB(new UBNat32(xbIndex));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return new XBRFormatSpec(client, index);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long findMaxFormatSpecXB(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(MAXFORMATSPEC_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public long getFormatSpecsCount(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(FORMATSPECSCOUNT_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public long getGroupSpecsCount(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(GROUPSPECSCOUNT_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public long getBlockSpecsCount(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(BLOCKSPECSCOUNT_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public long getSpecsCount(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(SPECSCOUNT_NODE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public long getDefsCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(BINDSCOUNT_BIND_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            Long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public XBRSpecDef getSpecDefByOrder(XBCSpec spec, long index) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(BIND_SPEC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRSpec) spec).getId()));
            listener.attribXB(new UBNat32(index));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long bind = checker.matchAttribXB().getNaturalLong();
            long bindType = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            if (bindType == 0) {
                if (spec instanceof XBCFormatSpec) {
                    return new XBRFormatJoin(client, bind);
                }
                if (spec instanceof XBCGroupSpec) {
                    return new XBRGroupJoin(client, bind);
                }
                if (spec instanceof XBCBlockSpec) {
                    return new XBRBlockJoin(client, bind);
                }
                return null;
            } else {
                if (spec instanceof XBCFormatSpec) {
                    return new XBRFormatCons(client, bind);
                }
                if (spec instanceof XBCGroupSpec) {
                    return new XBRGroupCons(client, bind);
                }
                if (spec instanceof XBCBlockSpec) {
                    return new XBRBlockCons(client, bind);
                }
                return null;
            }
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRSpecDef findSpecDefByXB(XBCSpec spec, long xbIndex) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(FINDBIND_SPEC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRSpec) spec).getId()));
            listener.attribXB(new UBNat32(xbIndex));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long bind = checker.matchAttribXB().getNaturalLong();
            int bindType = checker.matchAttribXB().getNaturalInt();
            checker.matchEndXB();
            message.close();
            if (spec instanceof XBCFormatSpec) {
                return bindType == 0 ? new XBRFormatCons(client, bind) : new XBRFormatJoin(client, bind);
            } else if (spec instanceof XBCGroupSpec) {
                return bindType == 0 ? new XBRGroupCons(client, bind) : new XBRGroupJoin(client, bind);
            } else if (spec instanceof XBCBlockSpec) {
                switch (bindType) {
                    case 0:
                        return new XBRBlockCons(client, bind);
                    case 1:
                        return new XBRBlockJoin(client, bind);
                    case 2:
                        return new XBRBlockListCons(client, bind);
                    case 3:
                        return new XBRBlockListJoin(client, bind);
                }
            } else {
                return new XBRSpecDef(client, bind);
            }
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<XBCSpecDef> getSpecDefs(XBCSpec spec) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(BINDS_SPEC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRSpec) spec).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            List<XBCSpecDef> result = new ArrayList<>();
            long count = checker.matchAttribXB().getNaturalLong();
            for (int i = 0; i < count; i++) {
                result.add(new XBRSpecDef(client, checker.matchAttribXB().getNaturalLong()));
            }
            checker.matchEndXB();
            message.close();
            return result;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public long getSpecDefsCount(XBCSpec spec) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(BINDSCOUNT_SPEC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRSpec) spec).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public XBRSpec createItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persistItem(XBRSpec item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeItem(XBRSpec item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRSpec getItem(long itemId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<XBRSpec> getAllItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getItemsCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
