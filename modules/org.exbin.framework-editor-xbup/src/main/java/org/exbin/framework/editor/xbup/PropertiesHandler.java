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
import org.exbin.framework.editor.xbup.dialog.BlockPropertiesDialog;
import org.exbin.framework.editor.xbup.dialog.DocPropertiesDialog;
import org.exbin.framework.editor.xbup.panel.XBDocumentPanel;
import org.exbin.framework.gui.editor.api.XBEditorProvider;
import org.exbin.framework.gui.frame.api.GuiFrameModuleApi;
import org.exbin.framework.gui.utils.ActionUtils;

/**
 * Properties handler.
 *
 * @version 0.2.0 2016/03/01
 * @author ExBin Project (http://exbin.org)
 */
public class PropertiesHandler {

    private final XBEditorProvider editorProvider;
    private final XBApplication application;
    private final ResourceBundle resourceBundle;

    private int metaMask;

    private Action propertiesAction;
    private Action itemPropertiesAction;
    private boolean devMode;

    public PropertiesHandler(XBApplication application, XBEditorProvider editorProvider) {
        this.application = application;
        this.editorProvider = editorProvider;
        resourceBundle = ActionUtils.getResourceBundleByClass(EditorXbupModule.class);
    }

    public void init() {
        metaMask = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        propertiesAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editorProvider instanceof XBDocumentPanel) {
                    XBDocumentPanel activePanel = (XBDocumentPanel) editorProvider;
                    GuiFrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(GuiFrameModuleApi.class);
                    DocPropertiesDialog dialog = new DocPropertiesDialog(frameModule.getFrame(), true);
                    dialog.setIconImage(application.getApplicationIcon());
                    dialog.setLocationRelativeTo(dialog.getParent());
                    dialog.runDialog(activePanel.getDoc(), activePanel.getFileName());
                }
            }
        };
        ActionUtils.setupAction(propertiesAction, resourceBundle, "propertiesAction");
        propertiesAction.putValue(ActionUtils.ACTION_DIALOG_MODE, true);

        itemPropertiesAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editorProvider instanceof XBDocumentPanel) {
                    XBDocumentPanel activePanel = (XBDocumentPanel) editorProvider;
                    GuiFrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(GuiFrameModuleApi.class);
                    BlockPropertiesDialog propertiesDialog = new BlockPropertiesDialog(frameModule.getFrame(), true);
                    propertiesDialog.setCatalog(activePanel.getCatalog());
                    propertiesDialog.setDevMode(devMode);
                    propertiesDialog.runDialog(activePanel.getSelectedItem());
                }
            }
        };
        ActionUtils.setupAction(itemPropertiesAction, resourceBundle, "itemPropertiesAction");
        itemPropertiesAction.putValue(ActionUtils.ACTION_DIALOG_MODE, true);
    }

    public Action getPropertiesAction() {
        return propertiesAction;
    }

    public Action getItemPropertiesAction() {
        return itemPropertiesAction;
    }

    void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }
}
