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
package org.exbin.framework.editor.xbup;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.editor.xbup.panel.XBDocumentPanel;
import org.exbin.framework.gui.editor.api.XBEditorProvider;
import org.exbin.framework.gui.utils.ActionUtils;

/**
 * View mode handler.
 *
 * @version 0.2.0 2016/02/11
 * @author ExBin Project (http://exbin.org)
 */
public class ViewModeHandler {

    public static String VIEW_MODE_RADIO_GROUP_ID = "viewModeRadioGroup";

    private final XBEditorProvider editorProvider;
    private final XBApplication application;
    private final ResourceBundle resourceBundle;

    private int metaMask;

    private Action viewTreeModeAction;
    private Action viewTextModeAction;
    private Action viewHexModeAction;

    private XBDocumentPanel.PanelMode viewMode = XBDocumentPanel.PanelMode.TREE;

    public ViewModeHandler(XBApplication application, XBEditorProvider editorProvider) {
        this.application = application;
        this.editorProvider = editorProvider;
        resourceBundle = ActionUtils.getResourceBundleByClass(EditorXbupModule.class);
    }

    public void init() {
        metaMask = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        viewTreeModeAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editorProvider instanceof XBDocumentPanel) {
                    setViewMode(XBDocumentPanel.PanelMode.TREE);
                }
            }
        };
        ActionUtils.setupAction(viewTreeModeAction, resourceBundle, "viewTreeModeAction");
        viewTreeModeAction.putValue(ActionUtils.ACTION_TYPE, ActionUtils.ActionType.RADIO);
        viewTreeModeAction.putValue(ActionUtils.ACTION_RADIO_GROUP, VIEW_MODE_RADIO_GROUP_ID);
        viewTreeModeAction.putValue(Action.SELECTED_KEY, viewMode == XBDocumentPanel.PanelMode.TREE);

        viewTextModeAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editorProvider instanceof XBDocumentPanel) {
                    setViewMode(XBDocumentPanel.PanelMode.TEXT);
                }
            }
        };
        ActionUtils.setupAction(viewTextModeAction, resourceBundle, "viewTextModeAction");
        viewTextModeAction.putValue(ActionUtils.ACTION_TYPE, ActionUtils.ActionType.RADIO);
        viewTextModeAction.putValue(ActionUtils.ACTION_RADIO_GROUP, VIEW_MODE_RADIO_GROUP_ID);
        viewTextModeAction.putValue(Action.SELECTED_KEY, viewMode == XBDocumentPanel.PanelMode.TEXT);

        viewHexModeAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editorProvider instanceof XBDocumentPanel) {
                    setViewMode(XBDocumentPanel.PanelMode.HEX);
                }
            }
        };
        ActionUtils.setupAction(viewHexModeAction, resourceBundle, "viewHexModeAction");
        viewHexModeAction.putValue(ActionUtils.ACTION_RADIO_GROUP, VIEW_MODE_RADIO_GROUP_ID);
        viewHexModeAction.putValue(ActionUtils.ACTION_TYPE, ActionUtils.ActionType.RADIO);
        viewHexModeAction.putValue(Action.SELECTED_KEY, viewMode == XBDocumentPanel.PanelMode.HEX);
    }

    public void setViewMode(XBDocumentPanel.PanelMode viewMode) {
        this.viewMode = viewMode;
        XBDocumentPanel activePanel = (XBDocumentPanel) editorProvider;
        activePanel.setMode(viewMode);
    }

    public XBDocumentPanel.PanelMode getViewMode() {
        return viewMode;
    }

    public Action getTreeModeAction() {
        return viewTreeModeAction;
    }

    public Action getTextModeAction() {
        return viewTextModeAction;
    }

    public Action getHexModeAction() {
        return viewHexModeAction;
    }
}
