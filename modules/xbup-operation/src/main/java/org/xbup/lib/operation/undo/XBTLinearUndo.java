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
package org.xbup.lib.operation.undo;

import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.operation.XBTDocCommand;
import org.xbup.lib.parser_tree.XBTTreeDocument;

/**
 * Linear undo command sequence storage.
 *
 * @version 0.1.25 2015/04/25
 * @author XBUP Project (http://xbup.org)
 */
public class XBTLinearUndo {

    private long undoMaximumCount;
    private long undoMaximumSize;
    private long usedSize;
    private long commandPosition;
    private long syncPointPosition = -1;
    private final List<XBTDocCommand> commandList;
    private final XBTTreeDocument document;

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
    public void execute(XBTDocCommand command) throws Exception {
        command.setDocument(document);
        command.execute();
        // TODO Optimize for changed data only?
        document.processSpec();
        // TODO: Check for undoOperationsMaximumCount & size
        while (commandList.size() > commandPosition) {
            commandList.remove((int) commandPosition);
        }
        commandList.add(command);
        commandPosition++;
    }

    /**
     * Performs single undo step.
     *
     * @throws java.lang.Exception
     */
    public void performUndo() throws Exception {
        commandPosition--;
        XBTDocCommand command = commandList.get((int) commandPosition);
        command.setDocument(document);
        command.undo();
        document.processSpec();
    }

    /**
     * Performs single redo step.
     *
     * @throws java.lang.Exception
     */
    public void performRedo() throws Exception {
        XBTDocCommand command = commandList.get((int) commandPosition);
        command.setDocument(document);
        command.redo();
        document.processSpec();
        commandPosition++;
    }

    /**
     * Performs multiple undo step.
     *
     * @param count count of steps
     * @throws Exception
     */
    public void performUndo(int count) throws Exception {
        if (commandPosition < count) {
            throw new IllegalArgumentException("Unable to perform " + count + " undo steps");
        }
        while (count > 0) {
            performUndo();
            count--;
        }
    }

    /**
     * Performs multiple redo step.
     *
     * @param count count of steps
     * @throws Exception
     */
    public void performRedo(int count) throws Exception {
        if (commandList.size() - commandPosition < count) {
            throw new IllegalArgumentException("Unable to perform " + count + " redo steps");
        }
        while (count > 0) {
            XBTLinearUndo.this.performRedo();
            count--;
        }
    }

    public void clear() {
        init();
    }

    public boolean canUndo() {
        return commandPosition > 0;
    }

    public boolean canRedo() {
        return commandList.size() > commandPosition;
    }

    public long getMaximumUndo() {
        return undoMaximumCount;
    }

    public long getCommandPosition() {
        return commandPosition;
    }

    /**
     * Performs revert to sync point.
     *
     * @throws java.lang.Exception
     */
    public void doSync() throws Exception {
        setCommandPosition(syncPointPosition);
    }

    public void setUndoMaxCount(long maxUndo) {
        this.undoMaximumCount = maxUndo;
    }

    public long getUndoMaximumSize() {
        return undoMaximumSize;
    }

    public void setUndoMaximumSize(long maxSize) {
        this.undoMaximumSize = maxSize;
    }

    public long getUsedSize() {
        return usedSize;
    }

    public long getSyncPoint() {
        return syncPointPosition;
    }

    public void setSyncPoint(long syncPoint) {
        this.syncPointPosition = syncPoint;
    }

    public void setSyncPoint() {
        this.syncPointPosition = commandPosition;
    }

    public List<XBTDocCommand> getCommandList() {
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
    public void setCommandPosition(long targetPosition) throws Exception {
        if (targetPosition < commandPosition) {
            performUndo((int) (commandPosition - targetPosition));
        } else if (targetPosition > commandPosition) {
            performRedo((int) (targetPosition - commandPosition));
        }
    }
}
