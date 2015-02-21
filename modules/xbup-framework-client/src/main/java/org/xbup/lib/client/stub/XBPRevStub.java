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
import org.xbup.lib.client.catalog.remote.XBRBlockRev;
import org.xbup.lib.client.catalog.remote.XBRFormatRev;
import org.xbup.lib.client.catalog.remote.XBRGroupRev;
import org.xbup.lib.client.catalog.remote.XBRRev;
import org.xbup.lib.client.catalog.remote.XBRSpec;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCFormatSpec;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;
import org.xbup.lib.core.catalog.base.XBCRev;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.XBMatchingProvider;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC stub class for XBRRev catalog items.
 *
 * @version 0.1.25 2015/02/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBPRevStub implements XBPManagerStub<XBRRev> {

    public static long[] FINDREV_SPEC_PROCEDURE = {0, 2, 5, 4, 0};
    public static long[] REVSCOUNT_SPEC_PROCEDURE = {0, 2, 5, 9, 0};
    public static long[] REV_SPEC_PROCEDURE = {0, 2, 5, 10, 0};
    public static long[] XBLIMIT_REV_PROCEDURE = {0, 2, 7, 0, 0};
    public static long[] REVSCOUNT_REV_PROCEDURE = {0, 2, 7, 1, 0};

    private final XBCatalogServiceClient client;

    public XBPRevStub(XBCatalogServiceClient client) {
        this.client = client;
    }

    public Long getXBLimit(long revId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBLIMIT_REV_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(revId));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPRevStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRRev findRevByXB(XBCSpec spec, long xbIndex) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(FINDREV_SPEC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRSpec) spec).getId()));
            listener.attribXB(new UBNat32(xbIndex));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long bind = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return new XBRRev(client, bind);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPRevStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRRev getRev(XBCSpec spec, long index) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(REV_SPEC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRSpec) spec).getId()));
            listener.attribXB(new UBNat32(index));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long rev = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
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
            XBCatalogServiceMessage message = client.executeProcedure(REVSCOUNT_SPEC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRSpec) spec).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            List<XBCRev> result = new ArrayList<>();
            long count = checker.matchAttribXB().getNaturalLong();
            for (int i = 0; i < count; i++) {
                result.add(new XBRRev(client, checker.matchAttribXB().getNaturalLong()));
            }
            checker.matchEndXB();
            message.close();
            return result;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPRevStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRRev findRevById(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getRevsCount(XBCSpec spec) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(REVSCOUNT_SPEC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRSpec) spec).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
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
            XBCatalogServiceMessage message = client.executeProcedure(REVSCOUNT_REV_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            Long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPRevStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}
