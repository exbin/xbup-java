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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import org.xbup.lib.framework.gui.menu.api.ActionMenuContribution;
import org.xbup.lib.framework.gui.menu.api.GuiMenuModuleApi;
import org.xbup.lib.framework.gui.menu.api.MenuContribution;
import org.xbup.lib.framework.gui.menu.api.MenuGroup;
import org.xbup.lib.framework.gui.menu.api.MenuPosition;
import org.xbup.lib.framework.gui.menu.api.PositionMode;
import org.xbup.lib.framework.gui.menu.api.SeparationMode;
import org.xbup.lib.framework.gui.menu.api.SubMenuContribution;
import org.xbup.lib.framework.gui.utils.ActionUtils;

/**
 * Menu handler.
 *
 * @version 0.2.0 2016/01/10
 * @author XBUP Project (http://xbup.org)
 */
public class MenuHandler {

    /**
     * Menu records: menu id -> menu definition.
     */
    private Map<String, MenuDefinition> menus = new HashMap<>();

    /**
     * Menu group records: menu id -> menu group.
     */
    private Map<String, List<MenuGroup>> menuGroups = new HashMap<>();

    /**
     * Menu modified flags.
     */
    private Set<String> menuModified = new HashSet<>();

    /**
     * Map of plugins usage per menu id.
     */
    private Map<String, String> pluginsUsage = new HashMap<>();

    public MenuHandler() {
    }

    public void buildMenu(JPopupMenu targetMenu, String menuId) {
        MenuHandler.this.buildMenu(new PopupMenuWrapper(targetMenu), menuId);
    }

    private void buildMenu(MenuTarget targetMenu, String menuId) {
        MenuDefinition menuDef = menus.get(menuId);

        if (menuDef == null) {
            return;
        }

        List<MenuGroupRecord> groupRecords = new LinkedList<>();

        // Create list of build-in groups
        Map<String, MenuGroupRecord> groupsMap = new HashMap<>();
        for (PositionMode mode : PositionMode.values()) {
            MenuGroupRecord menuGroupRecord = new MenuGroupRecord(mode.name());
            groupsMap.put(mode.name(), menuGroupRecord);
            groupRecords.add(menuGroupRecord);
        }

        // Build full tree of groups
        List<MenuGroup> groups = menuGroups.get(menuId);
        if (groups != null) {
            for (MenuGroup group : groups) {
                String groupId = group.getGroupId();
                SeparationMode separationMode = group.getSeparationMode();
                MenuPosition position = group.getPosition();
                if (position.getBasicMode() != null) {
                    MenuGroupRecord groupRecord = groupsMap.get(position.getBasicMode().name());
                    MenuGroupRecord menuGroupRecord = new MenuGroupRecord(groupId);
                    menuGroupRecord.separationMode = separationMode;
                    groupRecord.subGroups.add(menuGroupRecord);
                    groupsMap.put(groupId, menuGroupRecord);
                } else {
                    MenuGroupRecord groupRecord = groupsMap.get(position.getGroupId());
                    MenuGroupRecord menuGroupRecord = new MenuGroupRecord(groupId);
                    menuGroupRecord.separationMode = separationMode;
                    groupRecord.subGroups.add(menuGroupRecord);
                    groupsMap.put(groupId, menuGroupRecord);
                }
            }
        }

        // Go thru all contributions and link them to its target group
        for (MenuContribution contribution : menuDef.getContributions()) {
            MenuPosition menuPosition = contribution.getMenuPosition();
            if (menuPosition.getBasicMode() != null) {
                MenuGroupRecord menuGroupRecord = groupsMap.get(menuPosition.getBasicMode().name());
                menuGroupRecord.contributions.add(contribution);
            } else {
                MenuGroupRecord menuGroupRecord = groupsMap.get(menuPosition.getGroupId());
                menuGroupRecord.contributions.add(contribution);
            }
        }

        processMenuGroup(groupRecords, targetMenu);
    }

    private void processMenuGroup(List<MenuGroupRecord> groups, MenuTarget targetMenu) {
        List<MenuGroupRecordPathNode> processingPath = new LinkedList<>();
        processingPath.add(new MenuGroupRecordPathNode(groups));

        boolean separatorQueued = false;
        boolean menuContinues = false;

        while (!processingPath.isEmpty()) {
            MenuGroupRecordPathNode pathNode = processingPath.get(processingPath.size() - 1);
            if (pathNode.childIndex == pathNode.records.size()) {
                processingPath.remove(processingPath.size() - 1);
                continue;
            }

            MenuGroupRecord groupRecord = pathNode.records.get(pathNode.childIndex);
            pathNode.childIndex++;

            if ((groupRecord.separationMode == SeparationMode.ABOVE || groupRecord.separationMode == SeparationMode.AROUND) && menuContinues) {
                targetMenu.addSeparator();
                separatorQueued = false;
            }

            for (MenuContribution contribution : groupRecord.contributions) {
                if (separatorQueued) {
                    targetMenu.addSeparator();
                    separatorQueued = false;
                }

                if (contribution instanceof ActionMenuContribution) {
                    Action action = ((ActionMenuContribution) contribution).getAction();
                    ActionUtils.ActionType actionType = (ActionUtils.ActionType) action.getValue(ActionUtils.ACTION_TYPE);
                    JMenuItem menuItem;
                    if (actionType != null) {
                        switch (actionType) {
                            case CHECK: {
                                menuItem = new JCheckBoxMenuItem(action);
                                break;
                            }
                            case RADIO: {
                                menuItem = new JRadioButtonMenuItem(action);
                                break;
                            }
                            default: {
                                menuItem = new JMenuItem(action);
                            }
                        }
                    } else {
                        menuItem = new JMenuItem(action);
                    }
                    
                    Object dialogMode = action.getValue(ActionUtils.ACTION_DIALOG_MODE);
                    if (dialogMode instanceof Boolean && ((Boolean) dialogMode)) {
                        menuItem.setText(menuItem.getText() + GuiMenuModuleApi.DIALOG_MENUITEM_EXT);
                    }

                    targetMenu.add(menuItem);
                } else if (contribution instanceof SubMenuContribution) {
                    SubMenuContribution subMenuContribution = (SubMenuContribution) contribution;
                    JMenu subMenu = new JMenu();
                    MenuHandler.this.buildMenu(subMenu.getPopupMenu(), subMenuContribution.getMenuId());
                    subMenu.setText(subMenuContribution.getName());
                    if (subMenu.getMenuComponentCount() > 0) {
                        targetMenu.add(subMenu);
                    }
                }

                menuContinues = true;
            }

            if (groupRecord.separationMode == SeparationMode.AROUND || groupRecord.separationMode == SeparationMode.BELOW) {
                separatorQueued = true;
            }

            if (!groupRecord.subGroups.isEmpty()) {
                processingPath.add(new MenuGroupRecordPathNode(groupRecord.subGroups));
            }
        }
    }

    public void buildMenu(JMenuBar targetMenuBar, String menuId) {
        buildMenu(new MenuBarWrapper(targetMenuBar), menuId);
    }

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

    public void registerMenuGroup(String menuId, MenuGroup menuGroup) {
        List<MenuGroup> groups = menuGroups.get(menuId);
        if (groups == null) {
            groups = new LinkedList<>();
            menuGroups.put(menuId, groups);
        }
        groups.add(menuGroup);
    }

    public void registerMenuItem(String menuId, String pluginId, Action action, MenuPosition position) {
        MenuDefinition menuDef = menus.get(menuId);
        if (menuDef == null) {
            throw new IllegalStateException("Menu with Id " + menuId + " doesn't exist");
        }

        ActionMenuContribution menuContribution = new ActionMenuContribution(action, position);
        menuDef.getContributions().add(menuContribution);
    }

    public void registerMenuItem(String menuId, String pluginId, String subMenuId, String subMenuName, MenuPosition position) {
        MenuDefinition menuDef = menus.get(menuId);
        if (menuDef == null) {
            throw new IllegalStateException("Menu with Id " + menuId + " doesn't exist");
        }

        SubMenuContribution menuContribution = new SubMenuContribution(subMenuId, subMenuName, position);
        menuDef.getContributions().add(menuContribution);
    }

    private class MenuGroupRecord {

        String groupId;
        SeparationMode separationMode;
        List<MenuGroupRecord> subGroups = new LinkedList<>();
        List<MenuContribution> contributions = new LinkedList<>();

        public MenuGroupRecord(String groupId) {
            this.groupId = groupId;
        }

        public MenuGroupRecord(String groupId, SeparationMode separationMode) {
            this(groupId);
            this.separationMode = separationMode;
        }
    }

    private class MenuGroupRecordPathNode {

        List<MenuGroupRecord> records;
        int childIndex;

        public MenuGroupRecordPathNode(List<MenuGroupRecord> records) {
            this.records = records;
        }
    }

    private static interface MenuTarget {

        void add(JMenu menuItem);

        void add(JMenuItem menuItem);

        void addSeparator();
    }

    private static class MenuBarWrapper implements MenuTarget {

        private final JMenuBar menuBar;

        public MenuBarWrapper(JMenuBar menuBar) {
            this.menuBar = menuBar;
        }

        @Override
        public void add(JMenu menuItem) {
            menuBar.add(menuItem);
        }

        @Override
        public void add(JMenuItem menuItem) {
            menuBar.add(menuItem);
        }

        @Override
        public void addSeparator() {
        }
    }

    private static class PopupMenuWrapper implements MenuTarget {

        private final JPopupMenu menu;

        public PopupMenuWrapper(JPopupMenu menu) {
            this.menu = menu;
        }

        @Override
        public void add(JMenu menuItem) {
            menu.add(menuItem);
        }

        @Override
        public void add(JMenuItem menuItem) {
            menu.add(menuItem);
        }

        @Override
        public void addSeparator() {
            menu.addSeparator();
        }
    }
}