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
package org.xbup.lib.xb.catalog.declaration;

import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.xb.block.XBBasicBlockType;
import org.xbup.lib.xb.block.XBBlockDataMode;
import org.xbup.lib.xb.block.XBBlockType;
import org.xbup.lib.xb.block.XBFixedBlockType;
import org.xbup.lib.xb.block.XBTBlock;
import org.xbup.lib.xb.block.declaration.XBFormatDecl;
import org.xbup.lib.xb.block.declaration.XBGroupDecl;
import org.xbup.lib.xb.catalog.XBCatalog;
import org.xbup.lib.xb.catalog.base.XBCFormatSpec;
import org.xbup.lib.xb.catalog.base.XBCGroupSpec;
import org.xbup.lib.xb.catalog.base.XBCNode;
import org.xbup.lib.xb.catalog.base.XBCRev;
import org.xbup.lib.xb.catalog.base.XBCSpecDef;
import org.xbup.lib.xb.catalog.base.service.XBCNodeService;
import org.xbup.lib.xb.catalog.base.service.XBCRevService;
import org.xbup.lib.xb.catalog.base.service.XBCSpecService;
import org.xbup.lib.xb.parser.basic.XBTListener;
import org.xbup.lib.xb.ubnumber.type.UBPath32;

/**
 * XBUP level 1 format declaration represented by catalog.
 *
 * @version 0.1 wr21.0 2011/12/31
 * @author XBUP Project (http://xbup.org)
 */
public class XBCFormatDecl extends XBFormatDecl {

    private XBCatalog catalog;
    private XBCFormatSpec formatSpec;
    private XBTListener eventListener;

    public XBCFormatDecl(XBCatalog catalog) {
        this.catalog = catalog;
    }

    public XBCFormatDecl(XBCatalog catalog, XBCFormatSpec formatSpec) {
        this.catalog = catalog;
        setFormatSpec(formatSpec);
    }

    public boolean produceXBT() {
        throw new UnsupportedOperationException("Not supported yet.");
        /*        try {
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
         } */
    }

    public static XBFormatDecl processFormatSpec(XBCatalog catalog, XBTBlock specBlock) {
        // TODO: Provide nonstatic method to call super
        XBCNodeService nodeService = (XBCNodeService) catalog.getCatalogService(XBCNodeService.class);
        XBCSpecService specService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        if (specBlock.getDataMode() == XBBlockDataMode.NODE_BLOCK) {
            if (specBlock.getAttributesCount() > 1) {
                if ((specBlock.getBlockType().getGroupID().getLong() == 0) && (specBlock.getBlockType().getBlockID().getLong() == XBBasicBlockType.FORMAT_DECLARATION.ordinal())) {
                    XBCFormatDecl decl = new XBCFormatDecl(catalog);
                    decl.setGroupsLimit(specBlock.getAttribute(0).getLong());
                    Long[] catalogPath = new Long[specBlock.getAttribute(1).getInt()];
                    int i;
                    for (i = 0; i < catalogPath.length; i++) {
                        catalogPath[i] = specBlock.getAttribute(i + 2).getLong();
                    }
                    XBCNode node = nodeService.findParentByXBPath(catalogPath);
                    if (node != null) {
                        decl.setFormatSpec(specService.findFormatSpecByXB(node, catalogPath[catalogPath.length - 1]));
                    }
                    decl.setRevision(specBlock.getAttribute(i + 2).getLong());
                    return decl;
                }
            }
        }
        return null;
    }

    @Override
    public XBFixedBlockType toStaticType(XBBlockType type) {
        // TODO: Mixed definition (both catalog + internal)
        if (formatSpec != null) {
            List<XBGroupDecl> groups = catalog.getGroups(formatSpec);
            for (int groupId = 0; groupId < groups.size(); groupId++) {
                Integer blockId = ((XBCGroupDecl) groups.get(groupId)).matchType(type);
                if (blockId != null) {
                    return new XBFixedBlockType(groupId, blockId);
                }
            }
            return null;
        } else {
            return super.toStaticType(type);
        }
    }

    @Override
    public void setCatalogPath(UBPath32 catalogPath) {
        XBCNodeService nodeService = (XBCNodeService) catalog.getCatalogService(XBCNodeService.class);
        XBCSpecService specService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        super.setCatalogPath(catalogPath);
        Long[] longPath = catalogPath.getLongPath();
        setFormatSpec(specService.findFormatSpecByXB(nodeService.findParentByXBPath(longPath), longPath[longPath.length - 1]));
    }

    /**
     * @return the formatSpec
     */
    public XBCFormatSpec getFormatSpec() {
        return formatSpec;
    }

    /**
     * @param formatSpec the formatSpec to set
     */
    public void setFormatSpec(XBCFormatSpec formatSpec) {
        XBCSpecService bindService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        XBCRevService revService = (XBCRevService) catalog.getCatalogService(XBCRevService.class);
        this.formatSpec = formatSpec;
        if (formatSpec == null) {
            return;
        }
        XBCRev rev = revService.getRev(formatSpec, getRevision());
        List<XBGroupDecl> groups = new ArrayList<XBGroupDecl>();
        for (int i = 0; i <= rev.getXBLimit(); i++) {
            XBCSpecDef bind = bindService.findSpecDefByXB(formatSpec, i);
            if (bind != null) {
                XBCRev target = bind.getTarget();
                if (target != null) {
                    groups.add(new XBCGroupDecl(catalog, (XBCGroupSpec) target.getParent()));
                } else {
                    groups.add(null);
                }
            } else {
                groups.add(null);
            }
        }
        setGroups(groups);
    }

    /*
     public long getGroupsCount() {
     Long count = catalog.getSpecManager().getSpecDefsCount(spec);
     if (count==0) return 0;
     return count.longValue();
     }

     public XBL1BlockDecl getBlockType(int group, int block) {
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
}
