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
package org.exbin.xbup.operation;

import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBTEditableDocument;

/**
 * Abstract class for operation using XBUP level 1 document.
 *
 * @version 0.2.0 2016/02/27
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public abstract class XBTOpDocCommand extends XBTDocCommand {

    @Nullable
    private XBTDocOperation operation;
    private boolean isUndoMode = false;

    public XBTOpDocCommand(XBTEditableDocument document) {
        super(document);
    }

    @Nonnull
    public Optional<XBTDocOperation> getOperation() {
        return Optional.ofNullable(operation);
    }

    public void setOperation(@Nullable XBTDocOperation operation) {
        this.operation = operation;
    }

    /**
     * Returns currently active operation.
     *
     * @return document operation
     */
    @Nonnull
    public Optional<XBTDocOperation> getCurrentOperation() {
        return Optional.ofNullable(operation);
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public void undo() throws Exception {
        if (isUndoMode) {
            XBTDocOperation redoOperation = (XBTDocOperation) operation.executeWithUndo().orElse(null);
            if (document instanceof OperationListener) {
                ((OperationListener) document).notifyChange(new OperationEvent(operation));
            }

            operation = redoOperation;
            isUndoMode = false;
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @Override
    public void redo() throws Exception {
        if (!isUndoMode) {
            XBTDocOperation undoOperation = (XBTDocOperation) operation.executeWithUndo().orElse(null);
            if (document instanceof OperationListener) {
                ((OperationListener) document).notifyChange(new OperationEvent(operation));
            }

            operation = undoOperation;
            isUndoMode = true;
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
