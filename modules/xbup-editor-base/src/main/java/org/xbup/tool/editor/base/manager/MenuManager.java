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
package org.xbup.tool.editor.base.manager;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import org.xbup.tool.editor.base.api.BasicMenuType;
import org.xbup.tool.editor.base.api.MenuManagement;
import org.xbup.tool.editor.base.api.MenuPositionMode;

/**
 * Manager for menus.
 *
 * @version 0.1.25 2015/04/21
 * @author XBUP Project (http://xbup.org)
 */
public class MenuManager implements MenuManagement {

    private final BaseModuleRepository moduleRepository;
    private MenuManagerMode mode;

    private JMenuBar mainBar;
    private JToolBar toolBar;
    private JPopupMenu mainPopupMenu;
    private Map<Long, JMenuBar> panelMenus;
    private List<MenuGap> menuGaps;
    private MenuGap insertedMenusGap;
    private int toolsMenuPosition;

    private Map<Long, List<JMenuItem>> menuItemRegistrationBank;
    private Map<Long, List<JMenu>> menuRegistrationBank;

    public MenuManager(BaseModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
        init();
    }

    private void init() {
        mainBar = moduleRepository.getMainFrame().getJMenuBar();
        toolBar = moduleRepository.getMainFrame().getToolBar();
        mainPopupMenu = moduleRepository.getMainFrame().getMainPopupMenu();
        setMode(MenuManagerMode.SIMPLE);

        // Fill menu positions
        menuGaps = new ArrayList<>();
        menuGaps.add(new MenuGap(mainBar.getMenu(BasicMenuType.FILE.ordinal()).getItemCount() - 2));
        menuGaps.add(new MenuGap(mainBar.getMenu(BasicMenuType.EDIT.ordinal()).getItemCount()));
        menuGaps.add(new MenuGap(mainBar.getMenu(BasicMenuType.VIEW.ordinal()).getItemCount()));
        menuGaps.add(new MenuGap(0));
        menuGaps.add(new MenuGap(0));
        menuGaps.add(new MenuGap(0));

        insertedMenusGap = new MenuGap(3);
        toolsMenuPosition = -1;
    }

    @Override
    public void extendMenu(JMenu menu, BasicMenuType menuType, MenuPositionMode positionMode) {
        moduleRepository.getMainFrame().initMenuItem(menu);
        int index = 0;
        int lastMax = menu.getMenuComponentCount();
        while (index < lastMax) {
            addMenuItem(menu.getMenuComponent(index), menuType, positionMode);
            int newMax = menu.getMenuComponentCount();
            if (newMax == lastMax) {
                index++;
            }
            lastMax = newMax;
        }
    }

    @Override
    public void addMenuItem(Component menuItem, BasicMenuType menuType, MenuPositionMode positionMode) {
        if (menuType == BasicMenuType.TOOLS) {
            if (toolsMenuPosition < 0) {
                toolsMenuPosition = moduleRepository.getMainFrame().initToolsMenu();
            }
        }
        MenuGap gap = menuGaps.get(menuType.ordinal());
        int position = gap.insertItem(moduleRepository.getActiveModule(), positionMode);
        JMenu menu = getBasicMenu(menuType);
        if (menuType == BasicMenuType.OPTIONS) {
            if (menu.getItemCount() == 1) {
                Component separator = new JSeparator();
                menu.add(separator, 0);
                moduleRepository.getMainFrame().initMenuItem(separator);
            }
        }
        menu.add(menuItem, position);
        moduleRepository.getMainFrame().initMenuItem(menuItem);

        //menuItemRegistrationBank.put(moduleRepository.getActiveModule(), menuItem);
    }

    @Override
    public void insertMenu(JMenu menu, MenuPositionMode positionMode) {
        int position;
        if (mode == MenuManagerMode.FILE) {
            position = insertedMenusGap.insertItem(moduleRepository.getActiveModule(), positionMode);
        } else {
            position = 0;
        }
        mainBar.add(menu, position);
        moduleRepository.getMainFrame().initMenu(menu);

        //menuRegistrationBank.put(moduleRepository.getActiveModule(), menu);
    }

    public JMenu getBasicMenu(BasicMenuType menuType) {
        // TODO: Help menu might be somewhere else
        if (menuType == BasicMenuType.HELP) {
            return mainBar.getMenu(mainBar.getMenuCount() - 1);
        }
        if (menuType.ordinal() < BasicMenuType.TOOLS.ordinal()) {
            return mainBar.getMenu(menuType.ordinal());
        }
        if (menuType == BasicMenuType.TOOLS) {
            return mainBar.getMenu(toolsMenuPosition);
        }
        return mainBar.getMenu(menuType.ordinal() - 1);
    }

    public MenuManagerMode getMode() {
        return mode;
    }

    public void setMode(MenuManagerMode mode) {
        this.mode = mode;
        if (mode == MenuManagerMode.SIMPLE) {
            mainBar.getMenu(BasicMenuType.FILE.ordinal()).setVisible(false);
            mainBar.getMenu(BasicMenuType.VIEW.ordinal()).setVisible(false);
            toolBar.setVisible(false);
        } else {
            mainBar.getMenu(BasicMenuType.FILE.ordinal()).setVisible(true);
            mainBar.getMenu(BasicMenuType.VIEW.ordinal()).setVisible(true);
            toolBar.setVisible(true);
        }
    }

    @Override
    public void extendToolBar(JToolBar appendedToolBar) {
        int componentCount = appendedToolBar.getComponentCount();
        for (int i = 0; i < componentCount; i++) {
            toolBar.add(appendedToolBar.getComponent(0));
        }
    }

    @Override
    public void insertMainPopupMenu(JPopupMenu popupMenu, int position) {
        Component[] menuItems = mainPopupMenu.getComponents();
        for (int menuItemIndex = 0; menuItemIndex < menuItems.length; menuItemIndex++) {
            Component component = menuItems[menuItemIndex];
            if (component instanceof JMenuItem) {
                JMenuItem menuItem = new JMenuItem();
                menuItem.setText(((JMenuItem) component).getText());
                menuItem.setToolTipText(((JMenuItem) component).getToolTipText());
                menuItem.setAccelerator(((JMenuItem) component).getAccelerator());
                menuItem.setEnabled(((JMenuItem) component).isEnabled());
                ActionListener[] listeners = ((JMenuItem) component).getActionListeners();
                if ((listeners != null) && (listeners.length > 0)) {
                    menuItem.addActionListener(listeners[0]);
                }
                menuItem.setAction(((JMenuItem) component).getAction());
                popupMenu.insert(menuItem, position + menuItemIndex);
            } else if (component instanceof JSeparator) {
                popupMenu.insert(new JSeparator(), position + menuItemIndex);
            }
            moduleRepository.getMainFrame().initPopupMenu(popupMenu);
        }
    }

    public class MenuGap {

        private final Map<Long, Integer> panelItemsCount;

        private int gapPosition;
        private int beforeItemsCount;
        private int afterItemsCount;
        private int topItemsCount;
        private int bottomItemsCount;

        public MenuGap(int gapPosition) {
            this.gapPosition = gapPosition;
            this.beforeItemsCount = 0;
            panelItemsCount = new HashMap<>();
            this.afterItemsCount = 0;
            this.topItemsCount = 0;
            this.bottomItemsCount = 0;
        }

        public int getGapPosition() {
            return gapPosition;
        }

        public int getBeforeItemsCount() {
            return beforeItemsCount;
        }

        public void setBeforeItemsCount(int beforeItemsCount) {
            this.beforeItemsCount = beforeItemsCount;
        }

        public int getPanelItemsCount() {
            // TODO
            return 0;
        }

        public void setGapPosition(int gapPosition) {
            this.gapPosition = gapPosition;
        }

        public void setPanelItemsCount(int panelItemsCount) {
            // TODO this.panelItemsCount = panelItemsCount;
        }

        public int getAfterItemsCount() {
            return afterItemsCount;
        }

        public void setAfterItemsCount(int afterItemsCount) {
            this.afterItemsCount = afterItemsCount;
        }

        public int insertItem(long activeModule, MenuPositionMode positionMode) {
            int insertPosition = -1;
            switch (positionMode) {
                case TOP: {
                    insertPosition = topItemsCount;
                    topItemsCount++;
                    break;
                }
                case TOP_LAST: {
                    // This is fixed operation for edit menu for now.
                    insertPosition = topItemsCount + 2;
                    topItemsCount++;
                    break;
                }
                case BEFORE_PANEL: {
                    insertPosition = gapPosition + topItemsCount + beforeItemsCount;
                    beforeItemsCount++;
                    break;
                }
                case PANEL: {
                    Integer itemsCount = panelItemsCount.get(activeModule);
                    if (itemsCount == null) {
                        itemsCount = 0;
                    }
                    insertPosition = gapPosition + topItemsCount + beforeItemsCount + itemsCount;
                    itemsCount++;
                    panelItemsCount.put(activeModule, itemsCount);
                    break;
                }
                case AFTER_PANEL: {
                    Integer itemsCount = panelItemsCount.get(activeModule);
                    if (itemsCount == null) {
                        itemsCount = 1;
                    }
                    insertPosition = gapPosition + topItemsCount + beforeItemsCount + itemsCount + afterItemsCount;
                    afterItemsCount++;
                    break;
                }
                case BOTTOM: {
                    Integer itemsCount = panelItemsCount.get(activeModule);
                    if (itemsCount == null) {
                        itemsCount = 1;
                    }
                    insertPosition = gapPosition + topItemsCount + beforeItemsCount + itemsCount + afterItemsCount + bottomItemsCount;
                    bottomItemsCount++;
                    break;
                }
            }

            return insertPosition;
        }
    }

    public enum MenuManagerMode {

        SIMPLE,
        FILE
    }
}
