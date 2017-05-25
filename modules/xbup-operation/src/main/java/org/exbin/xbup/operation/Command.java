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

import java.util.Date;
import javax.annotation.Nullable;

/**
 * Interface for XBUP editor command.
 *
 * @version 0.2.1 2017/05/25
 * @author ExBin Project (http://exbin.org)
 */
public interface Command {

    /**
     * Returns caption as text.
     *
     * @return text caption
     */
    @Nullable
    String getCaption();

    /**
     * Performs operation on given document.
     *
     * @throws java.lang.Exception exception
     */
    void execute() throws Exception;

    /**
     * Performs update of command use information.
     */
    void use();

    /**
     * Performs redo on given document.
     *
     * @throws java.lang.Exception exception
     */
    void redo() throws Exception;

    /**
     * Performs undo operation on given document.
     *
     * @throws java.lang.Exception exception
     */
    void undo() throws Exception;

    /**
     * Returns true if command support undo operation.
     *
     * @return true if undo supported
     */
    boolean canUndo();

    /**
     * Disposes command.
     *
     * @throws java.lang.Exception exception
     */
    void dispose() throws Exception;

    /**
     * Returns time of command execution.
     *
     * @return time
     */
    @Nullable
    Date getExecutionTime();
}
