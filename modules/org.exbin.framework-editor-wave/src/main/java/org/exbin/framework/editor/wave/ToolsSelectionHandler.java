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
package org.exbin.framework.editor.wave;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.exbin.framework.api.XBApplication;
import org.exbin.xbup.audio.swing.XBWavePanel;
import org.exbin.framework.editor.wave.panel.AudioPanel;
import org.exbin.framework.gui.editor.api.XBEditorProvider;
import org.exbin.framework.gui.utils.ActionUtils;

/**
 * Tools selection control handler.
 *
 * @version 0.2.0 2016/01/30
 * @author ExBin Project (http://exbin.org)
 */
public class ToolsSelectionHandler {

    public static String TOOLS_SELECTION_RADIO_GROUP_ID = "toolsSelectionRadioGroup";

    private final XBEditorProvider editorProvider;
    private final XBApplication application;
    private final ResourceBundle resourceBundle;

    private Action selectionToolAction;
    private Action pencilToolAction;

    private XBWavePanel.ToolMode toolMode = XBWavePanel.ToolMode.SELECTION;

    public ToolsSelectionHandler(XBApplication application, XBEditorProvider editorProvider) {
        this.application = application;
        this.editorProvider = editorProvider;
        resourceBundle = ActionUtils.getResourceBundleByClass(EditorWaveModule.class);
    }

    public void init() {

        selectionToolAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editorProvider instanceof AudioPanel) {
                    setToolMode(XBWavePanel.ToolMode.SELECTION);
                }
            }
        };
        ActionUtils.setupAction(selectionToolAction, resourceBundle, "selectionToolAction");
        selectionToolAction.putValue(ActionUtils.ACTION_TYPE, ActionUtils.ActionType.RADIO);
        selectionToolAction.putValue(ActionUtils.ACTION_RADIO_GROUP, TOOLS_SELECTION_RADIO_GROUP_ID);
        selectionToolAction.putValue(Action.SELECTED_KEY, toolMode == XBWavePanel.ToolMode.SELECTION);

        pencilToolAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editorProvider instanceof AudioPanel) {
                    setToolMode(XBWavePanel.ToolMode.PENCIL);
                }
            }
        };
        ActionUtils.setupAction(pencilToolAction, resourceBundle, "pencilToolAction");
        pencilToolAction.putValue(ActionUtils.ACTION_TYPE, ActionUtils.ActionType.RADIO);
        pencilToolAction.putValue(ActionUtils.ACTION_RADIO_GROUP, TOOLS_SELECTION_RADIO_GROUP_ID);
        pencilToolAction.putValue(Action.SELECTED_KEY, toolMode == XBWavePanel.ToolMode.PENCIL);
    }

    public void setToolMode(XBWavePanel.ToolMode mode) {
        AudioPanel activePanel = (AudioPanel) editorProvider;
        activePanel.setToolMode(mode);
    }

    public Action getSelectionToolAction() {
        return selectionToolAction;
    }

    public Action getPencilToolAction() {
        return pencilToolAction;
    }
}
