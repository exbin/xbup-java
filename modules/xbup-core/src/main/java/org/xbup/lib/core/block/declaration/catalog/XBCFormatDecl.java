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

import org.xbup.lib.core.block.declaration.XBFormatDecl;
import org.xbup.lib.core.catalog.base.XBCFormatRev;

/**
 * XBUP level 1 format declaration represented by catalog.
 *
 * @version 0.1.24 2014/08/29
 * @author XBUP Project (http://xbup.org)
 */
public class XBCFormatDecl implements XBFormatDecl {

    private XBCFormatRev formatSpec;

    public XBCFormatDecl(XBCFormatRev formatSpec) {
        this.formatSpec = formatSpec;
    }

    /* public boolean produceXBT() {
        throw new UnsupportedOperationException("Not supported yet.");
                try {
         eventListener.beginXBL1(false);
         eventListener.typeXBL1(new XBL1SBBlockDecl(XBBasicBlockType.GROUP_CATALOG_LINK));
         eventListener.attribXBL1(new UBNat32(path.length-1));
         for (int i = 0; i < path.length; i++) {
         eventListener.attribXBL1(new UBNat32(path[i]));
         }
         eventListener.endXBL1();
         } catch (XBProcessingException ex) {
         Logger.getLogger(XBCFormatDecl.class.getName()).log(Level.SEVERE, null, ex);
         } catch (IOException ex) {
         Logger.getLogger(XBCFormatDecl.class.getName()).log(Level.SEVERE, null, ex);
         }
    } */

    /*public XBL1BlockDecl getBlockType(int group, int block) {
     if ((spec.getXBIndex().intValue()>0)||(spec.getParent().getParent()!=null)) { // Not root context
     if (group==0) return catalog.getRootContext().getBlockType(group,block);
     group--;
     }
     XBCSpecDef formatBind = (XBCSpecDef) catalog.getSpecManager().findSpecDefByXB(spec,group);
     if (formatBind == null) return null;
     XBCSpec groupSpec = (XBCSpec) formatBind.getTarget();
     if (groupSpec == null) return null;
     XBCSpecDef groupBind = (XBCSpecDef) catalog.getSpecManager().findSpecDefByXB(groupSpec,block);
     if (groupBind == null) return null;
     XBCSpec blockSpec = (XBCSpec) groupBind.getTarget();
     if (blockSpec == null) return null;
     return new XBL1CSBlockDecl(catalog, (XBCBlockSpec) blockSpec);
     }

     public void toXB(XBL0Listener target) throws XBProcessingException, IOException {
     target.beginXBL0(false);
     // TODO: DocumentSpecification - replace with relevant code later
     target.attribXBL0(new UBNat32(0));
     target.attribXBL0(new UBNat32(0));
     target.attribXBL0(new UBNat32(getGroupsCount()));
     target.attribXBL0(new UBNat32(1));
     target.attribXBL0(new UBNat32(2));
     target.beginXBL0(true);
     // TODO: Format Specification in catalog
     target.attribXBL0(new UBNat32(0));
     target.attribXBL0(new UBNat32(6));
     // TODO: UBPath type
     Long[] path = catalog.getNodeManager().getNodeXBPath(spec.getParent());
     target.attribXBL0(new UBNat32(path.length));
     for (int i = 0; i < path.length; i++) {
     target.attribXBL0(new UBNat32(path[i]));
     }
     target.attribXBL0(new UBNat32(spec.getXBIndex()));
     target.endXBL0();
     }
     */

    public XBCFormatRev getFormatSpec() {
        return formatSpec;
    }

    public void setFormatSpec(XBCFormatRev formatSpec) {
        this.formatSpec = formatSpec;
    }
}
