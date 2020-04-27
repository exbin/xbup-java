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

import java.util.Optional;
import javax.annotation.Nonnull;

/**
 * Interface for XBUP editor operation.
 *
 * @version 0.2.1 2017/05/25
 * @author ExBin Project (http://exbin.org)
 */
public interface Operation {

    /**
     * Returns caption as text.
     *
     * @return text caption
     */
    @Nonnull
    String getCaption();

    /**
     * Performs operation on given document.
     *
     * @throws java.lang.Exception exception
     */
    void execute() throws Exception;

    /**
     * Performs operation on given document and returns undo operation.
     *
     * @return undo operation or null if not available
     * @throws java.lang.Exception exception
     */
    @Nonnull
    Optional<Operation> executeWithUndo() throws Exception;

    /**
     * Disposes command.
     *
     * @throws java.lang.Exception exception
     */
    void dispose() throws Exception;
}
