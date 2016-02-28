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

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.xbup.lib.framework.editor.picture.panel.ImagePanel;
import org.xbup.lib.framework.api.XBApplication;
import org.xbup.lib.framework.gui.editor.api.XBEditorProvider;
import org.xbup.lib.framework.gui.utils.ActionUtils;

/**
 * Print handler.
 *
 * @version 0.2.0 2016/02/06
 * @author ExBin Project (http://exbin.org)
 */
public class PrintHandler {

    private final XBEditorProvider editorProvider;
    private final XBApplication application;
    private final ResourceBundle resourceBundle;

    private int metaMask;

    private Action printAction;

    public PrintHandler(XBApplication application, XBEditorProvider editorProvider) {
        this.application = application;
        this.editorProvider = editorProvider;
        resourceBundle = ActionUtils.getResourceBundleByClass(EditorPictureModule.class);
    }

    public void init() {
        metaMask = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        printAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editorProvider instanceof ImagePanel) {
                    ImagePanel activePanel = (ImagePanel) editorProvider;
                    activePanel.printFile();
                }
            }
        };
        ActionUtils.setupAction(printAction, resourceBundle, "printAction");
        printAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, metaMask));
        printAction.putValue(ActionUtils.ACTION_DIALOG_MODE, true);
    }

    public Action getPrintAction() {
        return printAction;
    }
}
