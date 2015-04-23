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
package org.xbup.lib.operation;

/**
 * Abstract class for operation using XBUP level 1 document.
 *
 * @version 0.1.25 2015/04/24
 * @author XBUP Project (http://xbup.org)
 */
public abstract class XBTOpDocCommand extends XBTDocCommand {

    private XBTDocOperation redoOperation;
    private XBTDocOperation undoOperation;

    public XBTDocOperation getRedoOperation() {
        return redoOperation;
    }

    public void setRedoOperation(XBTDocOperation redoOperation) {
        this.redoOperation = redoOperation;
    }

    public XBTDocOperation getUndoOperation() {
        return undoOperation;
    }

    public void setUndoOperation(XBTDocOperation undoOperation) {
        this.undoOperation = undoOperation;
    }

    /**
     * Returns currently active operation
     *
     * @return document operation
     */
    public XBTDocOperation getCurrentOperation() {
        return redoOperation != null ? redoOperation : undoOperation;
    }

    @Override
    public boolean canUndo() {
        return undoOperation != null;
    }

    @Override
    public void undo() throws Exception {
        undoOperation.execute();
    }

    @Override
    public void redo() throws Exception {
        redoOperation.execute();
    }
}
