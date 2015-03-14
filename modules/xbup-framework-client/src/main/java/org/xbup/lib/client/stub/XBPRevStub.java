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
import org.xbup.lib.client.catalog.remote.XBRBlockRev;
import org.xbup.lib.client.catalog.remote.XBRFormatRev;
import org.xbup.lib.client.catalog.remote.XBRGroupRev;
import org.xbup.lib.client.catalog.remote.XBRRev;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCFormatSpec;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;
import org.xbup.lib.core.catalog.base.XBCRev;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.remote.XBCallHandler;
import org.xbup.lib.core.serial.param.XBPListenerSerialHandler;
import org.xbup.lib.core.serial.param.XBPProviderSerialHandler;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC stub class for XBRRev catalog items.
 *
 * @version 0.1.25 2015/03/14
 * @author XBUP Project (http://xbup.org)
 */
public class XBPRevStub implements XBPManagerStub<XBRRev> {

    public static long[] FINDREV_SPEC_PROCEDURE = {0, 2, 5, 4, 0};
    public static long[] REVSCOUNT_SPEC_PROCEDURE = {0, 2, 5, 9, 0};
    public static long[] REV_SPEC_PROCEDURE = {0, 2, 5, 10, 0};
    public static long[] REVS_SPEC_PROCEDURE = {0, 2, 5, 11, 0};
    public static long[] XBLIMIT_REV_PROCEDURE = {0, 2, 7, 0, 0};
    public static long[] REVSCOUNT_REV_PROCEDURE = {0, 2, 7, 1, 0};

    private final XBCatalogServiceClient client;

    public XBPRevStub(XBCatalogServiceClient client) {
        this.client = client;
    }

    public Long getXBLimit(long revId) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(XBLIMIT_REV_PROCEDURE));
            serialInput.putAttribute(revId);
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 index = new UBNat32();
            serialOutput.process(index);

            return index.getLong();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPRevStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
                serialInput.begin();
                serialInput.matchType(new XBFixedBlockType());
                long count = serialInput.pullLongAttribute();
                List<XBCRev> result = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    long rev = serialInput.pullLongAttribute();
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
                serialInput.end();
                return result;
            }

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
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(REVSCOUNT_SPEC_PROCEDURE));
            serialInput.putAttribute(spec.getId());
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 count = new UBNat32();
            serialOutput.process(count);

            return count.getLong();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPRevStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public XBRRev createItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persistItem(XBRRev item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeItem(XBRRev item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRRev getItem(long itemId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<XBRRev> getAllItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getItemsCount() {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(REVSCOUNT_REV_PROCEDURE));
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 count = new UBNat32();
            serialOutput.process(count);

            return count.getLong();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPRevStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}
