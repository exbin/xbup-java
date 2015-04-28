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
package org.xbup.tool.editor.module.xbdoc_editor.dialog;

import javax.swing.AbstractListModel;
import org.xbup.lib.operation.XBTDocCommand;
import org.xbup.lib.operation.undo.XBTLinearUndo;

/**
 * List model for undo manager.
 *
 * @version 0.1.24 2015/04/24
 * @author XBUP Project (http://xbup.org)
 */
public class UndoManagerModel extends AbstractListModel<String> {

    private XBTLinearUndo undoHandler = null;

    public UndoManagerModel() {
    }

    public XBTLinearUndo getUndoHandler() {
        return undoHandler;
    }

    public void setUndoHandler(XBTLinearUndo undoHandler) {
        if (this.undoHandler != null) {
            fireIntervalRemoved(this, 0, this.undoHandler.getCommandList().size());
        }

        this.undoHandler = undoHandler;
        fireIntervalAdded(this, 0, undoHandler.getCommandList().size());
    }

    @Override
    public int getSize() {
        return undoHandler == null ? 0 : undoHandler.getCommandList().size() + 1;
    }

    @Override
    public String getElementAt(int index) {
        return undoHandler == null ? null : (index == 0 ? "Initial" : undoHandler.getCommandList().get(index - 1).getCaption())
                + (undoHandler.getCommandPosition() == index ? " (current)" : "")
                + (undoHandler.getSyncPoint() == index ? " (saved)" : "");
    }

    public XBTDocCommand getItem(int index) {
        return undoHandler == null || index == 0 ? null : undoHandler.getCommandList().get(index - 1);
    }

    public int getCurrentPosition() {
        return (int) undoHandler.getCommandPosition();
    }
}