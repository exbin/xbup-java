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
import org.xbup.lib.client.catalog.remote.XBRRev;
import org.xbup.lib.client.catalog.remote.XBRSpec;
import org.xbup.lib.client.catalog.remote.XBRSpecDef;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCFormatSpec;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.XBCSpecDef;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.remote.XBCallHandler;
import org.xbup.lib.core.serial.param.XBPListenerSerialHandler;
import org.xbup.lib.core.serial.param.XBPProviderSerialHandler;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC stub class for XBRSpec catalog items.
 *
 * @version 0.1.25 2015/03/14
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
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(TARGET_BIND_PROCEDURE));
            serialInput.putAttribute(specDefId);
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 target = new UBNat32();
            serialOutput.process(target);

            return target.isZero() ? null : new XBRRev(client, target.getLong());
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long getAllSpecsCount() {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(SPECSCOUNT_SPEC_PROCEDURE));
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 count = new UBNat32();
            serialOutput.process(count);

            return count.getLong();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long getAllFormatSpecsCount() {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(FORMATSPECSCOUNT_SPEC_PROCEDURE));
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 count = new UBNat32();
            serialOutput.process(count);

            return count.getLong();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long getAllGroupSpecsCount() {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(GROUPSPECSCOUNT_SPEC_PROCEDURE));
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 count = new UBNat32();
            serialOutput.process(count);

            return count.getLong();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long getAllBlockSpecsCount() {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(BLOCKSPECSCOUNT_SPEC_PROCEDURE));
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 count = new UBNat32();
            serialOutput.process(count);

            return count.getLong();
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
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(SPECS_NODE_PROCEDURE));
            serialInput.putAttribute(node.getId());
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            if (!serialOutput.pullIfEmptyBlock()) {
                serialInput.begin();
                serialInput.matchType(new XBFixedBlockType());
                long count = serialInput.pullLongAttribute();
                List<XBCSpec> result = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    result.add(new XBRSpec(client, serialInput.pullLongAttribute()));
                }
                serialInput.end();
                return result;
            }
            
            return null;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRSpec getSpec(XBCNode node, long orderIndex) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(SPEC_NODE_PROCEDURE));
            serialInput.putAttribute(node.getId());
            serialInput.putAttribute(orderIndex);
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 index = new UBNat32();
            serialOutput.process(index);

            return new XBRSpec(client, index.getLong());
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRFormatSpec getFormatSpec(XBCNode node, long orderIndex) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(FORMATSPEC_NODE_PROCEDURE));
            serialInput.putAttribute(node.getId());
            serialInput.putAttribute(orderIndex);
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 index = new UBNat32();
            serialOutput.process(index);

            return new XBRFormatSpec(client, index.getLong());
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<XBCFormatSpec> getFormatSpecs(XBCNode node) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(FORMATSPECS_NODE_PROCEDURE));
            serialInput.putAttribute(node.getId());
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            if (!serialOutput.pullIfEmptyBlock()) {
                serialInput.begin();
                serialInput.matchType(new XBFixedBlockType());
                long count = serialInput.pullLongAttribute();
                List<XBCFormatSpec> result = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    result.add(new XBRFormatSpec(client, serialInput.pullLongAttribute()));
                }
                serialInput.end();
                return result;
            }
            
            return null;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRBlockSpec getBlockSpec(XBCNode node, long orderIndex) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(BLOCKSPEC_NODE_PROCEDURE));
            serialInput.putAttribute(node.getId());
            serialInput.putAttribute(orderIndex);
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 index = new UBNat32();
            serialOutput.process(index);

            return new XBRBlockSpec(client, index.getLong());
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<XBCBlockSpec> getBlockSpecs(XBCNode node) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(BLOCKSPECS_NODE_PROCEDURE));
            serialInput.putAttribute(node.getId());
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            if (!serialOutput.pullIfEmptyBlock()) {
                serialInput.begin();
                serialInput.matchType(new XBFixedBlockType());
                long count = serialInput.pullLongAttribute();
                List<XBCBlockSpec> result = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    result.add(new XBRBlockSpec(client, serialInput.pullLongAttribute()));
                }
                serialInput.end();
                return result;
            }
            
            return null;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRGroupSpec getGroupSpec(XBCNode node, long orderIndex) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(GROUPSPEC_NODE_PROCEDURE));
            serialInput.putAttribute(node.getId());
            serialInput.putAttribute(orderIndex);
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 index = new UBNat32();
            serialOutput.process(index);

            return new XBRGroupSpec(client, index.getLong());
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<XBCGroupSpec> getGroupSpecs(XBCNode node) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(GROUPSPECS_NODE_PROCEDURE));
            serialInput.putAttribute(node.getId());
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            if (!serialOutput.pullIfEmptyBlock()) {
                serialInput.begin();
                serialInput.matchType(new XBFixedBlockType());
                long count = serialInput.pullLongAttribute();
                List<XBCGroupSpec> result = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    result.add(new XBRGroupSpec(client, serialInput.pullLongAttribute()));
                }
                serialInput.end();
                return result;
            }
            
            return null;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRBlockSpec findBlockSpecByXB(XBCNode node, long xbIndex) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(FINDBLOCKSPEC_NODE_PROCEDURE));
            serialInput.putAttribute(node.getId());
            serialInput.putAttribute(xbIndex);
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 index = new UBNat32();
            serialOutput.process(index);

            return new XBRBlockSpec(client, index.getLong());
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long findMaxBlockSpecXB(XBCNode node) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(MAXBLOCKSPEC_NODE_PROCEDURE));
            serialInput.putAttribute(node.getId());
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 count = new UBNat32();
            serialOutput.process(count);

            return count.getLong();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRGroupSpec findGroupSpecByXB(XBCNode node, long xbIndex) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(FINDGROUPSPEC_NODE_PROCEDURE));
            serialInput.putAttribute(node.getId());
            serialInput.putAttribute(xbIndex);
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 index = new UBNat32();
            serialOutput.process(index);

            return new XBRGroupSpec(client, index.getLong());
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long findMaxGroupSpecXB(XBCNode node) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(MAXGROUPSPEC_NODE_PROCEDURE));
            serialInput.putAttribute(node.getId());
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 count = new UBNat32();
            serialOutput.process(count);

            return count.getLong();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRFormatSpec findFormatSpecByXB(XBCNode node, long xbIndex) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(FINDFORMATSPEC_NODE_PROCEDURE));
            serialInput.putAttribute(node.getId());
            serialInput.putAttribute(xbIndex);
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 index = new UBNat32();
            serialOutput.process(index);

            return new XBRFormatSpec(client, index.getLong());
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long findMaxFormatSpecXB(XBCNode node) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(MAXFORMATSPEC_NODE_PROCEDURE));
            serialInput.putAttribute(node.getId());
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 count = new UBNat32();
            serialOutput.process(count);

            return count.getLong();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public long getFormatSpecsCount(XBCNode node) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(FORMATSPECSCOUNT_NODE_PROCEDURE));
            serialInput.putAttribute(node.getId());
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 count = new UBNat32();
            serialOutput.process(count);

            return count.getLong();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public long getGroupSpecsCount(XBCNode node) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(GROUPSPECSCOUNT_NODE_PROCEDURE));
            serialInput.putAttribute(node.getId());
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 count = new UBNat32();
            serialOutput.process(count);

            return count.getLong();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public long getBlockSpecsCount(XBCNode node) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(BLOCKSPECSCOUNT_NODE_PROCEDURE));
            serialInput.putAttribute(node.getId());
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 count = new UBNat32();
            serialOutput.process(count);

            return count.getLong();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public long getSpecsCount(XBCNode node) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(SPECSCOUNT_NODE_PROCEDURE));
            serialInput.putAttribute(node.getId());
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 count = new UBNat32();
            serialOutput.process(count);

            return count.getLong();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public long getDefsCount() {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(BINDSCOUNT_BIND_PROCEDURE));
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 count = new UBNat32();
            serialOutput.process(count);

            return count.getLong();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public XBRSpecDef getSpecDefByOrder(XBCSpec spec, long orderIndex) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(BIND_SPEC_PROCEDURE));
            serialInput.putAttribute(spec.getId());
            serialInput.putAttribute(orderIndex);
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            serialOutput.begin();
            serialOutput.matchType(new XBFixedBlockType());
            long index = serialOutput.pullLongAttribute();
            int bindType = serialOutput.pullIntAttribute();
            serialOutput.end();

            if (spec instanceof XBCFormatSpec) {
                return bindType == 0 ? new XBRFormatCons(client, index) : new XBRFormatJoin(client, index);
            } else if (spec instanceof XBCGroupSpec) {
                return bindType == 0 ? new XBRGroupCons(client, index) : new XBRGroupJoin(client, index);
            } else if (spec instanceof XBCBlockSpec) {
                switch (bindType) {
                    case 0:
                        return new XBRBlockCons(client, index);
                    case 1:
                        return new XBRBlockJoin(client, index);
                    case 2:
                        return new XBRBlockListCons(client, index);
                    case 3:
                        return new XBRBlockListJoin(client, index);
                }
            } else {
                return new XBRSpecDef(client, index);
            }
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRSpecDef findSpecDefByXB(XBCSpec spec, long xbIndex) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(FINDBIND_SPEC_PROCEDURE));
            serialInput.putAttribute(spec.getId());
            serialInput.putAttribute(xbIndex);
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            serialOutput.begin();
            serialOutput.matchType(new XBFixedBlockType());
            long index = serialOutput.pullLongAttribute();
            int bindType = serialOutput.pullIntAttribute();
            serialOutput.end();

            if (spec instanceof XBCFormatSpec) {
                return bindType == 0 ? new XBRFormatCons(client, index) : new XBRFormatJoin(client, index);
            } else if (spec instanceof XBCGroupSpec) {
                return bindType == 0 ? new XBRGroupCons(client, index) : new XBRGroupJoin(client, index);
            } else if (spec instanceof XBCBlockSpec) {
                switch (bindType) {
                    case 0:
                        return new XBRBlockCons(client, index);
                    case 1:
                        return new XBRBlockJoin(client, index);
                    case 2:
                        return new XBRBlockListCons(client, index);
                    case 3:
                        return new XBRBlockListJoin(client, index);
                }
            } else {
                return new XBRSpecDef(client, index);
            }
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<XBCSpecDef> getSpecDefs(XBCSpec spec) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(BINDS_SPEC_PROCEDURE));
            serialInput.putAttribute(spec.getId());
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            if (!serialOutput.pullIfEmptyBlock()) {
                serialInput.begin();
                serialInput.matchType(new XBFixedBlockType());
                long count = serialInput.pullLongAttribute();
                List<XBCSpecDef> result = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    result.add(new XBRSpecDef(client, serialInput.pullLongAttribute()));
                }
                serialInput.end();
                return result;
            }
            
            return null;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public long getSpecDefsCount(XBCSpec spec) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(BINDSCOUNT_SPEC_PROCEDURE));
            serialInput.putAttribute(spec.getId());
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 count = new UBNat32();
            serialOutput.process(count);

            return count.getLong();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
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
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(SPECSCOUNT_SPEC_PROCEDURE));
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 count = new UBNat32();
            serialOutput.process(count);

            return count.getLong();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}
