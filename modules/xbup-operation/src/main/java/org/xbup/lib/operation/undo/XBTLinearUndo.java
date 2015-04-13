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
import org.xbup.lib.parser_tree.XBTTreeDocument;
import org.xbup.lib.operation.XBTDocCommand;

/**
 * Linear undo command sequence storage.
 *
 * @version 0.1.25 2015/04/13
 * @author XBUP Project (http://xbup.org)
 */
public class XBTLinearUndo {

    private long undoOperationsMaximumCount;
    private long undoOperationsMaximumSize;
    private long usedSize;
    private long operationPosition;
    private long syncPointPosition;
    private final List<XBTDocCommand> commandList;
    private final XBTTreeDocument document;

    /**
     * Creates a new instance.
     *
     * @param document document
     */
    public XBTLinearUndo(XBTTreeDocument document) {
        this.document = document;
        undoOperationsMaximumCount = 1024;
        undoOperationsMaximumSize = 65535;
        commandList = new ArrayList<>();
        init();
    }

    /**
     * Add new step into revert list.
     *
     * @param command
     * @throws java.lang.Exception
     */
    public void performStep(XBTDocCommand command) throws Exception {
        command.setDocument(document);
        command.redo();
        // TODO: Check for undoOperationsMaximumCount & size
        while (commandList.size() > operationPosition) {
            commandList.remove((int) operationPosition);
        }
        commandList.add(command);
        operationPosition++;
    }

    /**
     * Perform single undo step.
     *
     * @throws java.lang.Exception
     */
    public void performUndo() throws Exception {
        operationPosition--;
        XBTDocCommand command = commandList.get((int) operationPosition);
        command.setDocument(document);
        command.undo();
    }

    /**
     * Perform single redo step.
     *
     * @throws java.lang.Exception
     */
    public void performRedo() throws Exception {
        XBTDocCommand command = commandList.get((int) operationPosition);
        command.setDocument(document);
        command.redo();
        operationPosition++;
    }

    /**
     *
     * @param count
     * @throws Exception
     */
    public void performUndo(int count) throws Exception {
        if (operationPosition < count) {
            throw new IllegalArgumentException("Unable to perform " + count + " undo steps");
        }
        while (count > 0) {
            performUndo();
            count--;
        }
    }

    public void performRedo(int count) throws Exception {
        if (commandList.size() - operationPosition < count) {
            throw new IllegalArgumentException("Unable to perform " + count + " redo steps");
        }
        while (count > 0) {
            performRedo();
            count--;
        }
    }

    public void clear() {
        init();
    }

    public boolean canUndo() {
        return operationPosition > 0;
    }

    public boolean canRedo() {
        return commandList.size() > operationPosition;
    }

    public long getMaxUndo() {
        return undoOperationsMaximumCount;
    }

    /**
     * Perform revert to sync point.
     */
    public void doSync() {
    }

    public void setMaxUndo(long maxUndo) {
        this.undoOperationsMaximumCount = maxUndo;
    }

    public long getMaxSize() {
        return undoOperationsMaximumSize;
    }

    public String getDesc(XBTDocCommand command) {
        command.setDocument(document);
        return command.getCaption();
    }

    public void setMaxSize(long maxSize) {
        this.undoOperationsMaximumSize = maxSize;
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
        this.syncPointPosition = operationPosition;
    }

    private void init() {
        usedSize = 0;
        operationPosition = 0;
        setSyncPoint(0);
        commandList.clear();
    }
}
