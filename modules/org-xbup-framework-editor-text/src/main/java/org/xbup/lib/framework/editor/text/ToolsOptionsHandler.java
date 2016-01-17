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
import org.xbup.lib.framework.gui.utils.ActionUtils;
import org.xbup.tool.editor.module.text_editor.dialog.FontDialog;

/**
 * Tools options action handler.
 *
 * @version 0.2.0 2016/01/16
 * @author XBUP Project (http://xbup.org)
 */
public class ToolsOptionsHandler {

    private int metaMask;
    private ResourceBundle resourceBundle;

    private Action toolsSetFontAction;
    private Action toolsSetColorAction;
    // private Action editReplaceAction;

    public ToolsOptionsHandler() {
        resourceBundle = ActionUtils.getResourceBundleByClass(EditorTextModule.class);
    }

    public void init() {
        toolsSetFontAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        ActionUtils.setupAction(toolsSetFontAction, resourceBundle, "toolsSetFontAction");
        toolsSetFontAction.putValue(ActionUtils.ACTION_DIALOG_MODE, true);

        toolsSetColorAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                FontDialog dlg = new FontDialog(getFrame(), true);
//                dlg.setIconImage(mainFrameManagement.getFrameIcon());
//                dlg.setLocationRelativeTo(dlg.getParent());
//                getActivePanel().showFontDialog(dlg);
            }
        };
        ActionUtils.setupAction(toolsSetColorAction, resourceBundle, "toolsSetColorAction");
        toolsSetColorAction.putValue(ActionUtils.ACTION_DIALOG_MODE, true);
    }

    public Action getToolsSetFontAction() {
        return toolsSetFontAction;
    }

    public Action getToolsSetColorAction() {
        return toolsSetColorAction;
    }
}
