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
package org.xbup.lib.framework.editor.xbup;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.xbup.lib.framework.gui.api.XBApplication;
import org.xbup.lib.framework.gui.editor.api.XBEditorProvider;
import org.xbup.lib.framework.gui.frame.api.GuiFrameModuleApi;
import org.xbup.lib.framework.gui.utils.ActionUtils;
import org.xbup.lib.framework.editor.xbup.dialog.CatalogEditorDialog;
import org.xbup.lib.framework.editor.xbup.panel.XBDocumentPanel;
import org.xbup.lib.framework.gui.utils.WindowUtils;

/**
 * Catalog browser handler.
 *
 * @version 0.2.0 2016/02/10
 * @author XBUP Project (http://xbup.org)
 */
public class CatalogBrowserHandler {

    private final XBEditorProvider editorProvider;
    private final XBApplication application;
    private ResourceBundle resourceBundle;

    private int metaMask;

    private CatalogEditorDialog catalogEditorDialog = null;

    private Action catalogBrowserAction;

    public CatalogBrowserHandler(XBApplication application, XBEditorProvider editorProvider) {
        this.application = application;
        this.editorProvider = editorProvider;
        resourceBundle = ActionUtils.getResourceBundleByClass(EditorXbupModule.class);
    }

    public void init() {
        metaMask = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        catalogBrowserAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                catalogEditorDialog = new CatalogEditorDialog(WindowUtils.getFrame((XBDocumentPanel) editorProvider), true);
//                catalogEditorDialog.setMenuManagement(menuManagement);
//                catalogEditorDialog.setMainFrameManagement(mainFrameManagement);
//                catalogEditorDialog.setCatalog(catalog);
                catalogEditorDialog.setVisible(true);

                catalogEditorDialog = null;
            }
        };
        ActionUtils.setupAction(catalogBrowserAction, resourceBundle, "catalogBrowserAction");
        catalogBrowserAction.putValue(ActionUtils.ACTION_DIALOG_MODE, true);
    }

    public Action getCatalogBrowserAction() {
        return catalogBrowserAction;
    }

    public void initCatalogBrowserDialog() {
        if (catalogEditorDialog == null) {
            GuiFrameModuleApi frameModule = application.getModuleRepository().getModuleByInterface(GuiFrameModuleApi.class);
            catalogEditorDialog = new CatalogEditorDialog(frameModule.getFrame(), true);
            catalogEditorDialog.setIconImage(application.getApplicationIcon());
        }
    }
}
