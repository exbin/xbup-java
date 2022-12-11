/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.catalog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import org.exbin.xbup.core.block.XBBasicBlockType;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.block.declaration.XBBlockDecl;
import org.exbin.xbup.core.block.declaration.XBContext;
import org.exbin.xbup.core.block.declaration.XBDeclaration;
import org.exbin.xbup.core.block.declaration.XBFormatDecl;
import org.exbin.xbup.core.block.declaration.XBGroup;
import org.exbin.xbup.core.block.declaration.XBGroupDecl;
import org.exbin.xbup.core.block.declaration.local.XBLFormatDecl;
import org.exbin.xbup.core.catalog.base.XBCFormatSpec;
import org.exbin.xbup.core.catalog.base.XBCGroupSpec;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.manager.XBCManager;
import org.exbin.xbup.core.catalog.base.service.XBCService;
import org.exbin.xbup.core.parser.token.pull.XBTPullProvider;

/**
 * XBUP level 1 limited path supporting catalog.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBPCatalog implements XBCatalog {

    private XBContext rootContext;
    private final List<XBFormatDecl> formatDecls = new ArrayList<>();

    @Override
    public XBContext getRootContext() {
        return rootContext;
    }

    public void setRootContext(XBContext rootContext) {
        this.rootContext = rootContext;
    }

    @Override
    public XBBlockDecl findBlockTypeByPath(Long[] xbCatalogPath, int revision) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBGroupDecl findGroupTypeByPath(Long[] xbCatalogPath, int revision) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBFormatDecl findFormatTypeByPath(Long[] catalogPath, int revision) {
        for (XBFormatDecl formatDecl : formatDecls) {
            if (formatDecl instanceof XBLFormatDecl) {
                if (Arrays.equals(((XBLFormatDecl) formatDecl).getCatalogPathAsClassArray(), catalogPath) && formatDecl.getRevision() == revision) {
                    return formatDecl;
                }
            }
        }

        return null;
    }

    @Override
    public List<XBGroupDecl> getGroups(XBCFormatSpec spec) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<XBBlockDecl> getBlocks(XBCGroupSpec spec) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends XBCManager<?>> T getCatalogManager(@Nonnull Class<T> managerClass) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends XBCService<?>> T getCatalogService(@Nonnull Class<T> serviceClass) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<XBCService<?>> getCatalogServices() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<XBCManager<?>> getCatalogManagers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends XBCManager<?>> void addCatalogManager(Class<T> type, T manager) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends XBCService<?>> void addCatalogService(Class<T> type, T service) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBContext processDeclaration(XBContext parent, XBTPullProvider blockProvider) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long[] getSpecPath(XBCSpec spec) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addFormatDecl(XBLFormatDecl formatDecl) {
        formatDecls.add(formatDecl);
    }

    @Override
    public XBBlockType getBasicBlockType(XBBasicBlockType blockType) {
        return new XBFixedBlockType(blockType);
    }

    public void generateContext() {
        rootContext = new XBContext();
        List<XBGroup> groups = new ArrayList<>();
        for (XBFormatDecl formatDecl : formatDecls) {
            for (XBGroupDecl groupDecl : formatDecl.getGroupDecls()) {
                groups.add(XBDeclaration.convertCatalogGroup(groupDecl));
            }
        }

        rootContext.setGroups(groups);
    }
}
