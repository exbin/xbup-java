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

import java.util.Arrays;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;

/**
 * XBUP level 1 block declaration using catalog path.
 *
 * @version 0.1.21 2011/12/03
 * @author XBUP Project (http://xbup.org)
 */
public class XBCPBlockDecl implements XBCBlockDecl {

    private long[] catalogPath;
    private int revision;

    public XBCPBlockDecl() {
        catalogPath = null;
    }

    public XBCPBlockDecl(long[] path) {
        this.catalogPath = path;
    }

    public XBCPBlockDecl(Long[] path) {
        setCatalogPath(path);
    }

    public void setCatalogPath(Long[] path) {
        setCatalogPath(new long[path.length]);
        for (int i = 0; i < path.length; i++) {
            getCatalogPath()[i] = path[i];
        }
    }

/*    public List<XBAttributeType> getAttrList() {
        List<XBAttributeType> list = new ArrayList<XBAttributeType>();
        XBBlockSpecificationBind elem;
        for (Iterator it = spec.getAttribs().iterator(); it.hasNext();) {
            elem = (XBBlockSpecificationBind) it.next();
            if (elem.getXbIndex()!=null) {
                if (list.size() <= elem.getXbIndex().intValue()) {
                    int newSize = elem.getXbIndex().intValue()+1; // TODO GetMaxXBIndex
                    if (list.size()>newSize) {
                        int count = list.size()-newSize;
                        for(int i=0;i<count;i++) list.remove(list.size()-1);
                    } else if (list.size()<newSize) {
                        int count = newSize-list.size();
                        for(int i=0;i<count;i++) list.add(null);
                    }
                }
                XBCBlockType typeBlock = new XBCBlockType(xbCatalog);
                list.set(elem.getXbIndex().intValue(),elem.getHasType());
            }
        }
        return list;
    }
 */

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof XBCPBlockDecl) {
            return Arrays.equals(((XBCPBlockDecl) obj).catalogPath,catalogPath) && (((XBCPBlockDecl) obj).revision == revision);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Arrays.hashCode(this.catalogPath);
        hash = 47 * hash + this.revision;
        return hash;
    }

    public int compareTo(Object o) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public long getAttribCount() {
        return 0;
    }

    @Override
    public XBCBlockSpec getBlockSpec(XBCatalog catalog) {
        return (XBCBlockSpec) catalog.findBlockTypeByPath(getCatalogObjectPath());
    }

    @Override
    public int getRevision() {
        return revision;
    }

    /**
     * @return the catalogPath
     */
    public long[] getCatalogPath() {
        return catalogPath;
    }

    /**
     * @param catalogPath the catalogPath to set
     */
    public void setCatalogPath(long[] catalogPath) {
        this.catalogPath = catalogPath;
    }

    /**
     * Get catalog path as array of Long instances.
     *
     * @return the catalogPath
     */
    public Long[] getCatalogObjectPath() {
        Long[] objectPath = new Long[catalogPath.length];
        for (int i = 0; i < objectPath.length; i++) {
            objectPath[i] = new Long(catalogPath[i]);
        }
        return objectPath;
    }

    /**
     * @param revision the revision to set
     */
    public void setRevision(int revision) {
        this.revision = revision;
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

/*
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
    }
*/
}
