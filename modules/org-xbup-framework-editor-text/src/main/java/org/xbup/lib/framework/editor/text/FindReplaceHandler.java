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

/**
 * Find/replace handler.
 *
 * @version 0.2.0 2016/01/13
 * @author XBUP Project (http://xbup.org)
 */
public class FindReplaceHandler {

    private int metaMask;
    private ResourceBundle resourceBundle;

    private Action editFindAction;
    private Action editFindAgainAction;
    private Action editReplaceAction;

    public FindReplaceHandler() {
        resourceBundle = ActionUtils.getResourceBundleByClass(EditorTextModule.class);
    }

    public void init() {
        editFindAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        ActionUtils.setupAction(editFindAction, resourceBundle, "editFindAction");
        editFindAction.putValue(ActionUtils.ACTION_DIALOG_MODE, true);

        editFindAgainAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        ActionUtils.setupAction(editFindAgainAction, resourceBundle, "editFindAgainAction");

        editReplaceAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        ActionUtils.setupAction(editReplaceAction, resourceBundle, "editReplaceAction");
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
}
