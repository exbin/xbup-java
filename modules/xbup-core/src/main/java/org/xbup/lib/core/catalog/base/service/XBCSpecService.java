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
package org.xbup.lib.core.catalog.base.service;

import java.util.List;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCFormatSpec;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.XBCSpecDef;
import org.xbup.lib.core.catalog.base.XBCSpecDefType;

/**
 * Interface for XBCSpec items service.
 *
 * @version 0.1.22 2013/01/11
 * @author XBUP Project (http://xbup.org)
 */
public interface XBCSpecService<T extends XBCSpec> extends XBCService<T> {

    public Long getAllSpecsCount();

    public Long getAllFormatSpecsCount();

    public Long getAllGroupSpecsCount();

    public Long getAllBlockSpecsCount();

    /** Returns Path of XBIndexes for given node */
    public Long[] getSpecXBPath(XBCSpec spec);

    /** Get list of specifications */
    public List<XBCSpec> getSpecs(XBCNode node);

    /** Get list of specifications */
    public XBCSpec getSpecByOrder(XBCNode node, long index);

    /** Returns format specification of given index */
    public XBCFormatSpec getFormatSpec(XBCNode node, long index);

    /** Get list of format specifications */
    public List<XBCFormatSpec> getFormatSpecs(XBCNode node);

    /** Returns block specification of given index */
    public XBCBlockSpec getBlockSpec(XBCNode node, long index);

    /** Get list of block specifications */
    public List<XBCBlockSpec> getBlockSpecs(XBCNode node);

    /** Returns group specification of given index */
    public XBCGroupSpec getGroupSpec(XBCNode node, long index);

    /** Get list of group specifications */
    public List<XBCGroupSpec> getGroupSpecs(XBCNode node);

    public XBCBlockSpec findBlockSpecByXB(XBCNode node, long xbIndex);
    public Long findMaxBlockSpecXB(XBCNode node);

    public XBCGroupSpec findGroupSpecByXB(XBCNode node, long xbIndex);
    public Long findMaxGroupSpecXB(XBCNode node);

    public XBCFormatSpec findFormatSpecByXB(XBCNode node, long xbIndex);
    public Long findMaxFormatSpecXB(XBCNode node);

    long getFormatSpecsCount(XBCNode node);
    long getGroupSpecsCount(XBCNode node);
    long getBlockSpecsCount(XBCNode node);
    long getSpecsCount(XBCNode node);

    /**
     * Get count of specification definitions.
     * @return count of definition rows.
     */
    public long getDefsCount();

    /** Get Bind of given index */
    public XBCSpecDef getSpecDefByOrder(XBCSpec spec, long index);

    /** Get Bind of given index
     * @param xbIndex XBIndex of given bind
     */
    public XBCSpecDef findSpecDefByXB(XBCSpec spec, long xbIndex);

    /** Get maximum index of binds */
    public Long findMaxSpecDefXB(XBCSpec spec);

    /** Get list of all binds */
    public List<XBCSpecDef> getSpecDefs(XBCSpec spec);

    /** Returns count of binds */
    public long getSpecDefsCount(XBCSpec spec);

    public XBCSpecDef getSpecDef(long itemId);

    public XBCSpecDef createSpecDef(XBCSpec spec, XBCSpecDefType type);

    public XBCBlockSpec createBlockSpec();

    public XBCGroupSpec createGroupSpec();

    public XBCFormatSpec createFormatSpec();
}
