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
package org.xbup.lib.core.catalog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.declaration.XBContext;
import org.xbup.lib.core.block.declaration.XBDeclaration;
import org.xbup.lib.core.block.declaration.XBFormatDecl;
import org.xbup.lib.core.block.declaration.XBGroup;
import org.xbup.lib.core.block.declaration.XBGroupDecl;
import org.xbup.lib.core.block.declaration.local.XBLFormatDecl;
import org.xbup.lib.core.catalog.base.XBCBase;
import org.xbup.lib.core.catalog.base.XBCFormatSpec;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.manager.XBCManager;
import org.xbup.lib.core.catalog.base.service.XBCService;
import org.xbup.lib.core.parser.token.pull.XBTPullProvider;

/**
 * XBUP level 1 limited path supporting catalog.
 *
 * @version 0.1.25 2015/02/25
 * @author XBUP Project (http://xbup.org)
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
    public XBCService<? extends XBCBase> getCatalogService(Class type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBCManager<? extends XBCBase> getCatalogManager(Class type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<XBCService<? extends XBCBase>> getCatalogServices() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<XBCManager<? extends XBCBase>> getCatalogManagers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addCatalogManager(Class type, XBCManager<? extends XBCBase> ext) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addCatalogService(Class type, XBCService<? extends XBCBase> ext) {
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
                rootContext.getGroups().add(XBDeclaration.convertCatalogGroup(groupDecl));
            }
        }

        rootContext.setGroups(groups);
    }
}
