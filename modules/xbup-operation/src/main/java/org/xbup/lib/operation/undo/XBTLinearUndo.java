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
import org.xbup.lib.operation.XBTCommand;

/**
 * Linear undo command sequence storage.
 *
 * @version 0.1 wr20.0 2010/09/15
 * @author XBUP Project (http://xbup.org)
 */
public class XBTLinearUndo {

    private long maxUndo; // Max count of revert steps
    private long maxSize; // Max size of revert steps space
    private long usedSize; // Currently used size
    private long position; // Current position in revert step list
    private long syncPoint; // Position of currently saved file in step list
    private List<XBTCommand> commandList;
    private XBTTreeDocument document;

    /**
     * Creates a new instance
     */
    public XBTLinearUndo(XBTTreeDocument document) {
        this.document = document;
        maxUndo = 1024;
        maxSize = 65535;
        commandList = new ArrayList<XBTCommand>();
        init();
    }

    /** Add new step into revert list */
    public void performStep(XBTCommand step) throws Exception {
        step.perform(document);
        // TODO: Check for maxUndo & size
        while (commandList.size()>position) {
            commandList.remove((int) position);
        }
        commandList.add(step);
        position++;
    }

    /** Perform single undo step */
    public void performUndo() throws Exception {
        position--;
        XBTCommand step = commandList.get((int) position);
        step.revert(document);
    }

    /** Perform single redo step */
    public void performRedo() throws Exception {
        XBTCommand step = commandList.get((int) position);
        step.perform(document);
        position++;
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
        if (commandList.size() - position < count) {
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
        return position > 0;
    }

    public boolean canRedo() {
        return commandList.size() > position;
    }

    public long getMaxUndo() {
        return maxUndo;
    }

    /** Perform revert to sync point */
    public void doSync() {
    }

    public void setMaxUndo(long maxUndo) {
        this.maxUndo = maxUndo;
    }

    public long getMaxSize() {
        return maxSize;
    }

    public String getDesc(XBTCommand undoStep) {
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

    public void setSyncPoint() {
        this.syncPoint = position;
    }

    private void init() {
        usedSize = 0;
        position = 0;
        setSyncPoint(0);
        commandList.clear();
    }

    /*            switch (opType) {
                case NODE_ADD:
                case NODE_DEL:
                    return "Deleted item";
                case NODE_MOD:
                    return "Modified item";
                case NODE_RSZ_ATTR:
                    return "Resized count of attributes";
                case NODE_MOD_DATA:
                    return "Modified item data part";
                case NODE_RSZ_DATA:
                    return "Resized item data part";
                case NODE_SWAP:
                    return "Swaped items";
                default:
                    return "Unknown";
            } */
}
