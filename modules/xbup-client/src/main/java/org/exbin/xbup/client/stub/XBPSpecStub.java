/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.client.stub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.client.catalog.remote.XBRBlockCons;
import org.exbin.xbup.client.catalog.remote.XBRBlockJoin;
import org.exbin.xbup.client.catalog.remote.XBRBlockListCons;
import org.exbin.xbup.client.catalog.remote.XBRBlockListJoin;
import org.exbin.xbup.client.catalog.remote.XBRBlockSpec;
import org.exbin.xbup.client.catalog.remote.XBRFormatCons;
import org.exbin.xbup.client.catalog.remote.XBRFormatJoin;
import org.exbin.xbup.client.catalog.remote.XBRFormatSpec;
import org.exbin.xbup.client.catalog.remote.XBRGroupCons;
import org.exbin.xbup.client.catalog.remote.XBRGroupJoin;
import org.exbin.xbup.client.catalog.remote.XBRGroupSpec;
import org.exbin.xbup.client.catalog.remote.XBRRev;
import org.exbin.xbup.client.catalog.remote.XBRSpec;
import org.exbin.xbup.client.catalog.remote.XBRSpecDef;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.XBCFormatSpec;
import org.exbin.xbup.core.catalog.base.XBCGroupSpec;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.XBCSpecDef;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.remote.XBCallHandler;
import org.exbin.xbup.core.serial.param.XBPListenerSerialHandler;
import org.exbin.xbup.core.serial.param.XBPProviderSerialHandler;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * RPC stub class for XBRSpec catalog items.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBPSpecStub extends XBPBaseStub<XBCSpec> {

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
        super(client, XBRSpec::new, new XBPBaseProcedureType(null, null, null, null, new XBDeclBlockType(SPECSCOUNT_SPEC_PROCEDURE)));
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
            procedureCall.execute();

            // TODO type
            return target.isZero() ? null : new XBRRev(client, target.getLong());
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long getAllSpecsCount() {
        return XBPStubUtils.voidToLongMethod(client.procedureCall(), new XBDeclBlockType(SPECSCOUNT_SPEC_PROCEDURE));
    }

    public Long getAllFormatSpecsCount() {
        return XBPStubUtils.voidToLongMethod(client.procedureCall(), new XBDeclBlockType(FORMATSPECSCOUNT_SPEC_PROCEDURE));
    }

    public Long getAllGroupSpecsCount() {
        return XBPStubUtils.voidToLongMethod(client.procedureCall(), new XBDeclBlockType(GROUPSPECSCOUNT_SPEC_PROCEDURE));
    }

    public Long getAllBlockSpecsCount() {
        return XBPStubUtils.voidToLongMethod(client.procedureCall(), new XBDeclBlockType(BLOCKSPECSCOUNT_SPEC_PROCEDURE));
    }

    public Long[] getSpecXBPath(XBCSpec node) {
        ArrayList<Long> list = new ArrayList<>();
        XBCNode parent = node.getParent();
        while (parent != null) {
            if (parent.getParent().isPresent()) {
                list.add(0, parent.getXBIndex());
            }
            parent = parent.getParent().orElse(null);
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
                serialOutput.begin();
                serialOutput.matchType();
                long count = serialOutput.pullLongAttribute();
                List<XBCSpec> result = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    result.add(new XBRSpec(client, serialOutput.pullLongAttribute()));
                }
                serialOutput.end();
                procedureCall.execute();
                return result;
            }

            procedureCall.execute();
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
            procedureCall.execute();

            // TODO type?
            return new XBRSpec(client, index.getLong());
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPSpecStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRFormatSpec getFormatSpec(XBCNode node, long orderIndex) {
        Long index = XBPStubUtils.twoLongsToLongMethod(client.procedureCall(), new XBDeclBlockType(FORMATSPEC_NODE_PROCEDURE), node.getId(), orderIndex);
        return index == null ? null : new XBRFormatSpec(client, index);
    }

    public XBRGroupSpec getGroupSpec(XBCNode node, long orderIndex) {
        Long index = XBPStubUtils.twoLongsToLongMethod(client.procedureCall(), new XBDeclBlockType(GROUPSPEC_NODE_PROCEDURE), node.getId(), orderIndex);
        return index == null ? null : new XBRGroupSpec(client, index);
    }

    public XBRBlockSpec getBlockSpec(XBCNode node, long orderIndex) {
        Long index = XBPStubUtils.twoLongsToLongMethod(client.procedureCall(), new XBDeclBlockType(BLOCKSPEC_NODE_PROCEDURE), node.getId(), orderIndex);
        return index == null ? null : new XBRBlockSpec(client, index);
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
                serialOutput.begin();
                serialOutput.matchType();
                long count = serialOutput.pullLongAttribute();
                List<XBCFormatSpec> result = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    result.add(new XBRFormatSpec(client, serialOutput.pullLongAttribute()));
                }
                serialOutput.end();
                procedureCall.execute();
                return result;
            }

            procedureCall.execute();
            return null;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
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
                serialOutput.begin();
                serialOutput.matchType();
                long count = serialOutput.pullLongAttribute();
                List<XBCGroupSpec> result = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    result.add(new XBRGroupSpec(client, serialOutput.pullLongAttribute()));
                }
                serialOutput.end();
                procedureCall.execute();
                return result;
            }

            procedureCall.execute();
            return null;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
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
                serialOutput.begin();
                serialOutput.matchType();
                long count = serialOutput.pullLongAttribute();
                List<XBCBlockSpec> result = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    result.add(new XBRBlockSpec(client, serialOutput.pullLongAttribute()));
                }
                serialOutput.end();
                procedureCall.execute();
                return result;
            }

            procedureCall.execute();
            return null;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRFormatSpec findFormatSpecByXB(XBCNode node, long xbIndex) {
        Long index = XBPStubUtils.twoLongsToLongMethod(client.procedureCall(), new XBDeclBlockType(FINDFORMATSPEC_NODE_PROCEDURE), node.getId(), xbIndex);
        return index == null ? null : new XBRFormatSpec(client, index);
    }

    public XBRGroupSpec findGroupSpecByXB(XBCNode node, long xbIndex) {
        Long index = XBPStubUtils.twoLongsToLongMethod(client.procedureCall(), new XBDeclBlockType(FINDGROUPSPEC_NODE_PROCEDURE), node.getId(), xbIndex);
        return index == null ? null : new XBRGroupSpec(client, index);
    }

    public XBRBlockSpec findBlockSpecByXB(XBCNode node, long xbIndex) {
        Long index = XBPStubUtils.twoLongsToLongMethod(client.procedureCall(), new XBDeclBlockType(FINDBLOCKSPEC_NODE_PROCEDURE), node.getId(), xbIndex);
        return index == null ? null : new XBRBlockSpec(client, index);
    }

    public Long findMaxFormatSpecXB(XBCNode node) {
        return XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(MAXFORMATSPEC_NODE_PROCEDURE), node.getId());
    }

    public Long findMaxGroupSpecXB(XBCNode node) {
        return XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(MAXGROUPSPEC_NODE_PROCEDURE), node.getId());
    }

    public Long findMaxBlockSpecXB(XBCNode node) {
        return XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(MAXBLOCKSPEC_NODE_PROCEDURE), node.getId());
    }

    public long getFormatSpecsCount(XBCNode node) {
        return XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(FORMATSPECSCOUNT_NODE_PROCEDURE), node.getId());
    }

    public long getGroupSpecsCount(XBCNode node) {
        return XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(GROUPSPECSCOUNT_NODE_PROCEDURE), node.getId());
    }

    public long getBlockSpecsCount(XBCNode node) {
        return XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(BLOCKSPECSCOUNT_NODE_PROCEDURE), node.getId());
    }

    public long getSpecsCount(XBCNode node) {
        return XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(SPECSCOUNT_NODE_PROCEDURE), node.getId());
    }

    public long getDefsCount() {
        return XBPStubUtils.voidToLongMethod(client.procedureCall(), new XBDeclBlockType(BINDSCOUNT_BIND_PROCEDURE));
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
            serialOutput.matchType();
            long index = serialOutput.pullLongAttribute();
            int bindType = serialOutput.pullIntAttribute();
            serialOutput.end();
            procedureCall.execute();

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
            serialOutput.matchType();
            long index = serialOutput.pullLongAttribute();
            int bindType = serialOutput.pullIntAttribute();
            serialOutput.end();
            procedureCall.execute();

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
                serialOutput.begin();
                serialOutput.matchType();
                long count = serialOutput.pullLongAttribute();
                List<XBCSpecDef> result = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    result.add(new XBRSpecDef(client, serialOutput.pullLongAttribute()));
                }
                serialOutput.end();
                procedureCall.execute();
                return result;
            }

            procedureCall.execute();
            return null;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public long getSpecDefsCount(XBCSpec spec) {
        return XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(BINDSCOUNT_SPEC_PROCEDURE), spec.getId());
    }
}
