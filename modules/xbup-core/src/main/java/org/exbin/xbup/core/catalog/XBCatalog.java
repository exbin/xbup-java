/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.catalog;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBBasicBlockType;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.declaration.XBBlockDecl;
import org.exbin.xbup.core.block.declaration.XBContext;
import org.exbin.xbup.core.block.declaration.XBFormatDecl;
import org.exbin.xbup.core.block.declaration.XBGroupDecl;
import org.exbin.xbup.core.catalog.base.XBCFormatSpec;
import org.exbin.xbup.core.catalog.base.XBCGroupSpec;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.manager.XBCManager;
import org.exbin.xbup.core.catalog.base.service.XBCService;
import org.exbin.xbup.core.parser.token.pull.XBTPullProvider;

/**
 * Interface for XBUP level 1 catalog.
 *
 * @version 0.2.1 2017/06/06
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
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
    XBBlockDecl findBlockTypeByPath(Long[] xbCatalogPath, int revision);

    /**
     * Returns a group type for given XB Path.
     *
     * @param xbCatalogPath a path in catalog using xbIndexes
     * @param revision XB index of revision
     * @return group Type for given catalog or null if fails
     */
    @Nullable
    XBGroupDecl findGroupTypeByPath(Long[] xbCatalogPath, int revision);

    /**
     * Returns a format type for given XB Path.
     *
     * @param xbCatalogPath a path in catalog using xbIndexes
     * @param revision XB index of revision
     * @return format Type for given catalog or null if fails
     */
    @Nullable
    XBFormatDecl findFormatTypeByPath(Long[] xbCatalogPath, int revision);

    /**
     * Returns list of binded specifications for given format specification for
     * processing context.
     *
     * @param spec a format specification
     * @return list of group specifications
     */
    @Nonnull
    List<XBGroupDecl> getGroups(XBCFormatSpec spec);

    /**
     * Returns list of binded specifications for given group specification for
     * processing context.
     *
     * @param spec a group specification
     * @return list of block specifications
     */
    @Nonnull
    List<XBBlockDecl> getBlocks(XBCGroupSpec spec);

    /**
     * Returns catalog manager of given type.
     *
     * @param <T> base param
     * @param managerClass a class for desired instance of catalog manager
     * @return manager
     */
    @Nonnull
    <T extends XBCManager<?>> T getCatalogManager(Class<T> managerClass);

    /**
     * Returns catalog service of given type.
     *
     * @param <T> base param
     * @param serviceClass a class for desired instance of catalog service
     * @return service
     */
    @Nonnull
    <T extends XBCService<?>> T getCatalogService(Class<T> serviceClass);

    /**
     * Returns list of catalog services.
     *
     * @return list of all catalog extensions
     */
    @Nonnull
    List<XBCService<?>> getCatalogServices();

    /**
     * Returns list of catalog managers.
     *
     * @return list of all catalog extensions
     */
    @Nonnull
    List<XBCManager<?>> getCatalogManagers();

    /**
     * Adds catalog manager to catalog repository.
     *
     * Should be used only for internal purposes.
     *
     * @param <T> manager class
     * @param type type of extension
     * @param manager instance of extension, must implement XBCExtension
     */
    <T extends XBCManager<?>> void addCatalogManager(Class<T> type, T manager);

    /**
     * Adds catalog service to catalog repository.
     *
     * @param <T> service class
     * @param type type of extension
     * @param service instance of extension, must implement XBCExtension
     */
    <T extends XBCService<?>> void addCatalogService(Class<T> type, T service);

    /**
     * Processes declaration block and it's children and construct new context.
     *
     * @param parent parent context
     * @param blockProvider data provider
     * @return new context
     */
    @Nonnull
    XBContext processDeclaration(@Nullable XBContext parent, XBTPullProvider blockProvider);

    /**
     * Gets path for given specification.
     *
     * @param spec specification
     * @return path
     */
    @Nonnull
    Long[] getSpecPath(XBCSpec spec);

    /**
     * Returns basic block type for given basic type.
     *
     * @param blockType block type
     * @return basic block type
     */
    @Nonnull
    XBBlockType getBasicBlockType(XBBasicBlockType blockType);
}
