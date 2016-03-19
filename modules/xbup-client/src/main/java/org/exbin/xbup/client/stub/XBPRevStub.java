/*
 * Copyright (C) ExBin Project
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
package org.exbin.xbup.client.stub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * @version 0.1.25 2015/03/25
 * @author ExBin Project (http://exbin.org)
 */
public class XBPRevStub extends XBPBaseStub<XBRRev> {

    public static long[] FINDREV_SPEC_PROCEDURE = {0, 2, 5, 4, 0};
    public static long[] REVSCOUNT_SPEC_PROCEDURE = {0, 2, 5, 9, 0};
    public static long[] REV_SPEC_PROCEDURE = {0, 2, 5, 10, 0};
    public static long[] REVS_SPEC_PROCEDURE = {0, 2, 5, 11, 0};
    public static long[] XBLIMIT_REV_PROCEDURE = {0, 2, 7, 0, 0};
    public static long[] REVSCOUNT_REV_PROCEDURE = {0, 2, 7, 1, 0};

    private final XBCatalogServiceClient client;

    public XBPRevStub(XBCatalogServiceClient client) {
        super(client, new XBPConstructorMethod<XBRRev>() {
            @Override
            public XBRRev itemConstructor(XBCatalogServiceClient client, long itemId) {
                return new XBRRev(client, itemId);
            }
        }, new XBPBaseProcedureType(null, null, null, null, new XBDeclBlockType(REVSCOUNT_REV_PROCEDURE)));
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
