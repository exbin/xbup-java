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

import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.declaration.XBDeclaration;
import org.xbup.lib.core.block.declaration.XBFormatDecl;
import org.xbup.lib.core.block.declaration.XBGroupDecl;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.serial.XBSerializationFromXB;

/**
 * Specification header declaration using catalog link.
 *
 * @version 0.1 wr21.0 2012/04/01
 * @author XBUP Project (http://xbup.org)
 */
public class XBCDeclaration extends XBDeclaration {

    private XBCatalog catalog;
//    private XBL1CTransNode transNode;

    /** Creates a new instance of XBCL1Declaration */
    public XBCDeclaration(XBFormatDecl format, XBCatalog catalog) {
        this.catalog = catalog;
        setContextFormat(format);
    }

    public XBCDeclaration(XBFormatDecl format) {
        setFormat(format);
    }

    public XBCDeclaration(XBFormatDecl format, XBSerializationFromXB rootNode) {
        setFormat(format);
        setRootNode(rootNode);
    }

    public XBCDeclaration(XBGroupDecl groupDecl) {
        XBFormatDecl formatDecl = new XBFormatDecl();
        formatDecl.setGroupsLimit(1);
        formatDecl.getGroups().add(groupDecl);
        setFormat(formatDecl);
    }

    public XBCDeclaration(XBBlockDecl blockDecl) {
        XBFormatDecl formatDecl = new XBFormatDecl();
        formatDecl.setGroupsLimit(1);
        XBGroupDecl groupDecl = new XBGroupDecl();
        groupDecl.setBlocksLimit(1);
        groupDecl.getBlocks().add(blockDecl);
        formatDecl.getGroups().add(groupDecl);
        setFormat(formatDecl);
    }

    public boolean produceXBT() {
        throw new UnsupportedOperationException("Not supported yet.");
/*
        try {
            eventListener.beginXBL1(false);
            eventListener.typeXBL1(new XBL1SBBlockDecl(XBBasicBlockTypeEnum.DOCUMENT_SPECIFICATION));
            long groupCount = 0;
            if (format instanceof XBCFormatDecl) {
                Long[] myPath = new Long[((XBCFormatDecl) format).getPath().length];
                for (int i = 0; i < ((XBCFormatDecl) format).getPath().length; i++) {
                    myPath[i] = new Long(((XBCFormatDecl) format).getPath()[i]);
                }
                XBEFormatSpec spec = ((XBECatalog) catalog).findFormatSpecByPath(myPath);
                groupCount = catalog.getSpecManager().getSpecDefsCount(spec);
            } else {
                groupCount = format.getGroups().size();
            }
            eventListener.attribXBL1(new UBNat32(groupCount)); // Count of reserved groups
            eventListener.attribXBL1(new UBNat32(1)); // Format Specification's Pointer
            eventListener.attribXBL1(new UBNat32(2)); // Document Root's Pointer
            format.generateXBL1(eventListener);
            format.produceXBT();
//            eventListener.endXBL1();
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBCDeclaration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBCDeclaration.class.getName()).log(Level.SEVERE, null, ex);
        } */
    }

    /** Transform dynamic block type to static */
    @Override
    public XBFixedBlockType toStaticType(XBBlockType type) {
        if (type instanceof XBFixedBlockType) {
            return (XBFixedBlockType) type;
        }
        if (type instanceof XBCBlockDecl) {
/* TODO:            Long[] myPath = new Long[((XBCBlockDecl) type).getCatalogPath().length];
            for (int i = 0; i < ((XBCBlockDecl) type).getCatalogPath().length; i++) {
                myPath[i] = new Long(((XBCBlockDecl) type).getCatalogPath()[i]);
            }
            XBFixedBlockType newType = new XBL1DBlockType(myPath);
            if (newType != null) {
                return newType;
            } else {
                return type;
            } */
//        } else {
//            return type;
        } else if (type instanceof XBBlockType) { // TODO: Replace dumb search for Maps
            XBFormatDecl format = getFormat();
            if (format == null) {
                return null;
            }
            if (format instanceof XBCFormatDecl) {
                return ((XBCFormatDecl) format).toStaticType(type);
            }
            XBCFormatDecl catFormat = new XBCFormatDecl(catalog);
            catFormat.setCatalogPath(format.getCatalogPath());
            return catFormat.toStaticType(type);
        }
        return null;
    }

    public XBCFormatDecl getContextFormat() {
        if (getFormat() instanceof XBCFormatDecl) {
            return (XBCFormatDecl) getFormat();
        }
        return null;
    }

    public XBCGroupDecl getContextGroup() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public XBCBlockDecl getContextBlock() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setContextFormat(XBFormatDecl format) {
        setFormat(format);
        prepareTrans();
    }

    public void setContextGroup(XBGroupDecl group) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setContextBlock(XBBlockDecl block) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public XBCatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(XBCatalog catalog) {
        this.catalog = catalog;
        prepareTrans();
    }

    private void prepareTrans() {
        throw new UnsupportedOperationException("Not supported yet.");
        /*
        XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
        XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
        XBFormatDecl format = getFormat();
        if (format instanceof XBCFormatDecl && catalog != null) {
//            transNode = new XBL1CTransNode();
            Long[] longPath = ((XBCFormatDecl) format).getCatalogPath().getLongPath();
            Long[] myPath = new Long[longPath.length];
            for (int i = 0; i < longPath.length; i++) {
                myPath[i] = new Long(longPath[i]);
            }
            XBEFormatSpec spec = ((XBECatalog) catalog).findFormatSpecByPath(myPath);
            if (spec == null) {
                return;
            }
            for (Iterator it = specService.getSpecDefs(spec).iterator(); it.hasNext();) {
                XBESpecDef bind = (XBESpecDef) it.next();
                XBEGroupSpec group = (XBEGroupSpec) bind.getTarget();
                for (Iterator it2 = specService.getSpecDefs(group).iterator(); it2.hasNext();) {
                    XBESpecDef bind2 = ((XBESpecDef) it2.next());
                    XBEBlockSpec block = (XBEBlockSpec) bind2.getTarget();
                    XBENode node = block.getParent();
                    Long[] blockNodePath = nodeService.getNodeXBPath(node);
                    Long[] blockPath = new Long[blockNodePath.length + 1];
                    System.arraycopy(blockNodePath, 0, blockPath, 0, blockNodePath.length);
                    blockPath[blockPath.length - 1] = block.getXBIndex();
//                    transNode.addBlock(blockPath, new XBFixedBlockType(bind.getXBIndex()+1, bind2.getXBIndex()));
                }
            }
        }
        */
    // TODO
    }
}
