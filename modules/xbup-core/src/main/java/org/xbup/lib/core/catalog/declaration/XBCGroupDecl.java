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
package org.xbup.lib.core.catalog.declaration;

import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.declaration.XBGroupDecl;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCRev;
import org.xbup.lib.core.catalog.base.XBCSpecDef;
import org.xbup.lib.core.catalog.base.service.XBCNodeService;
import org.xbup.lib.core.catalog.base.service.XBCRevService;
import org.xbup.lib.core.catalog.base.service.XBCSpecService;

/**
 * XBUP level 1 group specification.
 *
 * @version 0.1.22 2013/08/19
 * @author XBUP Project (http://xbup.org)
 */
public class XBCGroupDecl extends XBGroupDecl {

    private XBCatalog catalog;
    private XBCGroupSpec groupSpec;

    public XBCGroupDecl(XBCatalog catalog, XBCGroupSpec groupSpec) {
        this.catalog = catalog;
        this.groupSpec = groupSpec;
        populateBlocks();
    }

    public XBCGroupDecl(XBCatalog catalog, long[] path) {
        XBCNodeService nodeService = (XBCNodeService) catalog.getCatalogService(XBCNodeService.class);
        XBCSpecService specService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        this.catalog = catalog;
        Long[] longPath = new Long[path.length];
        for (int i = 0; i < path.length; i++) {
            longPath[i] = new Long(path[i]);
        }
        XBCNode node = (XBCNode) nodeService.findParentByXBPath(longPath);
        if (node != null) {
            this.groupSpec = specService.getGroupSpec(node, path[path.length -1]);
            populateBlocks();
        }
    }

    public boolean produceXBT() {
        throw new UnsupportedOperationException("Not supported yet.");
/*        try {
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
        } */
    }

    /**
     * @return the groupSpec
     */
    public XBCGroupSpec getGroupSpec() {
        return groupSpec;
    }

    /**
     * @param groupSpec the groupSpec to set
     */
    public void setGroupSpec(XBCGroupSpec groupSpec) {
        this.groupSpec = groupSpec;
    }

    private void populateBlocks() {
        XBCSpecService bindService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        XBCRevService revService = (XBCRevService) catalog.getCatalogService(XBCRevService.class);
        if (groupSpec == null) {
            return;
        }
        XBCRev rev = revService.getRev(groupSpec, getRevision());
        List<XBBlockDecl> blocks = new ArrayList<XBBlockDecl>();
        for (int i = 0; i <= rev.getXBLimit(); i++) {
            XBCSpecDef bind = bindService.findSpecDefByXB(groupSpec, i);
            if (bind != null) {
                XBCRev target = bind.getTarget();
                if (target != null) {
                    blocks.add(new XBCSBlockDecl((XBCBlockSpec) target.getParent()));
                } else {
                    blocks.add(null);
                }
            } else {
                blocks.add(null);
            }
            setBlocks(blocks);
        }
    }

    @Override
    public Integer matchType(XBBlockType type) {
        // TODO: Mixed definition (both catalog + internal)
        /*if (groupSpec != null) {
            List<XBBlockDecl> blocks = catalog.getBlocks(groupSpec);
            for (int blockId = 0; blockId < blocks.size(); blockId++) {
                if (((XBCBlockDecl) blocks.get(blockId)).matchType(type)) return blockId;
            }
            return null;
        } else */ return super.matchType(type);
    }
/*
    public void processSpec() {
        getBlocks().clear();
        XBGroupSpecificationBind elem;
        for (Iterator it = spec.getBlocks().iterator(); it.hasNext();) {
            elem = (XBGroupSpecificationBind) it.next();
            if (elem.getXbIndex()!=null) {
                if (getBlocks().size() <= elem.getXbIndex().intValue()) setListSize(elem.getXbIndex().intValue()+1); // TODO GetMaxXBIndex
                XBCTypeBlock typeBlock = new XBCTypeBlock(xbCatalog);
                getBlocks().set(elem.getXbIndex().intValue(),typeBlock);
                typeBlock.processSpec(elem.getHasBlock());
            }
        }
    }

    public void processSpec(XBGroupSpecification spec) {
        setSpec(spec);
        processSpec();
    }
*/
}
