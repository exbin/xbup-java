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

import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Interface for compound XBUP editor operation.
 *
 * @version 0.2.1 2017/05/25
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface CompoundOperation extends Operation {

    /**
     * Append operation to the list of operations.
     *
     * @param operation appended operation
     */
    void appendOperation(Operation operation);

    /**
     * Append list of operations to the list of operations.
     *
     * @param operations appended operations
     */
    void appendOperations(Collection<Operation> operations);

    /**
     * Returns list of operations.
     *
     * @return list of operations
     */
    @Nonnull
    List<Operation> getOperations();

    /**
     * Returns true if compound operation is empty.
     *
     * @return true if operation is empty
     */
    boolean isEmpty();
}
