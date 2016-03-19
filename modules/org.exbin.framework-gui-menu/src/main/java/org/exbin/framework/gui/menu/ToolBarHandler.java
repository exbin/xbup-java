/*
 * Copyright (C) ExBin Project
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
package org.exbin.framework.gui.menu;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;
import org.exbin.framework.gui.menu.api.ActionToolBarContribution;
import org.exbin.framework.gui.menu.api.PositionMode;
import org.exbin.framework.gui.menu.api.SeparationMode;
import org.exbin.framework.gui.menu.api.ToolBarContribution;
import org.exbin.framework.gui.menu.api.ToolBarGroup;
import org.exbin.framework.gui.menu.api.ToolBarPosition;
import org.exbin.framework.gui.utils.ActionUtils;

/**
 * Tool bar handler.
 *
 * @version 0.2.0 2016/01/11
 * @author ExBin Project (http://exbin.org)
 */
public class ToolBarHandler {

    /**
     * Tool bar records: tool bar id -> tool bar definition.
     */
    private Map<String, ToolBarDefinition> toolBars = new HashMap<>();

    /**
     * Tool bar group records: tool bar id -> tool bar group.
     */
    private Map<String, List<ToolBarGroup>> toolBarGroups = new HashMap<>();

    /**
     * Tool bar modified flags.
     */
    private Set<String> toolBarModified = new HashSet<>();

    /**
     * Map of plugins usage per tool bar id.
     */
    private Map<String, String> pluginsUsage = new HashMap<>();

    public ToolBarHandler() {
    }

    public void buildToolBar(JToolBar targetToolBar, String toolBarId) {
        ToolBarDefinition toolBarDef = toolBars.get(toolBarId);

        if (toolBarDef == null) {
            return;
        }

        List<ToolBarGroupRecord> groupRecords = new LinkedList<>();

        // Create list of build-in groups
        Map<String, ToolBarGroupRecord> groupsMap = new HashMap<>();
        for (PositionMode mode : PositionMode.values()) {
            ToolBarGroupRecord toolBarGroupRecord = new ToolBarGroupRecord(mode.name());
            groupsMap.put(mode.name(), toolBarGroupRecord);
            groupRecords.add(toolBarGroupRecord);
        }

        // Build full tree of groups
        List<ToolBarGroup> groups = toolBarGroups.get(toolBarId);
        if (groups != null) {
            for (ToolBarGroup group : groups) {
                String groupId = group.getGroupId();
                SeparationMode separationMode = group.getSeparationMode();
                ToolBarPosition position = group.getPosition();
                if (position.getBasicMode() != null) {
                    ToolBarGroupRecord groupRecord = groupsMap.get(position.getBasicMode().name());
                    ToolBarGroupRecord toolBarGroupRecord = new ToolBarGroupRecord(groupId);
                    toolBarGroupRecord.separationMode = separationMode;
                    groupRecord.subGroups.add(toolBarGroupRecord);
                    groupsMap.put(groupId, toolBarGroupRecord);
                } else {
                    ToolBarGroupRecord groupRecord = groupsMap.get(position.getGroupId());
                    ToolBarGroupRecord toolBarGroupRecord = new ToolBarGroupRecord(groupId);
                    toolBarGroupRecord.separationMode = separationMode;
                    groupRecord.subGroups.add(toolBarGroupRecord);
                    groupsMap.put(groupId, toolBarGroupRecord);
                }
            }
        }

        // Go thru all contributions and link them to its target group
        for (ToolBarContribution contribution : toolBarDef.getContributions()) {
            ToolBarPosition toolBarPosition = contribution.getToolBarPosition();
            if (toolBarPosition.getBasicMode() != null) {
                ToolBarGroupRecord toolBarGroupRecord = groupsMap.get(toolBarPosition.getBasicMode().name());
                toolBarGroupRecord.contributions.add(contribution);
            } else {
                ToolBarGroupRecord toolBarGroupRecord = groupsMap.get(toolBarPosition.getGroupId());
                toolBarGroupRecord.contributions.add(contribution);
            }
        }

        processToolBarGroup(groupRecords, targetToolBar);
    }

    private void processToolBarGroup(List<ToolBarGroupRecord> groups, JToolBar targetToolBar) {
        List<ToolBarGroupRecordPathNode> processingPath = new LinkedList<>();
        processingPath.add(new ToolBarGroupRecordPathNode(groups));

        boolean separatorQueued = false;
        boolean toolBarContinues = false;

        while (!processingPath.isEmpty()) {
            ToolBarGroupRecordPathNode pathNode = processingPath.get(processingPath.size() - 1);
            if (pathNode.childIndex == pathNode.records.size()) {
                processingPath.remove(processingPath.size() - 1);
                continue;
            }

            ToolBarGroupRecord groupRecord = pathNode.records.get(pathNode.childIndex);
            pathNode.childIndex++;

            if ((groupRecord.separationMode == SeparationMode.ABOVE || groupRecord.separationMode == SeparationMode.AROUND) && toolBarContinues) {
                targetToolBar.addSeparator();
                separatorQueued = false;
            }

            for (ToolBarContribution contribution : groupRecord.contributions) {
                if (separatorQueued) {
                    targetToolBar.addSeparator();
                    separatorQueued = false;
                }

                if (contribution instanceof ActionToolBarContribution) {
                    Action action = ((ActionToolBarContribution) contribution).getAction();
                    ActionUtils.ActionType actionType = (ActionUtils.ActionType) action.getValue(ActionUtils.ACTION_TYPE);
                    JComponent toolBarItem;
                    if (actionType != null) {
                        switch (actionType) {
                            case CHECK: {
                                JCheckBox newItem = new JCheckBox(action);
                                newItem.setFocusable(false);
                                newItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                                newItem.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                                toolBarItem = newItem;
                                break;
                            }
                            case RADIO: {
                                JRadioButton newItem = new JRadioButton(action);
                                newItem.setFocusable(false);
                                newItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                                newItem.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                                toolBarItem = newItem;
                                break;
                            }
                            default: {
                                toolBarItem = createDefaultToolBarItem(action);
                            }
                        }
                    } else {
                        toolBarItem = createDefaultToolBarItem(action);
                    }

                    targetToolBar.add(toolBarItem);
                }

                toolBarContinues = true;
            }

            if (groupRecord.separationMode == SeparationMode.AROUND || groupRecord.separationMode == SeparationMode.BELOW) {
                separatorQueued = true;
            }

            if (!groupRecord.subGroups.isEmpty()) {
                processingPath.add(new ToolBarGroupRecordPathNode(groupRecord.subGroups));
            }
        }
    }

    private JComponent createDefaultToolBarItem(Action action) {
        JButton newItem = new JButton(action);
        newItem.setFocusable(false);
        newItem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        newItem.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        return newItem;
    }

    public void registerToolBar(String toolBarId, String pluginId) {
        if (toolBarId == null) {
            throw new NullPointerException("Tool Bar Id cannot be null");
        }
        if (pluginId == null) {
            throw new NullPointerException("Plugin Id cannot be null");
        }

        ToolBarDefinition toolBar = toolBars.get(toolBarId);
        if (toolBar != null) {
            throw new IllegalStateException("Tool bar with ID " + toolBarId + " already exists.");
        }

        ToolBarDefinition toolBarDefinition = new ToolBarDefinition(pluginId);
        toolBars.put(toolBarId, toolBarDefinition);
    }

    public void registerToolBarGroup(String toolBarId, ToolBarGroup toolBarGroup) {
        List<ToolBarGroup> groups = toolBarGroups.get(toolBarId);
        if (groups == null) {
            groups = new LinkedList<>();
            toolBarGroups.put(toolBarId, groups);
        }
        groups.add(toolBarGroup);
    }

    public void registerToolBarItem(String toolBarId, String pluginId, Action action, ToolBarPosition position) {
        ToolBarDefinition toolBarDef = toolBars.get(toolBarId);
        if (toolBarDef == null) {
            throw new IllegalStateException("Tool bar with Id " + toolBarId + " doesn't exist");
        }

        ActionToolBarContribution toolBarContribution = new ActionToolBarContribution(action, position);
        toolBarDef.getContributions().add(toolBarContribution);
    }

    private class ToolBarGroupRecord {

        String groupId;
        SeparationMode separationMode;
        List<ToolBarGroupRecord> subGroups = new LinkedList<>();
        List<ToolBarContribution> contributions = new LinkedList<>();

        public ToolBarGroupRecord(String groupId) {
            this.groupId = groupId;
        }

        public ToolBarGroupRecord(String groupId, SeparationMode separationMode) {
            this(groupId);
            this.separationMode = separationMode;
        }
    }

    private class ToolBarGroupRecordPathNode {

        List<ToolBarGroupRecord> records;
        int childIndex;

        public ToolBarGroupRecordPathNode(List<ToolBarGroupRecord> records) {
            this.records = records;
        }
    }
}
