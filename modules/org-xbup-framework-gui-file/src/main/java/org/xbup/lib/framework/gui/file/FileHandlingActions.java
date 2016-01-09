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
package org.xbup.lib.framework.gui.file;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import org.xbup.lib.framework.gui.file.api.FileHandlingActionsApi;

/**
 * File handling operations.
 *
 * @version 0.2.0 2016/01/09
 * @author XBUP Project (http://xbup.org)
 */
public class FileHandlingActions implements FileHandlingActionsApi {

    private ResourceBundle resourceBundle;
    private int metaMask = java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

    private Action newFileAction;
    private Action openFileAction;
    private Action saveFileAction;
    private Action saveAsFileAction;

    private JFileChooser openFC, saveFC;

    public FileHandlingActions() {
        resourceBundle = java.util.ResourceBundle.getBundle("org/xbup/lib/framework/gui/file/resources/GuiFileModule");
    }

    public void init() {
        // Create File Choosers
        openFC = new JFileChooser();
        openFC.setAcceptAllFileFilterUsed(false);
        saveFC = new JFileChooser();
        saveFC.setAcceptAllFileFilterUsed(false);

        newFileAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                actionFileNew();
            }
        };
        newFileAction.putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/org/xbup/lib/framework/gui/file/resources/icons/tango-icon-theme/16x16/actions/document-new.png")));
        newFileAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, metaMask));
        newFileAction.putValue(Action.NAME, resourceBundle.getString("actionFileNew.Action.text"));
        newFileAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("actionFileNew.Action.shortDescription"));

        openFileAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                actionFileOpen();
            }
        };
        openFileAction.putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/org/xbup/lib/framework/gui/file/resources/icons/tango-icon-theme/16x16/actions/document-open.png")));
        openFileAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, metaMask));
        openFileAction.putValue(Action.NAME, resourceBundle.getString("actionFileOpen.Action.text"));
        openFileAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("actionFileOpen.Action.shortDescription"));

        saveFileAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                actionFileSave();
            }
        };
        saveFileAction.putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/org/xbup/lib/framework/gui/file/resources/icons/tango-icon-theme/16x16/actions/document-save.png")));
        saveFileAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, metaMask));
        saveFileAction.putValue(Action.NAME, resourceBundle.getString("actionFileSave.Action.text"));
        saveFileAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("actionFileSave.Action.shortDescription"));

        saveAsFileAction = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                actionFileSaveAs();
            }
        };
        saveAsFileAction.putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/org/xbup/lib/framework/gui/file/resources/icons/tango-icon-theme/16x16/actions/document-save-as.png")));
        saveAsFileAction.putValue(Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, metaMask));
        saveAsFileAction.putValue(Action.NAME, resourceBundle.getString("actionFileSaveAs.Action.text"));
        saveAsFileAction.putValue(Action.SHORT_DESCRIPTION, resourceBundle.getString("actionFileSaveAs.Action.shortDescription"));
    }

    @Override
    public Action getNewFileAction() {
        return newFileAction;
    }

    @Override
    public Action getOpenFileAction() {
        return openFileAction;
    }

    @Override
    public Action getSaveFileAction() {
        return saveFileAction;
    }

    @Override
    public Action getSaveAsFileAction() {
        return saveAsFileAction;
    }

    public void actionFileNew() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void actionFileOpen() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void actionFileSave() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void actionFileSaveAs() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
