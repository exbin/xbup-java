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
import javax.swing.JMenuItem;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.editor.xbup.panel.XBDocumentPanel;
import org.exbin.framework.gui.editor.api.XBEditorProvider;
import org.xbup.lib.framework.gui.utils.ActionUtils;

/**
 * Property panel control handler.
 *
 * @version 0.2.0 2016/02/11
 * @author ExBin Project (http://exbin.org)
 */
public class PropertyPanelHandler {

    private final XBEditorProvider editorProvider;
    private final XBApplication application;
    private final ResourceBundle resourceBundle;

    private int metaMask;

    private Action viewPropertyPanelAction;

    public PropertyPanelHandler(XBApplication application, XBEditorProvider editorProvider) {
        this.application = application;
        this.editorProvider = editorProvider;
        resourceBundle = ActionUtils.getResourceBundleByClass(EditorXbupModule.class);
    }

    public void init() {
        metaMask = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        viewPropertyPanelAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editorProvider instanceof XBDocumentPanel) {
                    ((XBDocumentPanel) editorProvider).setSplitMode(((JMenuItem) e.getSource()).isSelected());
                }
            }
        };
        ActionUtils.setupAction(viewPropertyPanelAction, resourceBundle, "viewPropertyPanelAction");
        viewPropertyPanelAction.putValue(ActionUtils.ACTION_TYPE, ActionUtils.ActionType.CHECK);
        viewPropertyPanelAction.putValue(Action.SELECTED_KEY, true);
    }

    public Action getViewPropertyPanelAction() {
        return viewPropertyPanelAction;
    }
}
