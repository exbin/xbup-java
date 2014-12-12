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
package org.xbup.tool.editor.module.service_manager.panel;

import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.tool.editor.base.api.MainFrameManagement;
import org.xbup.tool.editor.base.api.MenuManagement;

/**
 * Interface for catalog management panels.
 *
 * @version 0.1.24 2014/12/12
 * @author XBUP Project (http://xbup.org)
 */
public interface CatalogManagerPanelable {

    /**
     * Passes menu management.
     *
     * @param menuManagement menu management
     */
    public void setMenuManagement(MenuManagement menuManagement);

    /**
     * Passes access to catalog.
     *
     * @param catalog catalog
     */
    public void setCatalog(XBACatalog catalog);

    /**
     * Passes main frame management.
     *
     * @param mainFrameManagement main frame management
     */
    public void setMainFrameManagement(MainFrameManagement mainFrameManagement);
}
