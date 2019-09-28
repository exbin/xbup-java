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
package org.exbin.xbup.core.catalog.base.manager;

import java.util.List;
import org.exbin.xbup.core.block.definition.XBParamType;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.XBCFormatSpec;
import org.exbin.xbup.core.catalog.base.XBCGroupSpec;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.XBCSpecDef;

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
    Long getAllSpecsCount();

    /**
     * Gets count of all format specifications.
     *
     * @return count of specifications
     */
    Long getAllFormatSpecsCount();

    /**
     * Gets count of all group specifications.
     *
     * @return count of specifications
     */
    Long getAllGroupSpecsCount();

    /**
     * Gets count of all block specifications.
     *
     * @return count of specifications
     */
    Long getAllBlockSpecsCount();

    /**
     * Returns path of XBIndexes for given node.
     *
     * @param node node
     * @return catalog path
     */
    Long[] getSpecXBPath(XBCSpec node);

    /**
     * Gets list of specifications.
     *
     * @param node parent node
     * @return list of specifications
     */
    List<XBCSpec> getSpecs(XBCNode node);

    /**
     * Gets specification of given index.
     *
     * @param node parent node
     * @param index order index
     * @return specification
     */
    XBCSpec getSpec(XBCNode node, long index);

    /**
     * Returns format specification of given index.
     *
     * @param node parent node
     * @param index order index
     * @return format specification
     */
    XBCFormatSpec getFormatSpec(XBCNode node, long index);

    /**
     * Gets list of format specifications.
     *
     * @param node parent node
     * @return list of format specifications
     */
    List<XBCFormatSpec> getFormatSpecs(XBCNode node);

    /**
     * Returns block specification of given index.
     *
     * @param node parent node
     * @param index order index
     * @return block specification
     */
    XBCBlockSpec getBlockSpec(XBCNode node, long index);

    /**
     * Gets list of block specifications.
     *
     * @param node parent node
     * @return list of block specifications
     */
    List<XBCBlockSpec> getBlockSpecs(XBCNode node);

    /**
     * Returns group specification of given index.
     *
     * @param node parent node
     * @param index order index
     * @return group specification
     */
    XBCGroupSpec getGroupSpec(XBCNode node, long index);

    /**
     * Gets list of group specifications.
     *
     * @param node parent node
     * @return list of group specifications
     */
    List<XBCGroupSpec> getGroupSpecs(XBCNode node);

    /**
     * Finds block specification by XB Index.
     *
     * @param node parent node
     * @param xbIndex XB index
     * @return block specification
     */
    XBCBlockSpec findBlockSpecByXB(XBCNode node, long xbIndex);

    /**
     * Gets maximum block specification XB index for given node.
     *
     * @param node parent node
     * @return maximum XB index
     */
    Long findMaxBlockSpecXB(XBCNode node);

    /**
     * Finds group specification by XB Index.
     *
     * @param node parent node
     * @param xbIndex XB index
     * @return group specification
     */
    XBCGroupSpec findGroupSpecByXB(XBCNode node, long xbIndex);

    /**
     * Gets maximum group specification XB index for given node.
     *
     * @param node parent node
     * @return maximum XB index
     */
    Long findMaxGroupSpecXB(XBCNode node);

    /**
     * Finds format specification by XB Index.
     *
     * @param node parent node
     * @param xbIndex XB index
     * @return format specification
     */
    XBCFormatSpec findFormatSpecByXB(XBCNode node, long xbIndex);

    /**
     * Gets maximum format specification XB index for given node.
     *
     * @param node parent node
     * @return maximum XB index
     */
    Long findMaxFormatSpecXB(XBCNode node);

    /**
     * Gets count of format specifications for given node.
     *
     * @param node parent node
     * @return count of specifications
     */
    long getFormatSpecsCount(XBCNode node);

    /**
     * Gets count of group specifications for given node.
     *
     * @param node parent node
     * @return count of specifications
     */
    long getGroupSpecsCount(XBCNode node);

    /**
     * Gets count of block specifications for given node.
     *
     * @param node parent node
     * @return count of specifications
     */
    long getBlockSpecsCount(XBCNode node);

    /**
     * Gets count of specifications for given node.
     *
     * @param node parent node
     * @return count of specifications
     */
    long getSpecsCount(XBCNode node);

    /**
     * Gets count of specification definitions.
     *
     * @return count of definition rows.
     */
    long getDefsCount();

    /**
     * Gets specification definition of given index.
     *
     * @param spec parent specification
     * @param index order index
     * @return specification definition
     */
    XBCSpecDef getSpecDefByOrder(XBCSpec spec, long index);

    /**
     * Gets specification definition by XB index.
     *
     * @param spec parent specification
     * @param xbIndex XBIndex of given bind
     * @return specification definition
     */
    XBCSpecDef findSpecDefByXB(XBCSpec spec, long xbIndex);

//    /**
//     * Gets maximum index of binds.
//     */
//    Long findMaxBindXB(XBCSpec spec);
//
    /**
     * Gets list of all specification definitions.
     *
     * @param spec parent specification
     * @return list of specification definitions
     */
    List<XBCSpecDef> getSpecDefs(XBCSpec spec);

    /**
     * Returns count of specification definitions.
     *
     * @param spec parent specification
     * @return count of specification definitions
     */
    long getSpecDefsCount(XBCSpec spec);

    /**
     * Gets specification definition by unique index.
     *
     * @param itemId unique index
     * @return specification definition
     */
    XBCSpecDef getSpecDef(long itemId);

    /**
     * Creates new instance of specification definition.
     *
     * @param spec parent specification
     * @param type type of definition
     * @return specification definition
     */
    XBCSpecDef createSpecDef(XBCSpec spec, XBParamType type);

    /**
     * Creates new instance of block specification.
     *
     * @return block specification
     */
    XBCBlockSpec createBlockSpec();

    /**
     * Creates new instance of group specification.
     *
     * @return group specification
     */
    XBCGroupSpec createGroupSpec();

    /**
     * Creates new instance of format specification.
     *
     * @return format specification
     */
    XBCFormatSpec createFormatSpec();
}
