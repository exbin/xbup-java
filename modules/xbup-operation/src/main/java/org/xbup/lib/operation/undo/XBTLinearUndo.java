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
package org.xbup.lib.operation.undo;

import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.operation.Command;
import org.xbup.lib.parser_tree.XBTTreeDocument;

/**
 * Linear undo command sequence storage.
 *
 * @version 0.2.0 2016/01/24
 * @author ExBin Project (http://exbin.org)
 */
public class XBTLinearUndo implements XBUndoHandler {

    private long undoMaximumCount;
    private long undoMaximumSize;
    private long usedSize;
    private long commandPosition;
    private long syncPointPosition = -1;
    private final List<Command> commandList;
    private final XBTTreeDocument document;
    private final List<UndoUpdateListener> listeners = new ArrayList<>();

    /**
     * Creates a new instance.
     *
     * @param document document
     */
    public XBTLinearUndo(XBTTreeDocument document) {
        this.document = document;
        undoMaximumCount = 1024;
        undoMaximumSize = 65535;
        commandList = new ArrayList<>();
        init();
    }

    /**
     * Adds new step into revert list.
     *
     * @param command
     * @throws java.lang.Exception
     */
    @Override
    public void execute(Command command) throws Exception {
        command.execute();

        if (document != null) {
            // TODO Optimize for changed data only?
            document.processSpec();
        }

        // TODO: Check for undoOperationsMaximumCount & size
        while (commandList.size() > commandPosition) {
            commandList.remove((int) commandPosition);
        }
        commandList.add(command);
        commandPosition++;

        undoUpdated();
    }

    /**
     * Performs single undo step.
     *
     * @throws java.lang.Exception
     */
    @Override
    public void performUndo() throws Exception {
        performUndoInt();
        undoUpdated();
    }

    private void performUndoInt() throws Exception {
        commandPosition--;
        Command command = commandList.get((int) commandPosition);
        command.undo();
        if (document != null) {
            document.processSpec();
        }
    }

    /**
     * Performs single redo step.
     *
     * @throws java.lang.Exception
     */
    @Override
    public void performRedo() throws Exception {
        performRedoInt();
        undoUpdated();
    }

    private void performRedoInt() throws Exception {
        Command command = commandList.get((int) commandPosition);
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
     * @throws Exception
     */
    @Override
    public void performUndo(int count) throws Exception {
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
     * @throws Exception
     */
    @Override
    public void performRedo(int count) throws Exception {
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
    public long getMaximumUndo() {
        return undoMaximumCount;
    }

    @Override
    public long getCommandPosition() {
        return commandPosition;
    }

    /**
     * Performs revert to sync point.
     *
     * @throws java.lang.Exception
     */
    @Override
    public void doSync() throws Exception {
        setCommandPosition(syncPointPosition);
    }

    public void setUndoMaxCount(long maxUndo) {
        this.undoMaximumCount = maxUndo;
    }

    @Override
    public long getUndoMaximumSize() {
        return undoMaximumSize;
    }

    public void setUndoMaximumSize(long maxSize) {
        this.undoMaximumSize = maxSize;
    }

    @Override
    public long getUsedSize() {
        return usedSize;
    }

    @Override
    public long getSyncPoint() {
        return syncPointPosition;
    }

    @Override
    public void setSyncPoint(long syncPoint) {
        this.syncPointPosition = syncPoint;
    }

    @Override
    public void setSyncPoint() {
        this.syncPointPosition = commandPosition;
    }

    @Override
    public List<Command> getCommandList() {
        return commandList;
    }

    private void init() {
        usedSize = 0;
        commandPosition = 0;
        setSyncPoint(0);
        commandList.clear();
    }

    /**
     * Performs undo or redo operation to reach given position.
     *
     * @param targetPosition desired position
     * @throws java.lang.Exception
     */
    @Override
    public void setCommandPosition(long targetPosition) throws Exception {
        if (targetPosition < commandPosition) {
            performUndo((int) (commandPosition - targetPosition));
        } else if (targetPosition > commandPosition) {
            performRedo((int) (targetPosition - commandPosition));
        }
    }

    private void undoUpdated() {
        for (UndoUpdateListener listener : listeners) {
            listener.undoChanged();
        }
    }

    @Override
    public void addUndoUpdateListener(UndoUpdateListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeUndoUpdateListener(UndoUpdateListener listener) {
        listeners.remove(listener);
    }
}
