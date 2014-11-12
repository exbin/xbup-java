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
package org.xbup.tool.editor.module.service_manager.catalog.panel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.base.XBCBase;
import org.xbup.lib.core.catalog.base.service.XBCService;
import org.xbup.lib.core.catalog.base.XBCExtension;

/**
 * List Model for Catalog Extensions.
 *
 * @version 0.1.21 2011/12/31
 * @author XBUP Project (http://xbup.org)
 */
public class CatalogExtensionsListModel extends AbstractListModel {

    private final XBCatalog catalog;
    private final List<XBCService<? extends XBCBase>> extensions;

    public CatalogExtensionsListModel(XBCatalog catalog) {
        this.catalog = catalog;
        extensions = new ArrayList<>();
        for (XBCService<? extends XBCBase> service : catalog.getCatalogServices()) {
            if (service instanceof XBCExtension) {
                extensions.add(service);
            }
        }
    }

    @Override
    public int getSize() {
        return extensions.size();
    }

    @Override
    public String getElementAt(int index) {
        return ((XBCExtension) extensions.get(index)).getExtensionName();
    }
}
