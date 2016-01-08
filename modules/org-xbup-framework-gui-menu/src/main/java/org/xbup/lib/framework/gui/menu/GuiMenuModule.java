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
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import org.xbup.lib.framework.gui.api.XBApplication;
import org.xbup.lib.framework.gui.menu.api.GuiMenuModuleApi;
import org.xbup.lib.framework.gui.menu.api.ActionMenuContribution;
import org.xbup.lib.framework.gui.menu.api.MenuContribution;
import org.xbup.lib.framework.gui.menu.api.MenuPosition;
import org.xbup.lib.framework.gui.menu.api.SubMenuContribution;

/**
 * Implementation of XBUP framework menu module.
 *
 * @version 0.2.0 2016/01/02
 * @author XBUP Project (http://xbup.org)
 */
@PluginImplementation
public class GuiMenuModule implements GuiMenuModuleApi {

    private XBApplication application;

    /**
     * Menu records: menu id -> menu definition.
     */
    private Map<String, MenuDefinition> menus = new HashMap<>();

    /**
     * Menu cache map - menu id -> menu instance.
     *
     * Empty cache means menu needs refresh.
     */
    private Map<String, JMenu> menuCache = new HashMap<>();

    /**
     * Map of plugins usage per menu id.
     */
    private Map<String, String> pluginsUsage = new HashMap<>();

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
    public void getMenu(JPopupMenu targetMenu, String menuId) {
        MenuDefinition menuDef = menus.get(menuId);
        if (menuDef != null) {
            for (MenuContribution contribution : menuDef.getContributions()) {
                if (contribution instanceof ActionMenuContribution) {
                    Action action = ((ActionMenuContribution) contribution).getAction();
                    targetMenu.add(new JMenuItem(action));
                } else if (contribution instanceof SubMenuContribution) {
                    SubMenuContribution subMenuContribution = (SubMenuContribution) contribution;
                    JMenu subMenu = new JMenu();
                    getMenu(subMenu.getPopupMenu(), subMenuContribution.getMenuId());
                    subMenu.setText(subMenuContribution.getName());
                    if (subMenu.getMenuComponentCount() > 0) {
                        targetMenu.add(subMenu);
                    }
                }
            }
        }
    }

    @Override
    public void getMenu(JMenuBar targetMenuBar, String menuId) {
        MenuDefinition menuDef = menus.get(menuId);
        if (menuDef != null) {
            for (MenuContribution contribution : menuDef.getContributions()) {
                if (contribution instanceof ActionMenuContribution) {
                    Action action = ((ActionMenuContribution) contribution).getAction();
                    targetMenuBar.add(new JMenuItem(action));
                } else if (contribution instanceof SubMenuContribution) {
                    SubMenuContribution subMenuContribution = (SubMenuContribution) contribution;
                    JMenu subMenu = new JMenu();
                    getMenu(subMenu.getPopupMenu(), subMenuContribution.getMenuId());
                    subMenu.setText(subMenuContribution.getName());
                    if (subMenu.getMenuComponentCount() > 0) {
                        targetMenuBar.add(subMenu);
                    }
                }
            }
        }
    }

    @Override
    public void registerMenu(String menuId, String pluginId) {
        if (menuId == null) {
            throw new NullPointerException("Menu Id cannot be null");
        }
        if (pluginId == null) {
            throw new NullPointerException("Plugin Id cannot be null");
        }

        MenuDefinition menu = menus.get(menuId);
        if (menu != null) {
            throw new IllegalStateException("Menu with ID " + menuId + " already exists.");
        }

        MenuDefinition menuDefinition = new MenuDefinition(pluginId);
        menus.put(menuId, menuDefinition);
    }

    @Override
    public void registerMenuItem(String menuId, String pluginId, JMenu item, MenuPosition position) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void registerMenuItem(String menuId, String pluginId, JMenuItem item, MenuPosition position) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void registerMenuItem(String menuId, String pluginId, Action action, MenuPosition position) {
        MenuDefinition menuDef = menus.get(menuId);
        if (menuDef == null) {
            throw new IllegalStateException("Menu with Id " + menuId + " doesn't exist");
        }

        ActionMenuContribution menuContribution = new ActionMenuContribution();
        menuContribution.setAction(action);
        menuDef.getContributions().add(menuContribution);
    }

    @Override
    public void registerMenuItem(String menuId, String pluginId, String subMenuId, String subMenuName, MenuPosition position) {
        MenuDefinition menuDef = menus.get(menuId);
        if (menuDef == null) {
            throw new IllegalStateException("Menu with Id " + menuId + " doesn't exist");
        }

        SubMenuContribution menuContribution = new SubMenuContribution();
        menuContribution.setMenuId(subMenuId);
        menuContribution.setName(subMenuName);
        menuDef.getContributions().add(menuContribution);
    }
}
