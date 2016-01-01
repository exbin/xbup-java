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
package org.xbup.lib.framework.gui.menu;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.lib.framework.gui.api.XBApplication;
import org.xbup.lib.framework.gui.menu.api.GuiMenuModuleApi;
import org.xbup.lib.framework.gui.menu.api.MenuPosition;

/**
 * Implementation of XBUP framework menu module.
 *
 * @version 0.2.0 2015/12/16
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class GuiMenuModule implements GuiMenuModuleApi {

    private XBApplication application;

    // Menu records
    private Map<String, MenuDefinition> menus = new HashMap<>();

    // Menu cache map - menuId -> menu instance
    private Map<String, JMenu> menuCache = new HashMap<>();
    private Map<String, JMenuItem> pluginLinks = new HashMap<>();

    public GuiMenuModule() {
    }

    @Override
    public void init(XBApplication application) {
        this.application = application;
    }

    @Override
    public void unregisterPlugin(String pluginId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JMenu getMenu(String menuId) {
        JMenu menu = menuCache.get(menuId);
        if (menu != null) {
            return menu;
        }
        
        return generateMenu(menuId);
    }

    @Override
    public void registerMenu(String menuId, String pluginId, JMenu menu) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void registerMenuItem(String menuId, String pluginId, JMenu item, MenuPosition position) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void registerMenuItem(String menuId, String pluginId, JMenuItem item, MenuPosition position) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private JMenu generateMenu(String menuId) {
        JMenu menu = new JMenu();
        menu.add(new JMenuItem("Test"));
        return menu;
    }
}
