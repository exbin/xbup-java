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
import org.xbup.lib.core.catalog.base.XBCConsDef;
import org.xbup.lib.core.block.definition.XBParamType;
import org.xbup.lib.client.XBCatalogServiceClient;
import org.xbup.lib.client.XBCatalogServiceMessage;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.XBMatchingProvider;
import org.xbup.lib.core.remote.XBServiceClient;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 *
 * @version 0.1.22 2013/01/11
 * @author XBUP Project (http://xbup.org)
 */
public class XBRConsDef extends XBRSpecDef implements XBCConsDef {

    public XBRConsDef(XBCatalogServiceClient client, long id) {
        super(client, id);
    }

    @Override
    public XBRSpec getSpec() {
        XBRSpec item = super.getSpec();
        if (item == null) {
            return null;
        }
        return new XBRSpec(item.client, item.getId());
    }

    @Override
    public XBRRev getTarget() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.TARGET_BIND_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(getId()));
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

    @Override
    public XBParamType getType() {
        return XBParamType.CONSIST;
    }
}
