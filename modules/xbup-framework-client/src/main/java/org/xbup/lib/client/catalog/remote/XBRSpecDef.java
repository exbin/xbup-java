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
package org.xbup.lib.client.catalog.remote;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.catalog.base.XBCSpecDef;
import org.xbup.lib.core.block.definition.XBParamType;
import org.xbup.lib.client.XBCatalogServiceClient;
import org.xbup.lib.client.XBCatalogServiceMessage;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.remote.XBServiceClient;
import org.xbup.lib.core.stream.XBStreamChecker;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 *
 * @version 0.1.22 2013/01/11
 * @author XBUP Project (http://xbup.org)
 */
public class XBRSpecDef extends XBRItem implements XBCSpecDef {

    public XBRSpecDef(XBCatalogServiceClient client, long id) {
        super(client, id);
    }

    @Override
    public XBRSpec getSpec() {
        throw new UnsupportedOperationException("Not supported yet.");
/*        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.OWNER_ITEM_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long ownerId = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            if (ownerId == 0) return null;
            return new XBRItem(client, ownerId);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

        XBRItem item = super.getSpec();
        if (item == null) return null;
        return new XBRSpec(item.client,item.getId()); */
    }

    @Override
    public XBRRev getTarget() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.TARGET_BIND_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long target = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            if (target == 0) {
                return null;
            }
            return new XBRRev(client, target);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRSpecDef.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRSpecDef.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XBParamType getType() {
        // TODO
        return null;
    }
}
