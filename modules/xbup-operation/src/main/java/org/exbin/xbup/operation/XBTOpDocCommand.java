/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public abstract class XBTOpDocCommand extends XBTDocCommand {

    @Nullable
    private XBTDocOperation operation;
    private CommandPhase phase = CommandPhase.CREATED;

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
    public void undo() {
        if (phase == CommandPhase.REVERTED) {
            throw new IllegalStateException();
        }

        XBTDocOperation redoOperation = (XBTDocOperation) operation.executeWithUndo();
        operation = redoOperation;
        phase = CommandPhase.REVERTED;
    }

    @Override
    public void redo() {
        if (phase == CommandPhase.EXECUTED) {
            throw new IllegalStateException();
        }

        XBTDocOperation undoOperation = (XBTDocOperation) operation.executeWithUndo();
        operation = undoOperation;
        phase = CommandPhase.EXECUTED;
    }
}
