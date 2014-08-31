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

import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.declaration.local.XBDBlockType;
import org.xbup.lib.core.catalog.base.XBCBlockRev;

/**
 * Block type context defined by catalog specification.
 *
 * @version 0.1.24 2014/08/29
 * @author XBUP Project (http://xbup.org)
 */
public class XBCBlockDecl implements XBBlockDecl {

    private XBCBlockRev blockSpec;

    public XBCBlockDecl(XBCBlockRev blockSpec) {
        this.blockSpec = blockSpec;
    }

    /* public boolean produceXBT() {
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

    public boolean matchType(XBBlockType type) {
        if (type instanceof XBDBlockType) {
            throw new UnsupportedOperationException("Not supported yet.");
            // return this.equals(((XBDBlockType) type).getBlockDecl());
        }

        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof XBBlockDecl)) {
            return false;
        }
        if (obj instanceof XBCBlockDecl) {
            final XBCBlockDecl other = (XBCBlockDecl) obj;
            if (blockSpec == null || !this.blockSpec.equals(other.blockSpec)) {
                return false;
            }

            return blockSpec.getId() != other.blockSpec.getId().longValue();
        } else {
            return false;
        } // TODO: return Arrays.equals(((XBBlockDecl) obj).getCatalogPath(), getCatalogPath());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (blockSpec != null ? blockSpec.hashCode() : 0);
        return hash;
    }

    public XBCBlockRev getBlockSpec() {
        return blockSpec;
    }
    
    public void setBlockSpec(XBCBlockRev blockSpec) {
        this.blockSpec = blockSpec;
    }
}
