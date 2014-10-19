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
 * @version 0.1.24 2014/10/19
 * @author XBUP Project (http://xbup.org)
 * @param <T> specification entity
 */
public interface XBCSpecService<T extends XBCSpec> extends XBCService<T> {

    /**
     * Returns path of XBIndexes for given node.
     *
     * @param spec specifications
     * @return catalog path
     */
    public Long[] getSpecXBPath(XBCSpec spec);

    /**
     * Get list of specifications.
     *
     * @param node parent node
     * @return list of specifications
     */
    public List<XBCSpec> getSpecs(XBCNode node);

    /**
     * Get list of specifications.
     *
     * @param node parent node
     * @param index order index
     * @return specification
     */
    public XBCSpec getSpecByOrder(XBCNode node, long index);

    /**
     * Return format specification of given order index.
     *
     * @param node parent node
     * @param index order index
     * @return specification
     */
    public XBCFormatSpec getFormatSpec(XBCNode node, long index);

    /**
     * Get list of format specifications.
     *
     * @param node parent node
     * @return list of specifications
     */
    public List<XBCFormatSpec> getFormatSpecs(XBCNode node);

    /**
     * Return block specification of given order index.
     *
     * @param node parent node
     * @param index order index
     * @return specification
     */
    public XBCBlockSpec getBlockSpec(XBCNode node, long index);

    /**
     * Get list of block specifications.
     *
     * @param node parent node
     * @return list of specifications
     */
    public List<XBCBlockSpec> getBlockSpecs(XBCNode node);

    /**
     * Return group specification of given order index.
     *
     * @param node parent node
     * @param index order index
     * @return specification
     */
    public XBCGroupSpec getGroupSpec(XBCNode node, long index);

    /**
     * Get list of group specifications.
     *
     * @param node parent node
     * @return list of specifications
     */
    public List<XBCGroupSpec> getGroupSpecs(XBCNode node);

    /**
     * Find block specification by XB index.
     *
     * @param node parent node
     * @param xbIndex XB index
     * @return specification
     */
    public XBCBlockSpec findBlockSpecByXB(XBCNode node, long xbIndex);

    /**
     * Find maximum XB index of block specifications for given node.
     *
     * @param node parent node
     * @return maximum XB index
     */
    public Long findMaxBlockSpecXB(XBCNode node);

    /**
     * Find group specification by XB index.
     *
     * @param node parent node
     * @param xbIndex XB index
     * @return specification
     */
    public XBCGroupSpec findGroupSpecByXB(XBCNode node, long xbIndex);

    /**
     * Find maximum XB index of group specifications for given node.
     *
     * @param node parent node
     * @return maximum XB index
     */
    public Long findMaxGroupSpecXB(XBCNode node);

    /**
     * Find format specification by XB index.
     *
     * @param node parent node
     * @param xbIndex XB index
     * @return specification
     */
    public XBCFormatSpec findFormatSpecByXB(XBCNode node, long xbIndex);

    /**
     * Find maximum XB index of format specifications for given node.
     *
     * @param node parent node
     * @return maximum XB index
     */
    public Long findMaxFormatSpecXB(XBCNode node);

    /**
     * Get count of specification definitions.
     *
     * @return count of definition rows.
     */
    public long getDefsCount();

    /**
     * Get specification's definition of given order index.
     *
     * @param spec specification
     * @param index order index
     * @return specification's definition
     */
    public XBCSpecDef getSpecDefByOrder(XBCSpec spec, long index);

    /**
     * Get specification's definition of given XB index.
     *
     * @param spec specification
     * @param xbIndex XBIndex of given bind
     * @return specification's definition
     */
    public XBCSpecDef findSpecDefByXB(XBCSpec spec, long xbIndex);

    /**
     * Get maximum XB index of specification's definitions.
     *
     * @param spec specification
     * @return maximum XB index
     */
    public Long findMaxSpecDefXB(XBCSpec spec);

    /**
     * Get list of all specification's definitions.
     *
     * @param spec specification
     * @return list of specification's definitions
     */
    public List<XBCSpecDef> getSpecDefs(XBCSpec spec);

    /**
     * Return count of specification's definitions.
     *
     * @param spec specification
     * @return count of specification's definitions
     */
    public long getSpecDefsCount(XBCSpec spec);

    /**
     * Get specification's definition.
     *
     * @param itemId
     * @return specification's definition
     */
    public XBCSpecDef getSpecDef(long itemId);

    /**
     * Create new instance of block specification definition.
     *
     * @param spec specification
     * @param type specification definition's type
     * @return specification definition
     */
    public XBCSpecDef createSpecDef(XBCSpec spec, XBCSpecDefType type);

    /**
     * Create new instance of block specification.
     *
     * @return specification
     */
    public XBCBlockSpec createBlockSpec();

    /**
     * Create new instance of group specification.
     *
     * @return specification
     */
    public XBCGroupSpec createGroupSpec();

    /**
     * Create new instance of format specification.
     *
     * @return specification
     */
    public XBCFormatSpec createFormatSpec();

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
     * Get count of all specifications in catalog.
     *
     * @return count of specifications
     */
    public Long getAllSpecsCount();

    /**
     * Get count of all format specifications in catalog.
     *
     * @return count of specifications
     */
    public Long getAllFormatSpecsCount();

    /**
     * Get count of all group specifications in catalog.
     *
     * @return count of specifications
     */
    public Long getAllGroupSpecsCount();

    /**
     * Get count of all block specifications in catalog.
     *
     * @return count of specifications
     */
    public Long getAllBlockSpecsCount();
}
