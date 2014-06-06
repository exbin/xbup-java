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

import java.io.IOException;
import java.util.List;
import org.xbup.lib.xb.block.XBBlockType;
import org.xbup.lib.xb.block.declaration.XBBlockDecl;
import org.xbup.lib.xb.block.declaration.XBDBlockType;
import org.xbup.lib.xb.catalog.XBCatalog;
import org.xbup.lib.xb.catalog.base.XBCBlockSpec;
import org.xbup.lib.xb.catalog.base.XBCRev;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.serial.XBSerialHandler;
import org.xbup.lib.xb.serial.XBSerialMethod;
import org.xbup.lib.xb.serial.XBSerializationType;

/**
 * Block type context defined by catalog specification.
 *
 * @version 0.1 wr21.0 2011/12/03
 * @author XBUP Project (http://xbup.org)
 */
public class XBCSBlockDecl implements XBCBlockDecl {

    private XBCBlockSpec blockSpec;
    private int revision;

    public XBCSBlockDecl(XBCBlockSpec blockSpec, int revision) {
        this.blockSpec = blockSpec;
        this.revision = revision;
    }

    public XBCSBlockDecl(XBCBlockSpec blockSpec) {
        this(blockSpec, 0);
    }

    XBCSBlockDecl(XBCRev rev) {
        if (rev == null) {
            blockSpec = null;
            revision = 0;
        } else {
            blockSpec = (XBCBlockSpec) rev.getParent();
            revision = (int) (long) rev.getXBIndex();
        }
    }

    public long getAttribCount() {
        // TODO:
        return 1;
//        throw new UnsupportedOperationException("Not supported yet.");
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

    public boolean matchType(XBBlockType type) {
        if (type instanceof XBDBlockType) {
            return this.equals(((XBDBlockType) type).getBlockDecl());
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
        if (obj instanceof XBCSBlockDecl) {
            final XBCSBlockDecl other = (XBCSBlockDecl) obj;
            if (blockSpec == null || !this.blockSpec.equals(other.blockSpec)) {
                return false;
            }
            return blockSpec.getId() != other.blockSpec.getId();
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
/*
        nameManager = (XBCXNameManager) catalog.getExtension(XBCXNameManager.class);
        iconManager = (XBCXIconManager) catalog.getExtension(XBCXIconManager.class);
        fileManager = (XBCXFileManager) catalog.getExtension(XBCXFileManager.class);
    // TODO: Should be somewhere else
    public String getCaption() {
        if (nameManager==null) return ":";
        XBCXName name = nameManager.getItemName((XBCItem) getBlockSpec());
        if (name==null) return ":";
        return name.getText();
    }

    // TODO: Should be somewhere else
    public ImageIcon getDefaultIcon() {
        if (iconManager==null || fileManager==null) return null;
        XBCXIcon icon = iconManager.getDefaultIcon(getBlockSpec());
        if (icon == null) return null;
        XBCXFile file = icon.getIconFile();
        if (file == null) return null;
        return fileManager.getFileAsImageIcon(file);
    } */

    /**
     * @return the blockSpec
     */
    @Override
    public XBCBlockSpec getBlockSpec(XBCatalog catalog) {
        return blockSpec;
    }

    @Override
    public int getRevision() {
        return revision;
    }

    /**
     * @param blockSpec the blockSpec to set
     */
    public void setBlockSpec(XBCBlockSpec blockSpec) {
        this.blockSpec = blockSpec;
    }

    /**
     * @param revision the revision to set
     */
    public void setRevision(int revision) {
        this.revision = revision;
    }

    @Override
    public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
