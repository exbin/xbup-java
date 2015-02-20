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
import org.xbup.lib.core.catalog.base.XBCRev;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.manager.XBCRevManager;
import org.xbup.lib.client.XBCatalogServiceMessage;
import org.xbup.lib.client.catalog.remote.XBRBlockRev;
import org.xbup.lib.client.catalog.remote.XBRFormatRev;
import org.xbup.lib.client.catalog.remote.XBRGroupRev;
import org.xbup.lib.client.catalog.remote.XBRRev;
import org.xbup.lib.client.catalog.remote.XBRSpec;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.XBMatchingProvider;
import org.xbup.lib.core.remote.XBServiceClient;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Manager class for XBRRev catalog items.
 *
 * @version 0.1.21 2011/12/31
 * @author XBUP Project (http://xbup.org)
 */
public class XBRRevManager extends XBRDefaultManager<XBRRev> implements XBCRevManager<XBRRev> {

    public XBRRevManager(XBRCatalog catalog) {
        super(catalog);
    }

    @Override
    public long getItemsCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.REVSCOUNT_REV_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            Long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRRevManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRRevManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public XBRRev findRevByXB(XBCSpec spec, long xbIndex) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.FINDREV_SPEC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRSpec) spec).getId()));
            listener.attribXB(new UBNat32(xbIndex));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long bind = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return new XBRRev(client,bind);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRSpecManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRSpecManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XBRRev getRev(XBCSpec spec, long index) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.REV_SPEC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRSpec) spec).getId()));
            listener.attribXB(new UBNat32(index));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long rev = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            if (spec instanceof XBCBlockSpec) {
                return new XBRBlockRev(client,rev);
            }
            if (spec instanceof XBCGroupSpec) {
                return new XBRGroupRev(client,rev);
            }
            if (spec instanceof XBCFormatSpec) {
                return new XBRFormatRev(client,rev);
            }
            return new XBRRev(client,rev);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRSpecManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRSpecManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<XBCRev> getRevs(XBCSpec spec) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.REVSCOUNT_SPEC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRSpec) spec).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            List<XBCRev> result = new ArrayList<XBCRev>();
            long count = checker.matchAttribXB().getNaturalLong();
            for (int i = 0; i < count; i++) {
                result.add(new XBRRev(client,checker.matchAttribXB().getNaturalLong()));
            }
            checker.matchEndXB();
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
    public XBRRev findRevById(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long getAllRevisionsCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.REVSCOUNT_REV_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            Long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
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
    public long getRevsCount(XBCSpec spec) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.REVSCOUNT_SPEC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRSpec) spec).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
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
    public long getRevsLimitSum(XBCSpec spec, long revision) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long findMaxRevXB(XBCSpec spec) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
