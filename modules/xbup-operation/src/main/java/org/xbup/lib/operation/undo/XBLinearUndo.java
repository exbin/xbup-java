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
import org.xbup.lib.parser_tree.XBTree;
import org.xbup.lib.operation.XBDocOperation;

/**
 * Class for keeping undo command sequence.
 *
 * TODO: Incomplete
 *
 * @version 0.1.25 2015/04/13
 * @author ExBin Project (http://exbin.org)
 */
public class XBLinearUndo {

    private long maxUndo; // Max count of list of commands
    private long maxSize; // Max size of list of commands
    private long usedSize; // Currently used size
    private long position; // Current position in history qeue
    private long syncPoint; // Position of currently stored state
    private final List<XBDocOperation> operationList;
    private final XBTree tree;

    /**
     * Creates a new instance.
     *
     * @param tree
     */
    public XBLinearUndo(XBTree tree) {
        this.tree = tree;
        maxUndo = 1024;
        maxSize = 65535;
        operationList = new ArrayList<>();
        init();
    }

    public void performUndo() throws Exception {
        if (position == 0) {
            throw new Exception("");
        }
        XBDocOperation op = operationList.get((int) position);
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void performRedo() throws Exception {

    }

    public void performUndo(int count) throws Exception {
        if (position < count) {
            throw new IllegalArgumentException("Unable to perform " + count + " undo steps");
        }
        while (count > 0) {
            performUndo();
            count--;
        }
    }

    public void performRedo(int count) throws Exception {
        if (operationList.size() - position < count) {
            throw new IllegalArgumentException("Unable to perform " + count + " redo steps");
        }
        while (count > 0) {
            performUndo();
            count--;
        }
    }

    public void clear() {
        init();
    }

    private void init() {
        usedSize = 0;
        position = 0;
        setSyncPoint(0);
        operationList.clear();
    }

    public boolean canUndo() {
        return position > 0;
    }

    public boolean canRedo() {
        return operationList.size() > position;
    }

    public long getMaxUndo() {
        return maxUndo;
    }

    /**
     * Perform undo to sync point.
     */
    public void doSync() {

    }

    public void setMaxUndo(long maxUndo) {
        this.maxUndo = maxUndo;
    }

    public long getMaxSize() {
        return maxSize;
    }

    public String getDesc(XBDocOperation undoStep) {
        return undoStep.getCaption();
    }

    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }

    public long getUsedSize() {
        return usedSize;
    }

    public long getSyncPoint() {
        return syncPoint;
    }

    public void setSyncPoint(long syncPoint) {
        this.syncPoint = syncPoint;
    }
}
