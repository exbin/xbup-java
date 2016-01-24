/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xbup.lib.operation.undo;

import java.util.List;
import org.xbup.lib.operation.XBTDocCommand;

/**
 * Undo support handler.
 *
 * @version 0.2.0 2016/01/24
 * @author XBUP Project (http://xbup.org)
 */
public interface XBUndoHandler {

    boolean canRedo();

    boolean canUndo();

    void clear();

    /**
     * Performs revert to sync point.
     *
     * @throws java.lang.Exception
     */
    void doSync() throws Exception;

    /**
     * Adds new step into revert list.
     *
     * @param command
     * @throws java.lang.Exception
     */
    void execute(XBTDocCommand command) throws Exception;

    List<XBTDocCommand> getCommandList();

    long getCommandPosition();

    long getMaximumUndo();

    long getSyncPoint();

    long getUndoMaximumSize();

    long getUsedSize();

    /**
     * Performs single redo step.
     *
     * @throws java.lang.Exception
     */
    void performRedo() throws Exception;

    /**
     * Performs multiple redo step.
     *
     * @param count count of steps
     * @throws Exception
     */
    void performRedo(int count) throws Exception;

    /**
     * Performs single undo step.
     *
     * @throws java.lang.Exception
     */
    void performUndo() throws Exception;

    /**
     * Performs multiple undo step.
     *
     * @param count count of steps
     * @throws Exception
     */
    void performUndo(int count) throws Exception;

    /**
     * Performs undo or redo operation to reach given position.
     *
     * @param targetPosition desired position
     * @throws java.lang.Exception
     */
    void setCommandPosition(long targetPosition) throws Exception;

    void setSyncPoint(long syncPoint);

    void setSyncPoint();
    
    void addUndoUpdateListener(UndoUpdateListener listener);
    
    void removeUndoUpdateListener(UndoUpdateListener listener);
}
