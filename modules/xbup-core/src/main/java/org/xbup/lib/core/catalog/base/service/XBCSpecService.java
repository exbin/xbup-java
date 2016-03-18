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
package org.xbup.lib.core.catalog.base.service;

import java.util.List;
import org.xbup.lib.core.block.declaration.catalog.XBCBlockDecl;
import org.xbup.lib.core.block.declaration.catalog.XBCFormatDecl;
import org.xbup.lib.core.block.declaration.catalog.XBCGroupDecl;
import org.xbup.lib.core.block.declaration.local.XBLBlockDecl;
import org.xbup.lib.core.block.declaration.local.XBLFormatDecl;
import org.xbup.lib.core.block.declaration.local.XBLGroupDecl;
import org.xbup.lib.core.block.definition.XBParamType;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCFormatSpec;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.XBCSpecDef;

/**
 * Interface for XBCSpec items service.
 *
 * @version 0.1.25 2015/02/20
 * @author ExBin Project (http://exbin.org)
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
     * Gets list of specifications.
     *
     * @param node parent node
     * @return list of specifications
     */
    public List<XBCSpec> getSpecs(XBCNode node);

    /**
     * Gets list of specifications.
     *
     * @param node parent node
     * @param index order index
     * @return specification
     */
    public XBCSpec getSpecByOrder(XBCNode node, long index);

    /**
     * Returns format specification of given order index.
     *
     * @param node parent node
     * @param index order index
     * @return specification
     */
    public XBCFormatSpec getFormatSpec(XBCNode node, long index);

    /**
     * Gets list of format specifications.
     *
     * @param node parent node
     * @return list of specifications
     */
    public List<XBCFormatSpec> getFormatSpecs(XBCNode node);

    /**
     * Returns block specification of given order index.
     *
     * @param node parent node
     * @param index order index
     * @return specification
     */
    public XBCBlockSpec getBlockSpec(XBCNode node, long index);

    /**
     * Gets list of block specifications.
     *
     * @param node parent node
     * @return list of specifications
     */
    public List<XBCBlockSpec> getBlockSpecs(XBCNode node);

    /**
     * Returns group specification of given order index.
     *
     * @param node parent node
     * @param index order index
     * @return specification
     */
    public XBCGroupSpec getGroupSpec(XBCNode node, long index);

    /**
     * Gets list of group specifications.
     *
     * @param node parent node
     * @return list of specifications
     */
    public List<XBCGroupSpec> getGroupSpecs(XBCNode node);

    /**
     * Finds block specification by XB index.
     *
     * @param node parent node
     * @param xbIndex XB index
     * @return specification
     */
    public XBCBlockSpec findBlockSpecByXB(XBCNode node, long xbIndex);

    /**
     * Finds maximum XB index of block specifications for given node.
     *
     * @param node parent node
     * @return maximum XB index
     */
    public Long findMaxBlockSpecXB(XBCNode node);

    /**
     * Finds group specification by XB index.
     *
     * @param node parent node
     * @param xbIndex XB index
     * @return specification
     */
    public XBCGroupSpec findGroupSpecByXB(XBCNode node, long xbIndex);

    /**
     * Finds maximum XB index of group specifications for given node.
     *
     * @param node parent node
     * @return maximum XB index
     */
    public Long findMaxGroupSpecXB(XBCNode node);

    /**
     * Finds format specification by XB index.
     *
     * @param node parent node
     * @param xbIndex XB index
     * @return specification
     */
    public XBCFormatSpec findFormatSpecByXB(XBCNode node, long xbIndex);

    /**
     * Finds maximum XB index of format specifications for given node.
     *
     * @param node parent node
     * @return maximum XB index
     */
    public Long findMaxFormatSpecXB(XBCNode node);

    /**
     * Gets count of specification definitions.
     *
     * @return count of definition rows.
     */
    public long getDefsCount();

    /**
     * Gets specification's definition of given order index.
     *
     * @param spec specification
     * @param index order index
     * @return specification's definition
     */
    public XBCSpecDef getSpecDefByOrder(XBCSpec spec, long index);

    /**
     * Gets specification's definition of given XB index.
     *
     * @param spec specification
     * @param xbIndex XBIndex of given bind
     * @return specification's definition
     */
    public XBCSpecDef findSpecDefByXB(XBCSpec spec, long xbIndex);

    /**
     * Gets maximum XB index of specification's definitions.
     *
     * @param spec specification
     * @return maximum XB index
     */
    public Long findMaxSpecDefXB(XBCSpec spec);

    /**
     * Gets list of all specification's definitions.
     *
     * @param spec specification
     * @return list of specification's definitions
     */
    public List<XBCSpecDef> getSpecDefs(XBCSpec spec);

    /**
     * Returns count of specification's definitions.
     *
     * @param spec specification
     * @return count of specification's definitions
     */
    public long getSpecDefsCount(XBCSpec spec);

    /**
     * Gets specification's definition.
     *
     * @param itemId
     * @return specification's definition
     */
    public XBCSpecDef getSpecDef(long itemId);

    /**
     * Creates new instance of block specification definition.
     *
     * @param spec specification
     * @param type specification definition's type
     * @return specification definition
     */
    public XBCSpecDef createSpecDef(XBCSpec spec, XBParamType type);

    /**
     * Creates new instance of block specification.
     *
     * @return specification
     */
    public XBCBlockSpec createBlockSpec();

    /**
     * Creates new instance of group specification.
     *
     * @return specification
     */
    public XBCGroupSpec createGroupSpec();

    /**
     * Creates new instance of format specification.
     *
     * @return specification
     */
    public XBCFormatSpec createFormatSpec();

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
     * Gets count of all specifications in catalog.
     *
     * @return count of specifications
     */
    public Long getAllSpecsCount();

    /**
     * Gets count of all format specifications in catalog.
     *
     * @return count of specifications
     */
    public Long getAllFormatSpecsCount();

    /**
     * Gets count of all group specifications in catalog.
     *
     * @return count of specifications
     */
    public Long getAllGroupSpecsCount();

    /**
     * Gets count of all block specifications in catalog.
     *
     * @return count of specifications
     */
    public Long getAllBlockSpecsCount();

    /**
     * Removes specification definition with all dependencies.
     *
     * @param specDef definition to remove
     */
    public void removeItemDepth(XBCSpecDef specDef);

    /**
     * Converts catalog format declaration to local declaration with definition.
     *
     * @param formatDecl format specification
     * @return local format declaration
     */
    public XBLFormatDecl getFormatDeclAsLocal(XBCFormatDecl formatDecl);

    /**
     * Converts catalog group declaration to local declaration with definition.
     *
     * @param groupDecl group specification
     * @return local group declaration
     */
    public XBLGroupDecl getGroupDeclAsLocal(XBCGroupDecl groupDecl);

    /**
     * Converts catalog block declaration to local declaration with definition.
     *
     * @param blockDecl block specification
     * @return local block declaration
     */
    public XBLBlockDecl getBlockDeclAsLocal(XBCBlockDecl blockDecl);
}
