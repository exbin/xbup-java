/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
import org.exbin.xbup.operation.XBDocOperation;
import org.exbin.xbup.parser_tree.XBTree;

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
     * @param tree main tree object
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
