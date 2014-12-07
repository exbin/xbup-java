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
package org.xbup.lib.core.block.definition.catalog;

import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.block.declaration.XBGroupDecl;
import org.xbup.lib.core.block.declaration.catalog.XBCFormatDecl;
import org.xbup.lib.core.block.declaration.catalog.XBCGroupDecl;
import org.xbup.lib.core.block.definition.XBFormatDef;
import org.xbup.lib.core.block.definition.XBFormatParam;
import org.xbup.lib.core.block.definition.XBFormatParamConsist;
import org.xbup.lib.core.block.definition.XBFormatParamJoin;
import org.xbup.lib.core.block.definition.XBRevisionDef;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.base.XBCFormatRev;
import org.xbup.lib.core.catalog.base.XBCFormatSpec;
import org.xbup.lib.core.catalog.base.XBCGroupRev;
import org.xbup.lib.core.catalog.base.XBCSpecDef;
import org.xbup.lib.core.catalog.base.service.XBCSpecService;
import org.xbup.lib.core.serial.XBSerializable;

/**
 * XBUP level 1 block definition.
 *
 * @version 0.1.24 2014/12/07
 * @author XBUP Project (http://xbup.org)
 */
public class XBCFormatDef implements XBFormatDef, XBSerializable {

    private final XBCatalog catalog;
    private final XBCFormatSpec formatSpec;

    public XBCFormatDef(XBCatalog catalog, XBCFormatSpec formatSpec) {
        this.catalog = catalog;
        this.formatSpec = formatSpec;
    }

    @Override
    public List<XBFormatParam> getFormatParams() {
        XBCSpecService specService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        List<XBFormatParam> resultList = new ArrayList<>();
        List<XBCSpecDef> specDefs = specService.getSpecDefs(formatSpec);
        for (XBCSpecDef specDef : specDefs) {
            switch (specDef.getType()) {
                case CONS: {
                    resultList.add(new XBFormatParamConsist(new XBCGroupDecl((XBCGroupRev) specDef.getTarget(), catalog)));
                    break;
                }
                case JOIN: {
                    resultList.add(new XBFormatParamJoin(new XBCFormatDecl((XBCFormatRev) specDef.getTarget(), catalog)));
                    break;
                }
            }
        }
        return resultList;
    }

    @Override
    public XBRevisionDef getRevisionDef() {
        return new XBCRevisionDef(catalog, formatSpec);
    }

    @Override
    public XBGroupDecl getGroupDecl(int groupId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
