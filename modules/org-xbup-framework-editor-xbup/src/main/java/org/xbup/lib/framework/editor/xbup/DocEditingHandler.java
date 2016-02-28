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
package org.xbup.lib.framework.editor.xbup;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.xbup.lib.framework.api.XBApplication;
import org.xbup.lib.framework.gui.editor.api.XBEditorProvider;
import org.xbup.lib.framework.gui.frame.api.GuiFrameModuleApi;
import org.xbup.lib.framework.gui.utils.ActionUtils;
import org.xbup.lib.framework.editor.text.dialog.FindTextDialog;
import org.xbup.lib.framework.editor.xbup.panel.XBDocumentPanel;

/**
 * Document editing handler.
 *
 * @version 0.2.0 2016/02/28
 * @author ExBin Project (http://exbin.org)
 */
public class DocEditingHandler {

    private final XBEditorProvider editorProvider;
    private final XBApplication application;
    private final ResourceBundle resourceBundle;

    private int metaMask;

    private FindTextDialog findDialog = null;

    private Action addItemAction;
    private Action modifyItemAction;

    public DocEditingHandler(XBApplication application, XBEditorProvider editorProvider) {
        this.application = application;
        this.editorProvider = editorProvider;
        resourceBundle = ActionUtils.getResourceBundleByClass(EditorXbupModule.class);
    }

    public void init() {
        metaMask = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        addItemAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((XBDocumentPanel) editorProvider).performAdd();
            }
        };
        ActionUtils.setupAction(addItemAction, resourceBundle, "addItemAction");
        addItemAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_PLUS, 0));
        addItemAction.putValue(ActionUtils.ACTION_DIALOG_MODE, true);

        modifyItemAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((XBDocumentPanel) editorProvider).performModify();
            }
        };
        ActionUtils.setupAction(modifyItemAction, resourceBundle, "modifyItemAction");
        modifyItemAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        modifyItemAction.putValue(ActionUtils.ACTION_DIALOG_MODE, true);
        modifyItemAction.setEnabled(false);
    }

    public Action getAddItemAction() {
        return addItemAction;
    }

    public Action getModifyItemAction() {
        return modifyItemAction;
    }

    public void initFindDialog() {
        if (findDialog == null) {
            GuiFrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(GuiFrameModuleApi.class);
            findDialog = new FindTextDialog(frameModule.getFrame(), true);
            findDialog.setIconImage(application.getApplicationIcon());
        }
    }

    void setAddEnabled(boolean addEnabled) {
        addItemAction.setEnabled(addEnabled);
    }

    void setEditEnabled(boolean editEnabled) {
        modifyItemAction.setEnabled(editEnabled);
    }
}
