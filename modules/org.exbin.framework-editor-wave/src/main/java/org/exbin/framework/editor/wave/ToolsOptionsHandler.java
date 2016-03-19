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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.exbin.framework.api.XBApplication;
import org.exbin.framework.editor.wave.dialog.WaveColorDialog;
import org.exbin.framework.editor.wave.panel.AudioPanel;
import org.exbin.framework.editor.wave.panel.WaveColorPanelApi;
import org.exbin.framework.gui.editor.api.XBEditorProvider;
import org.exbin.framework.gui.frame.api.GuiFrameModuleApi;
import org.exbin.framework.gui.utils.ActionUtils;

/**
 * Tools options action handler.
 *
 * @version 0.2.0 2016/01/23
 * @author ExBin Project (http://exbin.org)
 */
public class ToolsOptionsHandler {

    private int metaMask;
    private ResourceBundle resourceBundle;

    private Action toolsSetColorAction;

    private final XBEditorProvider editorProvider;
    private final XBApplication application;

    public ToolsOptionsHandler(XBApplication application, XBEditorProvider editorProvider) {
        this.application = application;
        this.editorProvider = editorProvider;
        resourceBundle = ActionUtils.getResourceBundleByClass(EditorWaveModule.class);
    }

    public void init() {
        toolsSetColorAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuiFrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(GuiFrameModuleApi.class);
                WaveColorPanelApi textColorPanelApi = new WaveColorPanelApi() {
                    @Override
                    public Color[] getCurrentWaveColors() {
                        return ((AudioPanel) editorProvider).getAudioPanelColors();
                    }

                    @Override
                    public Color[] getDefaultWaveColors() {
                        return ((AudioPanel) editorProvider).getDefaultColors();
                    }

                    @Override
                    public void setCurrentWaveColors(Color[] colors) {
                        ((AudioPanel) editorProvider).setAudioPanelColors(colors);
                    }
                };
                WaveColorDialog dialog = new WaveColorDialog(frameModule.getFrame(), true, textColorPanelApi);
                dialog.setIconImage(application.getApplicationIcon());
                dialog.setLocationRelativeTo(dialog.getParent());
                dialog.showDialog();
            }
        };
        ActionUtils.setupAction(toolsSetColorAction, resourceBundle, "toolsSetColorAction");
        toolsSetColorAction.putValue(ActionUtils.ACTION_DIALOG_MODE, true);
    }

    public Action getToolsSetColorAction() {
        return toolsSetColorAction;
    }
}
