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
import org.xbup.lib.framework.gui.api.XBApplication;
import org.xbup.lib.framework.gui.editor.api.XBEditorProvider;
import org.xbup.lib.framework.gui.frame.api.GuiFrameModuleApi;
import org.xbup.lib.framework.gui.utils.ActionUtils;
import org.xbup.lib.framework.editor.text.dialog.FindTextDialog;
import org.xbup.lib.framework.editor.text.panel.TextPanel;

/**
 * Find/replace handler.
 *
 * @version 0.2.0 2016/01/20
 * @author XBUP Project (http://xbup.org)
 */
public class FindReplaceHandler {

    private final XBEditorProvider editorProvider;
    private final XBApplication application;
    private ResourceBundle resourceBundle;

    private int metaMask;

    private FindTextDialog findDialog = null;

    private Action editFindAction;
    private Action editFindAgainAction;
    private Action editReplaceAction;

    public FindReplaceHandler(XBApplication application, XBEditorProvider editorProvider) {
        this.application = application;
        this.editorProvider = editorProvider;
        resourceBundle = ActionUtils.getResourceBundleByClass(EditorTextModule.class);
    }

    public void init() {
        metaMask = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        editFindAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initFindDialog();
                findDialog.setShallReplace(false);
                findDialog.setSelected();
                findDialog.setLocationRelativeTo(findDialog.getParent());
                findDialog.setVisible(true);
                if (findDialog.getDialogOption() == JOptionPane.OK_OPTION) {
                    if (editorProvider instanceof TextPanel) {
                        ((TextPanel) editorProvider).findText(findDialog);
                    }
                }
            }
        };
        ActionUtils.setupAction(editFindAction, resourceBundle, "editFindAction");
        editFindAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, metaMask));
        editFindAction.putValue(ActionUtils.ACTION_DIALOG_MODE, true);

        editFindAgainAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initFindDialog();
                findDialog.setShallReplace(false);
                if (editorProvider instanceof TextPanel) {
                    ((TextPanel) editorProvider).findText(findDialog);
                }
            }
        };
        ActionUtils.setupAction(editFindAgainAction, resourceBundle, "editFindAgainAction");
        editFindAgainAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));

        editReplaceAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initFindDialog();
                findDialog.setShallReplace(true);
                findDialog.setSelected();
                findDialog.setLocationRelativeTo(findDialog.getParent());
                findDialog.setVisible(true);
                if (findDialog.getDialogOption() == JOptionPane.OK_OPTION) {
                    if (editorProvider instanceof TextPanel) {
                        ((TextPanel) editorProvider).findText(findDialog);
                    }
                }
            }
        };
        ActionUtils.setupAction(editReplaceAction, resourceBundle, "editReplaceAction");
        editReplaceAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, metaMask));
        editReplaceAction.putValue(ActionUtils.ACTION_DIALOG_MODE, true);
    }

    public Action getEditFindAction() {
        return editFindAction;
    }

    public Action getEditFindAgainAction() {
        return editFindAgainAction;
    }

    public Action getEditReplaceAction() {
        return editReplaceAction;
    }

    public void initFindDialog() {
        if (findDialog == null) {
            GuiFrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(GuiFrameModuleApi.class);
            findDialog = new FindTextDialog(frameModule.getFrame(), true);
            findDialog.setIconImage(application.getApplicationIcon());
        }
    }
}
