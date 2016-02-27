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

import org.xbup.lib.core.block.XBTEditableDocument;

/**
 * Abstract class for operation using XBUP level 1 document.
 *
 * @version 0.2.0 2016/02/27
 * @author XBUP Project (http://xbup.org)
 */
public abstract class XBTOpDocCommand extends XBTDocCommand {

    private XBTDocOperation operation;
    private boolean isUndoMode = false;

    public XBTOpDocCommand(XBTEditableDocument document) {
        super(document);
    }

    public XBTDocOperation getOperation() {
        return operation;
    }

    public void setOperation(XBTDocOperation operation) {
        this.operation = operation;
    }

    /**
     * Returns currently active operation.
     *
     * @return document operation
     */
    public XBTDocOperation getCurrentOperation() {
        return operation;
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public void undo() throws Exception {
        if (isUndoMode) {
            operation = (XBTDocOperation) operation.executeWithUndo();
            isUndoMode = false;
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @Override
    public void redo() throws Exception {
        if (!isUndoMode) {
            operation = (XBTDocOperation) operation.executeWithUndo();
            isUndoMode = true;
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
