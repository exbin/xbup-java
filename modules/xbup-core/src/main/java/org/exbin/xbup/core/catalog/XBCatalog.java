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
package org.exbin.xbup.core.catalog;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.exbin.xbup.core.block.XBBasicBlockType;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.declaration.XBBlockDecl;
import org.exbin.xbup.core.block.declaration.XBContext;
import org.exbin.xbup.core.block.declaration.XBFormatDecl;
import org.exbin.xbup.core.block.declaration.XBGroupDecl;
import org.exbin.xbup.core.catalog.base.XBCBase;
import org.exbin.xbup.core.catalog.base.XBCFormatSpec;
import org.exbin.xbup.core.catalog.base.XBCGroupSpec;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.manager.XBCManager;
import org.exbin.xbup.core.catalog.base.service.XBCService;
import org.exbin.xbup.core.parser.token.pull.XBTPullProvider;

/**
 * Interface for XBUP level 1 catalog.
 *
 * @version 0.2.1 2017/05/27
 * @author ExBin Project (http://exbin.org)
 */
public interface XBCatalog {

    /**
     * Returns a processing context for empty document.
     *
     * @return block types processing context
     */
    @Nonnull
    XBContext getRootContext();

    /**
     * Returns a type of block for given XB Path.
     *
     * @param xbCatalogPath a path in catalog using xbIndexes
     * @param revision XB index of revision
     * @return block type declaration for given catalog or null if traversal
     * fails
     */
    @Nullable
    XBBlockDecl findBlockTypeByPath(@Nonnull Long[] xbCatalogPath, int revision);

    /**
     * Returns a group type for given XB Path.
     *
     * @param xbCatalogPath a path in catalog using xbIndexes
     * @param revision XB index of revision
     * @return group Type for given catalog or null if fails
     */
    @Nullable
    XBGroupDecl findGroupTypeByPath(@Nonnull Long[] xbCatalogPath, int revision);

    /**
     * Returns a format type for given XB Path.
     *
     * @param xbCatalogPath a path in catalog using xbIndexes
     * @param revision XB index of revision
     * @return format Type for given catalog or null if fails
     */
    @Nullable
    XBFormatDecl findFormatTypeByPath(@Nonnull Long[] xbCatalogPath, int revision);

    /**
     * Returns list of binded specifications for given format specification for
     * processing context.
     *
     * @param spec a format specification
     * @return list of group specifications
     */
    List<XBGroupDecl> getGroups(@Nonnull XBCFormatSpec spec);

    /**
     * Returns list of binded specifications for given group specification for
     * processing context.
     *
     * @param spec a group specification
     * @return list of block specifications
     */
    List<XBBlockDecl> getBlocks(@Nonnull XBCGroupSpec spec);

    /**
     * Returns catalog service of given type.
     *
     * @param <T> base param
     * @param serviceClass a class for desired instance of catalog service
     * @return service
     */
    @Nonnull
    <T extends XBCService<? extends XBCBase>> T getCatalogService(@Nonnull Class<T> serviceClass);

    /**
     * Returns catalog manager of given type.
     *
     * @param <T> base param
     * @param managerClass a class for desired instance of catalog manager
     * @return manager
     */
    @Nonnull
    <T extends XBCManager<? extends XBCBase>> T getCatalogManager(@Nonnull Class<T> managerClass);

    /**
     * Returns list of catalog services.
     *
     * @return list of all catalog extensions
     */
    @Nonnull
    List<XBCService<? extends XBCBase>> getCatalogServices();

    /**
     * Returns list of catalog managers.
     *
     * @return list of all catalog extensions
     */
    @Nonnull
    List<XBCManager<? extends XBCBase>> getCatalogManagers();

    /**
     * Adds catalog manager to catalog repository.
     *
     * Should be used only for internal purposes.
     *
     * @param type type of extension
     * @param ext instance of extension, must implement XBCExtension
     */
    void addCatalogManager(Class type, XBCManager<? extends XBCBase> ext);

    /**
     * Adds catalog service to catalog repository.
     *
     * @param type type of extension
     * @param ext instance of extension, must implement XBCExtension
     */
    void addCatalogService(Class type, XBCService<? extends XBCBase> ext);

    /**
     * Processes declaration block and it's children and construct new context.
     *
     * @param parent parent context
     * @param blockProvider data provider
     * @return new context
     */
    XBContext processDeclaration(XBContext parent, XBTPullProvider blockProvider);

    /**
     * Gets path for given specification.
     *
     * @param spec specification
     * @return path
     */
    @Nonnull
    Long[] getSpecPath(@Nonnull XBCSpec spec);

    /**
     * Returns basic block type for given basic type.
     *
     * @param blockType block type
     * @return basic block type
     */
    @Nonnull
    XBBlockType getBasicBlockType(@Nonnull XBBasicBlockType blockType);
}
