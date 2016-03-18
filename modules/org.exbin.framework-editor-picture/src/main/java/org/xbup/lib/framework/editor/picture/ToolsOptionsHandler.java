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
package org.xbup.lib.framework.editor.picture;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.exbin.framework.api.XBApplication;
import org.xbup.lib.framework.editor.picture.dialog.ToolColorDialog;
import org.xbup.lib.framework.editor.picture.panel.ImagePanel;
import org.xbup.lib.framework.editor.picture.panel.ToolColorPanelApi;
import org.exbin.framework.gui.editor.api.XBEditorProvider;
import org.exbin.framework.gui.frame.api.GuiFrameModuleApi;
import org.xbup.lib.framework.gui.utils.ActionUtils;

/**
 * Tools options action handler.
 *
 * @version 0.2.0 2016/02/06
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
        resourceBundle = ActionUtils.getResourceBundleByClass(EditorPictureModule.class);
    }

    public void init() {
        toolsSetColorAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuiFrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(GuiFrameModuleApi.class);
                ToolColorPanelApi toolColorPanelApi = new ToolColorPanelApi() {
                    @Override
                    public Color getToolColor() {
                        return ((ImagePanel) editorProvider).getToolColor();
                    }

                    @Override
                    public Color getSelectionColor() {
                        return ((ImagePanel) editorProvider).getSelectionColor();
                    }

                    @Override
                    public void setToolColor(Color color) {
                        ((ImagePanel) editorProvider).setToolColor(color);
                    }

                    @Override
                    public void setSelectionColor(Color color) {
                        ((ImagePanel) editorProvider).setSelectionColor(color);
                    }
                };
                ToolColorDialog dialog = new ToolColorDialog(frameModule.getFrame(), true, toolColorPanelApi);
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
