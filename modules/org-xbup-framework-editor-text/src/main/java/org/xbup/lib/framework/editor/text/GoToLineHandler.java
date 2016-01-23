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
package org.xbup.lib.framework.editor.text;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import org.xbup.lib.framework.editor.text.dialog.GotoDialog;
import org.xbup.lib.framework.gui.api.XBApplication;
import org.xbup.lib.framework.gui.editor.api.XBEditorProvider;
import org.xbup.lib.framework.gui.utils.ActionUtils;
import org.xbup.lib.framework.editor.text.panel.TextPanel;
import org.xbup.lib.framework.gui.frame.api.GuiFrameModuleApi;

/**
 * Go to line handler.
 *
 * @version 0.2.0 2016/01/23
 * @author XBUP Project (http://xbup.org)
 */
public class GoToLineHandler {

    private final XBEditorProvider editorProvider;
    private final XBApplication application;
    private final ResourceBundle resourceBundle;

    private int metaMask;

    private GotoDialog gotoDialog = null;

    private Action goToLineAction;

    public GoToLineHandler(XBApplication application, XBEditorProvider editorProvider) {
        this.application = application;
        this.editorProvider = editorProvider;
        resourceBundle = ActionUtils.getResourceBundleByClass(EditorTextModule.class);
    }

    public void init() {
        metaMask = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        goToLineAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editorProvider instanceof TextPanel) {
                    TextPanel activePanel = (TextPanel) editorProvider;
                    initGotoDialog();
                    gotoDialog.setMaxLine(activePanel.getLineCount());
                    gotoDialog.setCharPos(1);
                    gotoDialog.setLocationRelativeTo(gotoDialog.getParent());
                    gotoDialog.setVisible(true);
                    if (gotoDialog.getDialogOption() == JOptionPane.OK_OPTION) {
                        activePanel.gotoLine(gotoDialog.getLine());
                        activePanel.gotoRelative(gotoDialog.getCharPos());
                    }
                }
            }
        };
        ActionUtils.setupAction(goToLineAction, resourceBundle, "goToLineAction");
        goToLineAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, metaMask));
        goToLineAction.putValue(ActionUtils.ACTION_DIALOG_MODE, true);
    }

    public Action getGoToLineAction() {
        return goToLineAction;
    }

    private void initGotoDialog() {
        if (gotoDialog == null) {
            GuiFrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(GuiFrameModuleApi.class);
            gotoDialog = new GotoDialog(frameModule.getFrame(), true);
            gotoDialog.setIconImage(application.getApplicationIcon());
        }
    }
}
