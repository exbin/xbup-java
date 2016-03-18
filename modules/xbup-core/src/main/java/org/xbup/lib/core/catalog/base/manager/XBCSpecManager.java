/*
 * Copyright (C) ExBin Project
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
package org.xbup.lib.core.catalog.base.manager;

import java.util.List;
import org.xbup.lib.core.block.definition.XBParamType;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCFormatSpec;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.XBCSpecDef;

/**
 * Interface for XBCSpec catalog manager.
 *
 * @version 0.1.22 2013/01/11
 * @author ExBin Project (http://exbin.org)
 * @param <T> specification entity
 */
public interface XBCSpecManager<T extends XBCSpec> extends XBCCatalogManager<T> {

    /**
     * Gets count of all specifications.
     *
     * @return count of specifications
     */
    public Long getAllSpecsCount();

    /**
     * Gets count of all format specifications.
     *
     * @return count of specifications
     */
    public Long getAllFormatSpecsCount();

    /**
     * Gets count of all group specifications.
     *
     * @return count of specifications
     */
    public Long getAllGroupSpecsCount();

    /**
     * Gets count of all block specifications.
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
     * Gets list of specifications.
     *
     * @param node parent node
     * @return list of specifications
     */
    public List<XBCSpec> getSpecs(XBCNode node);

    /**
     * Gets specification of given index.
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
     * Gets list of format specifications.
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
     * Gets list of block specifications.
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
     * Gets list of group specifications.
     *
     * @param node parent node
     * @return list of group specifications
     */
    public List<XBCGroupSpec> getGroupSpecs(XBCNode node);

    /**
     * Finds block specification by XB Index.
     *
     * @param node parent node
     * @param xbIndex XB index
     * @return block specification
     */
    public XBCBlockSpec findBlockSpecByXB(XBCNode node, long xbIndex);

    /**
     * Gets maximum block specification XB index for given node.
     *
     * @param node parent node
     * @return maximum XB index
     */
    public Long findMaxBlockSpecXB(XBCNode node);

    /**
     * Finds group specification by XB Index.
     *
     * @param node parent node
     * @param xbIndex XB index
     * @return group specification
     */
    public XBCGroupSpec findGroupSpecByXB(XBCNode node, long xbIndex);

    /**
     * Gets maximum group specification XB index for given node.
     *
     * @param node parent node
     * @return maximum XB index
     */
    public Long findMaxGroupSpecXB(XBCNode node);

    /**
     * Finds format specification by XB Index.
     *
     * @param node parent node
     * @param xbIndex XB index
     * @return format specification
     */
    public XBCFormatSpec findFormatSpecByXB(XBCNode node, long xbIndex);

    /**
     * Gets maximum format specification XB index for given node.
     *
     * @param node parent node
     * @return maximum XB index
     */
    public Long findMaxFormatSpecXB(XBCNode node);

    /**
     * Gets count of format specifications for given node.
     *
     * @param node parent node
     * @return count of specifications
     */
    public long getFormatSpecsCount(XBCNode node);

    /**
     * Gets count of group specifications for given node.
     *
     * @param node parent node
     * @return count of specifications
     */
    public long getGroupSpecsCount(XBCNode node);

    /**
     * Gets count of block specifications for given node.
     *
     * @param node parent node
     * @return count of specifications
     */
    public long getBlockSpecsCount(XBCNode node);

    /**
     * Gets count of specifications for given node.
     *
     * @param node parent node
     * @return count of specifications
     */
    public long getSpecsCount(XBCNode node);

    /**
     * Gets count of specification definitions.
     *
     * @return count of definition rows.
     */
    public long getDefsCount();

    /**
     * Gets specification definition of given index.
     *
     * @param spec parent specification
     * @param index order index
     * @return specification definition
     */
    public XBCSpecDef getSpecDefByOrder(XBCSpec spec, long index);

    /**
     * Gets specification definition by XB index.
     *
     * @param spec parent specification
     * @param xbIndex XBIndex of given bind
     * @return specification definition
     */
    public XBCSpecDef findSpecDefByXB(XBCSpec spec, long xbIndex);

//    /**
//     * Gets maximum index of binds.
//     */
//    public Long findMaxBindXB(XBCSpec spec);
//
    /**
     * Gets list of all specification definitions.
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
     * Gets specification definition by unique index.
     *
     * @param itemId unique index
     * @return specification definition
     */
    public XBCSpecDef getSpecDef(long itemId);

    /**
     * Creates new instance of specification definition.
     *
     * @param spec parent specification
     * @param type type of definition
     * @return specification definition
     */
    public XBCSpecDef createSpecDef(XBCSpec spec, XBParamType type);

    /**
     * Creates new instance of block specification.
     *
     * @return block specification
     */
    public XBCBlockSpec createBlockSpec();

    /**
     * Creates new instance of group specification.
     *
     * @return group specification
     */
    public XBCGroupSpec createGroupSpec();

    /**
     * Creates new instance of format specification.
     *
     * @return format specification
     */
    public XBCFormatSpec createFormatSpec();
}
