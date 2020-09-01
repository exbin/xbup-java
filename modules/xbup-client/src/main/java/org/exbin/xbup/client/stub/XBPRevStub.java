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
import org.exbin.xbup.client.catalog.remote.XBRBlockRev;
import org.exbin.xbup.client.catalog.remote.XBRFormatRev;
import org.exbin.xbup.client.catalog.remote.XBRGroupRev;
import org.exbin.xbup.client.catalog.remote.XBRRev;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.XBCFormatSpec;
import org.exbin.xbup.core.catalog.base.XBCGroupSpec;
import org.exbin.xbup.core.catalog.base.XBCRev;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.remote.XBCallHandler;
import org.exbin.xbup.core.serial.param.XBPListenerSerialHandler;
import org.exbin.xbup.core.serial.param.XBPProviderSerialHandler;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * RPC stub class for XBRRev catalog items.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBPRevStub extends XBPBaseStub<XBRRev> {

    public static long[] FINDREV_SPEC_PROCEDURE = {0, 2, 5, 4, 0};
    public static long[] REVSCOUNT_SPEC_PROCEDURE = {0, 2, 5, 9, 0};
    public static long[] REV_SPEC_PROCEDURE = {0, 2, 5, 10, 0};
    public static long[] REVS_SPEC_PROCEDURE = {0, 2, 5, 11, 0};
    public static long[] XBLIMIT_REV_PROCEDURE = {0, 2, 7, 0, 0};
    public static long[] REVSCOUNT_REV_PROCEDURE = {0, 2, 7, 1, 0};

    private final XBCatalogServiceClient client;

    public XBPRevStub(XBCatalogServiceClient client) {
        super(client, XBRRev::new, new XBPBaseProcedureType(null, null, null, null, new XBDeclBlockType(REVSCOUNT_REV_PROCEDURE)));
        this.client = client;
    }

    public Long getXBLimit(long revId) {
        return XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(XBLIMIT_REV_PROCEDURE), revId);
    }

    public XBRRev findRevByXB(XBCSpec spec, long xbIndex) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(FINDREV_SPEC_PROCEDURE));
            serialInput.putAttribute(spec.getId());
            serialInput.putAttribute(xbIndex);
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 index = new UBNat32();
            serialOutput.process(index);
            procedureCall.execute();

            long rev = index.getLong();
            if (spec instanceof XBCBlockSpec) {
                return new XBRBlockRev(client, rev);
            }
            if (spec instanceof XBCGroupSpec) {
                return new XBRGroupRev(client, rev);
            }
            if (spec instanceof XBCFormatSpec) {
                return new XBRFormatRev(client, rev);
            }
            return new XBRRev(client, rev);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPRevStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRRev getRev(XBCSpec spec, long orderIndex) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(REV_SPEC_PROCEDURE));
            serialInput.putAttribute(spec.getId());
            serialInput.putAttribute(orderIndex);
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 index = new UBNat32();
            serialOutput.process(index);
            procedureCall.execute();

            long rev = index.getLong();
            if (spec instanceof XBCBlockSpec) {
                return new XBRBlockRev(client, rev);
            }
            if (spec instanceof XBCGroupSpec) {
                return new XBRGroupRev(client, rev);
            }
            if (spec instanceof XBCFormatSpec) {
                return new XBRFormatRev(client, rev);
            }
            return new XBRRev(client, rev);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPRevStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<XBCRev> getRevs(XBCSpec spec) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(REVS_SPEC_PROCEDURE));
            serialInput.putAttribute(spec.getId());
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            if (!serialOutput.pullIfEmptyBlock()) {
                serialOutput.begin();
                serialOutput.matchType();
                long count = serialOutput.pullLongAttribute();
                List<XBCRev> result = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    long rev = serialOutput.pullLongAttribute();
                    if (spec instanceof XBCBlockSpec) {
                        result.add(new XBRBlockRev(client, rev));
                    } else if (spec instanceof XBCGroupSpec) {
                        result.add(new XBRGroupRev(client, rev));
                    } else if (spec instanceof XBCFormatSpec) {
                        result.add(new XBRFormatRev(client, rev));
                    } else {
                        result.add(new XBRRev(client, rev));
                    }
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

    public XBRRev findRevById(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getRevsCount(XBCSpec spec) {
        return XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(REVSCOUNT_REV_PROCEDURE), spec.getId());
    }
}
