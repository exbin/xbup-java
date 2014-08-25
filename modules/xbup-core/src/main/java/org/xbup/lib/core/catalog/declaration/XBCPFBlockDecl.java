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

import org.xbup.lib.core.block.XBTBlock;
import org.xbup.lib.core.block.XBTEditableBlock;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.parser.basic.XBTConsumer;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * Context group type specified by format path and group and block indexes.
 *
 * @version 0.1 wr21.0 2011/12/03
 * @author XBUP Project (http://xbup.org)
 */
public class XBCPFBlockDecl implements XBBlockDecl {

    private XBCatalog catalog;
    private long[] formatPath;
    private long groupIndex;
    private long blockIndex;

    public XBCPFBlockDecl() {
        catalog = null;
    }

    /** Creates a new instance of XBCTypeFormat */
    public XBCPFBlockDecl(long[] formatPath, long groupIndex, long blockIndex) {
        this.formatPath = formatPath;

//        this.setSpec(spec);
//        nameManager = (XBCXNameManager) catalog.getExtension(XBCXNameManager.class);
    }

    public void processSpec() {
//        if (spec.getAttrCount()!=null) setAttribCount(spec.getAttrCount().intValue());
    }
/*
    public void processSpec(XBContextBlock spec) {
//        setSpec(spec);
//        processSpec();
    }

    public List<XBAttributeType> getAttrList() {
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

    public boolean equals(XBBlockDecl type) {
        if (type instanceof XBCPBlockDecl) {
//            return (((XBCPContextBlock) type).getCatalog() == getCatalog())&&(((XBCPContextBlock) type).getSpec() == getSpec()); // TODO: equals
            return false;
        } else {
            return false;
        }
    }

    public int compareTo(Object o) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public XBCatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(XBCatalog catalog) {
        this.catalog = catalog;
//        nameManager = (XBCXNameManager) catalog.getExtension(XBCXNameManager.class);
    }

    public UBNatural getBlockID() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public UBNatural getGroupID() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public long getAttribCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isClosed() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void attachXBTConsumer(XBTConsumer consumer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getRevision() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getBlocksLimit() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public XBTBlock getParameter(XBTBlock block, int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setParameter(XBTEditableBlock block, int index, XBTBlock parameter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getParametersCount(XBTBlock block) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
