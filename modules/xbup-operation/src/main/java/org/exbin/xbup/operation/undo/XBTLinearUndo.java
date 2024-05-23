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
package org.exbin.xbup.operation.undo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.operation.Command;
import org.exbin.xbup.parser_tree.XBTTreeDocument;

/**
 * Linear undo command sequence storage.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBTLinearUndo implements UndoRedo {

    private int commandPosition;
    private int syncPosition = -1;
    private final List<Command> commandList;
    private final XBTTreeDocument document;
    private final List<UndoRedoChangeListener> listeners = new ArrayList<>();

    /**
     * Creates a new instance.
     *
     * @param document document
     */
    public XBTLinearUndo(XBTTreeDocument document) {
        this.document = document;
        commandList = new ArrayList<>();
        init();
    }

    /**
     * Adds new step into revert list.
     *
     * @param command command
     */
    @Override
    public void execute(Command command) {
        command.execute();

        if (document != null) {
            // TODO Optimize for changed data only?
            document.processSpec();
        }

        addCommand(command);
    }

    public void addCommand(Command command) {
        // TODO: Check for undoOperationsMaximumCount & size
        while (commandList.size() > commandPosition) {
            commandList.remove((int) commandPosition);
        }
        commandList.add(command);
        commandPosition++;

        undoUpdated();
        for (UndoRedoChangeListener listener : listeners) {
            listener.undoChanged();
        }
    }

    /**
     * Performs single undo step.
     */
    @Override
    public void performUndo() {
        performUndoInt();
        undoUpdated();
    }

    private void performUndoInt() {
        commandPosition--;
        UndoableCommand command = (UndoableCommand) commandList.get((int) commandPosition);
        command.undo();
        if (document != null) {
            document.processSpec();
        }
    }

    /**
     * Performs single redo step.
     */
    @Override
    public void performRedo() {
        performRedoInt();
        undoUpdated();
    }

    private void performRedoInt() {
        UndoableCommand command = (UndoableCommand) commandList.get((int) commandPosition);
        command.redo();
        commandPosition++;
        if (document != null) {
            document.processSpec();
        }
    }

    /**
     * Performs multiple undo step.
     *
     * @param count count of steps
     */
    @Override
    public void performUndo(int count) {
        if (commandPosition < count) {
            throw new IllegalArgumentException("Unable to perform " + count + " undo steps");
        }
        while (count > 0) {
            performUndoInt();
            count--;
        }
        document.processSpec();
        undoUpdated();
    }

    /**
     * Performs multiple redo step.
     *
     * @param count count of steps
     */
    @Override
    public void performRedo(int count) {
        if (commandList.size() - commandPosition < count) {
            throw new IllegalArgumentException("Unable to perform " + count + " redo steps");
        }
        while (count > 0) {
            performRedoInt();
            count--;
        }
        document.processSpec();
        undoUpdated();
    }

    @Override
    public void clear() {
        init();
    }

    @Override
    public boolean canUndo() {
        return commandPosition > 0;
    }

    @Override
    public boolean canRedo() {
        return commandList.size() > commandPosition;
    }

    @Override
    public int getCommandPosition() {
        return commandPosition;
    }

    /**
     * Performs revert to sync point.
     */
    @Override
    public void performSync() {
        setCommandPosition(syncPosition);
    }

    @Override
    public int getSyncPosition() {
        return syncPosition;
    }

    @Override
    public void setSyncPosition(int syncPosition) {
        this.syncPosition = syncPosition;
    }

    @Override
    public void setSyncPosition() {
        this.syncPosition = commandPosition;
    }

    @Nonnull
    @Override
    public List<Command> getCommandList() {
        return commandList;
    }

    @Nonnull
    @Override
    public Optional<Command> getTopUndoCommand() {
        return commandPosition > 0 ? Optional.of(commandList.get(commandPosition - 1)) : Optional.empty();
    }

    @Override
    public int getCommandsCount() {
        return commandList.size();
    }

    @Override
    public boolean isModified() {
        return syncPosition != commandPosition;
    }

    private void init() {
        commandPosition = 0;
        setSyncPosition(0);
        commandList.clear();
    }

    /**
     * Performs undo or redo operation to reach given position.
     *
     * @param targetPosition desired position
     */
    public void setCommandPosition(int targetPosition) {
        if (targetPosition < commandPosition) {
            performUndo((int) (commandPosition - targetPosition));
        } else if (targetPosition > commandPosition) {
            performRedo((int) (targetPosition - commandPosition));
        }
    }

    private void undoUpdated() {
        for (UndoRedoChangeListener listener : listeners) {
            listener.undoChanged();
        }
    }

    @Override
    public void addChangeListener(UndoRedoChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeChangeListener(UndoRedoChangeListener listener) {
        listeners.remove(listener);
    }
}
