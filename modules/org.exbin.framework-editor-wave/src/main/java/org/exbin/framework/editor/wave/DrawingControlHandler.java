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
import org.xbup.lib.audio.swing.XBWavePanel;
import org.exbin.framework.editor.wave.panel.AudioPanel;
import org.exbin.framework.gui.editor.api.XBEditorProvider;
import org.xbup.lib.framework.gui.utils.ActionUtils;

/**
 * Drawing mode control handler.
 *
 * @version 0.2.0 2016/01/23
 * @author ExBin Project (http://exbin.org)
 */
public class DrawingControlHandler {

    public static String DRAWING_RADIO_GROUP_ID = "drawingRadioGroup";

    private final XBEditorProvider editorProvider;
    private final XBApplication application;
    private final ResourceBundle resourceBundle;

    private int metaMask;

    private Action dotsModeAction;
    private Action lineModeAction;
    private Action integralModeAction;

    private XBWavePanel.DrawMode drawMode = XBWavePanel.DrawMode.DOTS_MODE;

    public DrawingControlHandler(XBApplication application, XBEditorProvider editorProvider) {
        this.application = application;
        this.editorProvider = editorProvider;
        resourceBundle = ActionUtils.getResourceBundleByClass(EditorWaveModule.class);
    }

    public void init() {
        metaMask = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        dotsModeAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editorProvider instanceof AudioPanel) {
                    setDrawMode(XBWavePanel.DrawMode.DOTS_MODE);
                }
            }
        };
        ActionUtils.setupAction(dotsModeAction, resourceBundle, "dotsModeAction");
        dotsModeAction.putValue(ActionUtils.ACTION_TYPE, ActionUtils.ActionType.RADIO);
        dotsModeAction.putValue(ActionUtils.ACTION_RADIO_GROUP, DRAWING_RADIO_GROUP_ID);
        dotsModeAction.putValue(Action.SELECTED_KEY, drawMode == XBWavePanel.DrawMode.DOTS_MODE);

        lineModeAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editorProvider instanceof AudioPanel) {
                    setDrawMode(XBWavePanel.DrawMode.LINE_MODE);
                }
            }
        };
        ActionUtils.setupAction(lineModeAction, resourceBundle, "lineModeAction");
        lineModeAction.putValue(ActionUtils.ACTION_TYPE, ActionUtils.ActionType.RADIO);
        lineModeAction.putValue(ActionUtils.ACTION_RADIO_GROUP, DRAWING_RADIO_GROUP_ID);
        lineModeAction.putValue(Action.SELECTED_KEY, drawMode == XBWavePanel.DrawMode.LINE_MODE);

        integralModeAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editorProvider instanceof AudioPanel) {
                    setDrawMode(XBWavePanel.DrawMode.INTEGRAL_MODE);
                }
            }
        };
        ActionUtils.setupAction(integralModeAction, resourceBundle, "integralModeAction");
        integralModeAction.putValue(ActionUtils.ACTION_RADIO_GROUP, DRAWING_RADIO_GROUP_ID);
        integralModeAction.putValue(ActionUtils.ACTION_TYPE, ActionUtils.ActionType.RADIO);
        integralModeAction.putValue(Action.SELECTED_KEY, drawMode == XBWavePanel.DrawMode.INTEGRAL_MODE);
    }

    public void setDrawMode(XBWavePanel.DrawMode mode) {
        AudioPanel activePanel = (AudioPanel) editorProvider;
        activePanel.setDrawMode(mode);
    }

    public Action getDotsModeAction() {
        return dotsModeAction;
    }

    public Action getLineModeAction() {
        return lineModeAction;
    }

    public Action getIntegralModeAction() {
        return integralModeAction;
    }
}
