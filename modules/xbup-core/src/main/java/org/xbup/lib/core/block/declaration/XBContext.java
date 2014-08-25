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
package org.xbup.lib.core.block.declaration;

import java.util.List;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockDataMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.XBTBlock;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.declaration.XBCDeclaration;
import org.xbup.lib.core.catalog.declaration.XBCFormatDecl;
import org.xbup.lib.core.catalog.declaration.XBCGroupDecl;

/**
 * Class representing current context for block type processing.
 *
 * Context include groups, blocks and their current numbers in current block.
 *
 * @version 0.1.24 2014/06/07
 * @author XBUP Project (http://xbup.org)
 */
public class XBContext {

    private XBContext parent;
    private final XBCatalog catalog;
    private XBDeclaration declaration;

    public XBContext() {
        parent = null;
        catalog = null;
        declaration = new XBDeclaration();
    }

    public XBContext(XBCatalog catalog) {
        parent = null;
        this.catalog = catalog;
        declaration = new XBDeclaration();
    }

    public XBContext(XBCatalog catalog, XBDeclaration declaration) {
        parent = null;
        this.catalog = catalog;
        this.declaration = declaration;
        if (declaration instanceof XBCDeclaration) {
            ((XBCDeclaration) declaration).setCatalog(catalog);
        }
    }

    /**
     * Provide static block type if possible.
     *
     * @param type block type
     * @return static block type
     */
    public XBFixedBlockType toStaticType(XBBlockType type) {
        if (type instanceof XBFixedBlockType) {
            return (XBFixedBlockType) type;
        }
        if (declaration == null) {
            if (parent != null) {
                return parent.toStaticType(type);
            }
        } else {
            XBFixedBlockType result;
            result = declaration.toStaticType(type);
            if ((result == null) && (parent != null)) {
                result = parent.toStaticType(type);
            }
            return result;
        }
        return null;
    }

    /**
     * Get BlockType for given block declaration in current context.
     * 
     * @param type block type declaration
     * @return  */
    public XBBlockType getBlockType(XBBlockDecl type) {
        throw new UnsupportedOperationException("Not supported yet.");
        /*        long grouId = 0;
         for (Iterator<XBL1FormatDecl> it = formats.iterator(); it.hasNext();) {
         XBL1FormatDecl formatDecl = ((XBL1FormatDecl) it.next());
         for (Iterator<XBL1GroupDecl> it1 = formatDecl.getGroups().iterator(); it1.hasNext();) {
         XBL1GroupDecl groupDecl = it1.next();
         int blockId = groupDecl.getBlocks().indexOf(type);
         if (blockId >= 0) return new XBL1SBlockDecl(grouId, blockId);
         grouId++;
         }
         }
         return null; */
    }

    public static XBContext processDeclaration(XBCatalog catalog, XBContext parent, XBTBlock specBlock) {
        if (specBlock.getDataMode() == XBBlockDataMode.NODE_BLOCK) {
            if (specBlock.getAttributesCount() > 1) {
                if ((specBlock.getBlockType().getGroupID().getLong() == 0) && (specBlock.getBlockType().getBlockID().getLong() == XBBasicBlockType.DECLARATION.ordinal())) {
                    XBContext context = new XBContext(catalog);
                    context.setParent(parent);
                    context.getDeclaration().setGroupsReserved(specBlock.getAttribute(0).getLong());
                    context.getDeclaration().setPreserveCount(specBlock.getAttribute(1).getLong());
                    if (specBlock.getChildCount() > 0) {
                        context.getDeclaration().setFormat(XBCFormatDecl.processFormatSpec(catalog, specBlock.getChildAt(0)));
                    }
                    return context;
                }
            }
        }
        return null;
    }

    /**
     * Get Block Type for specification defined by IDs.
     *
     * @param groupId
     * @param blockId
     * @return block type declaration
     */
    public XBBlockDecl getBlockType(int groupId, int blockId) {
        if (groupId == 0) {
            if (catalog == null) {
                return null;
            }

            long[] basicGroupPath = {0, 0};
            List<XBBlockDecl> list = new XBCGroupDecl(catalog, basicGroupPath).getBlocks();
            if (blockId >= list.size()) {
                return null;
            }

            return list.get(blockId);
        }

        return getDeclaration().getBlockType(groupId, blockId);
    }

    public XBDeclaration getParamDecl(XBBlockType blockType, int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public XBContext getParent() {
        return parent;
    }

    public void setParent(XBContext parent) {
        this.parent = parent;
    }

    public XBDeclaration getDeclaration() {
        return declaration;
    }

    public void setDeclaration(XBDeclaration declaration) {
        this.declaration = declaration;
    }
}
