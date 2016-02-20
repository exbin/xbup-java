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
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import org.xbup.lib.framework.gui.menu.api.ActionMenuContribution;
import org.xbup.lib.framework.gui.menu.api.DirectMenuContribution;
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
 * @version 0.2.0 2016/02/20
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

        Map<String, List<MenuContribution>> beforeItem = new HashMap<>();
        Map<String, List<MenuContribution>> afterItem = new HashMap<>();

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
            } else if (menuPosition.getNextToMode() != null) {
                switch (menuPosition.getNextToMode()) {
                    case BEFORE: {
                        List<MenuContribution> contributions = beforeItem.get(menuPosition.getGroupId());
                        if (contributions == null) {
                            contributions = new LinkedList<>();
                            beforeItem.put(menuPosition.getGroupId(), contributions);
                        }
                        contributions.add(contribution);
                        break;
                    }
                    case AFTER: {
                        List<MenuContribution> contributions = afterItem.get(menuPosition.getGroupId());
                        if (contributions == null) {
                            contributions = new LinkedList<>();
                            afterItem.put(menuPosition.getGroupId(), contributions);
                        }
                        contributions.add(contribution);
                        break;
                    }
                    default:
                        throw new IllegalStateException();
                }
            } else {
                MenuGroupRecord menuGroupRecord = groupsMap.get(menuPosition.getGroupId());
                menuGroupRecord.contributions.add(contribution);
            }
        }

        Map<String, ButtonGroup> buttonGroups = new HashMap<>();
        processMenuGroup(groupRecords, beforeItem, afterItem, targetMenu, buttonGroups);
    }

    private void processMenuGroup(List<MenuGroupRecord> groups, Map<String, List<MenuContribution>> beforeItem, Map<String, List<MenuContribution>> afterItem, final MenuTarget targetMenu, final Map<String, ButtonGroup> buttonGroups) {
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

                // Process all contributions, but don't insert them yet
                List<QueuedContribution> queue = new LinkedList<>();
                queue.add(new QueuedContribution(null, contribution));
                ProcessedContribution rootProcessed = null;
                while (!queue.isEmpty()) {
                    final QueuedContribution next = queue.remove(0);
                    ProcessedContribution processed;
                    if (next.contribution instanceof ActionMenuContribution) {
                        processed = new ProcessedContribution() {
                            JMenuItem menuItem;

                            @Override
                            public void process() {
                                Action action = ((ActionMenuContribution) next.contribution).getAction();
                                ActionUtils.ActionType actionType = (ActionUtils.ActionType) action.getValue(ActionUtils.ACTION_TYPE);
                                if (actionType != null) {
                                    switch (actionType) {
                                        case CHECK: {
                                            menuItem = new JCheckBoxMenuItem(action);
                                            break;
                                        }
                                        case RADIO: {
                                            menuItem = new JRadioButtonMenuItem(action);
                                            String radioGroup = (String) action.getValue(ActionUtils.ACTION_RADIO_GROUP);
                                            ButtonGroup buttonGroup = buttonGroups.get(radioGroup);
                                            if (buttonGroup == null) {
                                                buttonGroup = new ButtonGroup();
                                                buttonGroups.put(radioGroup, buttonGroup);
                                            }
                                            buttonGroup.add(menuItem);
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
                            }

                            @Override
                            public String getName() {
                                return menuItem.getText();
                            }

                            @Override
                            public void finish() {
                                targetMenu.add(menuItem);
                            }
                        };
                    } else if (next.contribution instanceof SubMenuContribution) {
                        processed = new ProcessedContribution() {
                            JMenu subMenu;

                            @Override
                            public void process() {
                                SubMenuContribution subMenuContribution = (SubMenuContribution) next.contribution;
                                subMenu = new JMenu();
                                MenuHandler.this.buildMenu(subMenu.getPopupMenu(), subMenuContribution.getMenuId());
                                subMenu.setText(subMenuContribution.getName());
                            }

                            @Override
                            public String getName() {
                                return subMenu.getText();
                            }

                            @Override
                            public void finish() {
                                if (subMenu.getMenuComponentCount() > 0) {
                                    targetMenu.add(subMenu);
                                }
                            }
                        };
                    } else if (next.contribution instanceof DirectMenuContribution) {
                        processed = new ProcessedContribution() {
                            DirectMenuContribution directMenuContribution;

                            @Override
                            public void process() {
                                directMenuContribution = (DirectMenuContribution) next.contribution;
                            }

                            @Override
                            public String getName() {
                                return directMenuContribution.getMenu().getName();
                            }

                            @Override
                            public void finish() {
                                targetMenu.add(directMenuContribution.getMenu());
                            }
                        };
                    } else {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    processed.process();
                    if (next.parent == null) {
                        rootProcessed = processed;
                    }
                    String name = processed.getName();
                    if (next.contribution.getMenuPosition().getNextToMode() != null) {
                        switch (next.contribution.getMenuPosition().getNextToMode()) {
                            case BEFORE: {
                                next.parent.before.add(processed);
                                break;
                            }
                            case AFTER: {
                                next.parent.after.add(processed);
                                break;
                            }
                            default:
                                throw new IllegalStateException();
                        }
                    }
                    List<MenuContribution> nextToBefore = beforeItem.get(name);
                    if (nextToBefore != null) {
                        for (MenuContribution menuContribution : nextToBefore) {
                            queue.add(new QueuedContribution(processed, menuContribution));
                        }
                    }

                    List<MenuContribution> nextToAfter = afterItem.get(name);
                    if (nextToAfter != null) {
                        for (MenuContribution menuContribution : nextToAfter) {
                            queue.add(new QueuedContribution(processed, menuContribution));
                        }
                    }
                }

                // Perform insertion of all processed menu contributions
                List<OrderingContribution> orderingPath = new LinkedList<>();

                orderingPath.add(new OrderingContribution(OrderingMode.BEFORE, rootProcessed));
                while (!orderingPath.isEmpty()) {
                    OrderingContribution orderingContribution = orderingPath.get(orderingPath.size() - 1);
                    switch (orderingContribution.mode) {
                        case BEFORE: {
                            if (orderingContribution.processed.before.isEmpty()) {
                                orderingContribution.mode = OrderingMode.ITEM;
                            } else {
                                orderingPath.add(new OrderingContribution(OrderingMode.BEFORE, orderingContribution.processed.before.remove(0)));
                            }
                            break;
                        }
                        case ITEM: {
                            orderingContribution.processed.finish();
                            orderingContribution.mode = OrderingMode.AFTER;
                            break;
                        }
                        case AFTER: {
                            if (orderingContribution.processed.after.isEmpty()) {
                                orderingPath.remove(orderingPath.size() - 1);
                            } else {
                                orderingPath.add(new OrderingContribution(OrderingMode.BEFORE, orderingContribution.processed.after.remove(0)));
                            }
                            break;
                        }
                        default:
                            throw new IllegalStateException();
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

    boolean menuGroupExists(String menuId, String groupId) {
        List<MenuGroup> menuGroupDefs = menuGroups.get(menuId);
        if (menuGroupDefs == null) {
            return false;
        }

        for (MenuGroup menuGroup : menuGroupDefs) {
            if (groupId.equals(menuGroup.getGroupId())) {
                return true;
            }
        }
        
        return false;
    }

    private static abstract class ProcessedContribution {

        List<ProcessedContribution> before = new LinkedList<>();
        List<ProcessedContribution> after = new LinkedList<>();

        abstract void process();

        abstract String getName();

        abstract void finish();
    }

    private static class QueuedContribution {

        ProcessedContribution parent;
        MenuContribution contribution;

        public QueuedContribution(ProcessedContribution parent, MenuContribution contribution) {
            this.parent = parent;
            this.contribution = contribution;
        }
    }

    private static class OrderingContribution {

        OrderingMode mode;
        ProcessedContribution processed;

        public OrderingContribution(OrderingMode mode, ProcessedContribution processed) {
            this.mode = mode;
            this.processed = processed;
        }
    }

    private enum OrderingMode {
        BEFORE, ITEM, AFTER
    };

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

    public void registerMenuItem(String menuId, String pluginId, JMenu menu, MenuPosition position) {
        MenuDefinition menuDef = menus.get(menuId);
        if (menuDef == null) {
            throw new IllegalStateException("Menu with Id " + menuId + " doesn't exist");
        }

        DirectMenuContribution menuContribution = new DirectMenuContribution(menu, position);
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
