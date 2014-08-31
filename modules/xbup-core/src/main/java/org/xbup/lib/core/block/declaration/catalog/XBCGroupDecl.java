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
package org.xbup.lib.core.block.declaration.catalog;

import org.xbup.lib.core.block.declaration.XBGroupDecl;
import org.xbup.lib.core.catalog.base.XBCGroupRev;

/**
 * XBUP level 1 group specification.
 *
 * @version 0.1.24 2014/08/29
 * @author XBUP Project (http://xbup.org)
 */
public class XBCGroupDecl implements XBGroupDecl {

    private XBCGroupRev groupSpec;

    public XBCGroupDecl(XBCGroupRev groupSpec) {
        this.groupSpec = groupSpec;
    }

/*    public boolean produceXBT() {
        throw new UnsupportedOperationException("Not supported yet.");
        try {
            eventListener.beginXBL1(false);
            eventListener.typeXBL1(new XBL1SBBlockDecl(XBBasicBlockTypeEnum.BLOCK_CATALOG_LINK));
            eventListener.attribXBL1(new UBNat32(path.length-1));
            for (int i = 0; i < path.length; i++) {
                eventListener.attribXBL1(new UBNat32(path[i]));
            }
            eventListener.endXBL1();
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBL1CFormatDecl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBL1CFormatDecl.class.getName()).log(Level.SEVERE, null, ex);
        }
    } */

    public XBCGroupRev getGroupSpec() {
        return groupSpec;
    }

    public void setGroupSpec(XBCGroupRev groupSpec) {
        this.groupSpec = groupSpec;
    }
}
