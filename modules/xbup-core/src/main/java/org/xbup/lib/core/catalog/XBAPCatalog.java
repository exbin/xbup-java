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

import java.util.List;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.declaration.XBContext;
import org.xbup.lib.core.block.declaration.XBFormatDecl;
import org.xbup.lib.core.block.declaration.XBGroupDecl;
import org.xbup.lib.core.catalog.base.XBCBase;
import org.xbup.lib.core.catalog.base.XBCFormatSpec;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.manager.XBCManager;
import org.xbup.lib.core.catalog.base.service.XBCService;
import org.xbup.lib.core.parser.token.pull.XBTPullProvider;

/**
 * XBUP level 2 limited path supporting catalog.
 *
 * @version 0.1.24 2014/11/24
 * @author XBUP Project (http://xbup.org)
 */
public class XBAPCatalog implements XBACatalog {

    @Override
    public XBContext getRootContext() {
        throw new UnsupportedOperationException("Not supported yet.");
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
    public XBFormatDecl findFormatTypeByPath(Long[] xbCatalogPath, int revision) {
        throw new UnsupportedOperationException("Not supported yet.");
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
    public XBFixedBlockType findFixedType(XBContext context, XBBlockDecl decl) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBFixedBlockType findFixedType(XBContext context, XBBlockType type) {
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

}
