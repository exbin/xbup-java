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
package org.xbup.lib.xb.catalog.base.manager;

import java.util.List;
import org.xbup.lib.xb.catalog.base.XBCBlockSpec;
import org.xbup.lib.xb.catalog.base.XBCFormatSpec;
import org.xbup.lib.xb.catalog.base.XBCGroupSpec;
import org.xbup.lib.xb.catalog.base.XBCNode;
import org.xbup.lib.xb.catalog.base.XBCSpec;
import org.xbup.lib.xb.catalog.base.XBCSpecDef;
import org.xbup.lib.xb.catalog.base.XBCSpecDefType;

/**
 * Interface for XBCSpec catalog manager.
 *
 * @version 0.1 wr22.0 2013/01/11
 * @author XBUP Project (http://xbup.org)
 * @param <T> specification entity
 */
public interface XBCSpecManager<T extends XBCSpec> extends XBCCatalogManager<T> {

    /**
     * Get count of all specifications.
     *
     * @return count of specifications
     */
    public Long getAllSpecsCount();

    /**
     * Get count of all format specifications.
     *
     * @return count of specifications
     */
    public Long getAllFormatSpecsCount();

    /**
     * Get count of all group specifications.
     *
     * @return count of specifications
     */
    public Long getAllGroupSpecsCount();

    /**
     * Get count of all block specifications.
     *
     * @return count of specifications
     */
    public Long getAllBlockSpecsCount();

    /**
     * Returns path of XBIndexes for given node.
     *
     * @param node
     * @return catalog path
     */
    public Long[] getSpecXBPath(XBCSpec node);

    /**
     * Get list of specifications.
     *
     * @param node parent node
     * @return list of specifications
     */
    public List<XBCSpec> getSpecs(XBCNode node);

    /**
     * Get specification of given index.
     *
     * @param node parent node
     * @param index order index
     * @return specification
     */
    public XBCSpec getSpec(XBCNode node, long index);

    /**
     * Returns format specification of given index.
     *
     * @param node parent node
     * @param index order index
     * @return format specification
     */
    public XBCFormatSpec getFormatSpec(XBCNode node, long index);

    /**
     * Get list of format specifications.
     *
     * @param node parent node
     * @return list of format specifications
     */
    public List<XBCFormatSpec> getFormatSpecs(XBCNode node);

    /**
     * Returns block specification of given index.
     *
     * @param node parent node
     * @param index order index
     * @return block specification
     */
    public XBCBlockSpec getBlockSpec(XBCNode node, long index);

    /**
     * Get list of block specifications.
     *
     * @param node parent node
     * @return list of block specifications
     */
    public List<XBCBlockSpec> getBlockSpecs(XBCNode node);

    /**
     * Returns group specification of given index.
     *
     * @param node parent node
     * @param index order index
     * @return group specification
     */
    public XBCGroupSpec getGroupSpec(XBCNode node, long index);

    /**
     * Get list of group specifications.
     *
     * @param node parent node
     * @return list of group specifications
     */
    public List<XBCGroupSpec> getGroupSpecs(XBCNode node);

    /**
     * Find block specification by XB Index.
     *
     * @param node parent node
     * @param xbIndex XB index
     * @return block specification
     */
    public XBCBlockSpec findBlockSpecByXB(XBCNode node, long xbIndex);

    /**
     * Get maximum block specification XB index for given node.
     *
     * @param node parent node
     * @return maximum XB index
     */
    public Long findMaxBlockSpecXB(XBCNode node);

    /**
     * Find group specification by XB Index.
     *
     * @param node parent node
     * @param xbIndex XB index
     * @return group specification
     */
    public XBCGroupSpec findGroupSpecByXB(XBCNode node, long xbIndex);

    /**
     * Get maximum group specification XB index for given node.
     *
     * @param node parent node
     * @return maximum XB index
     */
    public Long findMaxGroupSpecXB(XBCNode node);

    /**
     * Find format specification by XB Index.
     *
     * @param node parent node
     * @param xbIndex XB index
     * @return format specification
     */
    public XBCFormatSpec findFormatSpecByXB(XBCNode node, long xbIndex);

    /**
     * Get maximum format specification XB index for given node.
     *
     * @param node parent node
     * @return maximum XB index
     */
    public Long findMaxFormatSpecXB(XBCNode node);

    /**
     * Get count of format specifications for given node.
     *
     * @param node parent node
     * @return count of specifications
     */
    public long getFormatSpecsCount(XBCNode node);

    /**
     * Get count of group specifications for given node.
     *
     * @param node parent node
     * @return count of specifications
     */
    public long getGroupSpecsCount(XBCNode node);

    /**
     * Get count of block specifications for given node.
     *
     * @param node parent node
     * @return count of specifications
     */
    public long getBlockSpecsCount(XBCNode node);

    /**
     * Get count of specifications for given node.
     *
     * @param node parent node
     * @return count of specifications
     */
    public long getSpecsCount(XBCNode node);

    /**
     * Get count of specification definitions.
     *
     * @return count of definition rows.
     */
    public long getDefsCount();

    /**
     * Get specification definition of given index.
     *
     * @param spec parent specification
     * @param index order index
     * @return specification definition
     */
    public XBCSpecDef getSpecDefByOrder(XBCSpec spec, long index);

    /**
     * Get specification definition by XB index.
     *
     * @param spec parent specification
     * @param xbIndex XBIndex of given bind
     * @return specification definition
     */
    public XBCSpecDef findSpecDefByXB(XBCSpec spec, long xbIndex);

    /**
     * Get maximum index of binds
     */
//    public Long findMaxBindXB(XBCSpec spec);
    /**
     * Get list of all specification definitions.
     *
     * @param spec parent specification
     * @return list of specification definitions
     */
    public List<XBCSpecDef> getSpecDefs(XBCSpec spec);

    /**
     * Returns count of specification definitions.
     *
     * @param spec parent specification
     * @return count of specification definitions
     */
    public long getSpecDefsCount(XBCSpec spec);

    /**
     * Get specification definition by unique index.
     *
     * @param itemId unique index
     * @return specification definition
     */
    public XBCSpecDef getSpecDef(long itemId);

    /**
     * Create new instance of specification definition.
     *
     * @param spec parent specification
     * @param type type of definition
     * @return specification definition
     */
    public XBCSpecDef createSpecDef(XBCSpec spec, XBCSpecDefType type);

    /**
     * Create new instance of block specification.
     *
     * @return block specification
     */
    public XBCBlockSpec createBlockSpec();

    /**
     * Create new instance of group specification.
     *
     * @return group specification
     */
    public XBCGroupSpec createGroupSpec();

    /**
     * Create new instance of format specification.
     *
     * @return format specification
     */
    public XBCFormatSpec createFormatSpec();
}
