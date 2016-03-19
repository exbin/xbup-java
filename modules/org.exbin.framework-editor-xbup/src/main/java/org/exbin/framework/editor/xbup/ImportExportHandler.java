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
import org.exbin.framework.editor.xbup.panel.XBDocumentPanel;
import org.exbin.framework.gui.editor.api.XBEditorProvider;
import org.exbin.framework.gui.utils.ActionUtils;

/**
 * Import / export handler.
 *
 * @version 0.2.0 2016/02/13
 * @author ExBin Project (http://exbin.org)
 */
public class ImportExportHandler {

    private final XBEditorProvider editorProvider;
    private final XBApplication application;
    private final ResourceBundle resourceBundle;

    private int metaMask;

    private Action importItemAction;
    private Action exportItemAction;

    public ImportExportHandler(XBApplication application, XBEditorProvider editorProvider) {
        this.application = application;
        this.editorProvider = editorProvider;
        resourceBundle = ActionUtils.getResourceBundleByClass(EditorXbupModule.class);
    }

    public void init() {
        metaMask = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        importItemAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editorProvider instanceof XBDocumentPanel) {
                    XBDocumentPanel activePanel = (XBDocumentPanel) editorProvider;
                }
            }
        };
        ActionUtils.setupAction(importItemAction, resourceBundle, "importItemAction");
        importItemAction.putValue(ActionUtils.ACTION_DIALOG_MODE, true);

        exportItemAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editorProvider instanceof XBDocumentPanel) {
                    XBDocumentPanel activePanel = (XBDocumentPanel) editorProvider;
                }
            }
        };
        ActionUtils.setupAction(exportItemAction, resourceBundle, "exportItemAction");
        exportItemAction.putValue(ActionUtils.ACTION_DIALOG_MODE, true);
    }

    public Action getImportItemAction() {
        return importItemAction;
    }

    public Action getExportItemAction() {
        return exportItemAction;
    }
}
