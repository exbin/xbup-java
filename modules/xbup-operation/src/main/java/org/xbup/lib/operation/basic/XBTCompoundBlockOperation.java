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
package org.xbup.lib.operation.basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.xbup.lib.operation.CompoundOperation;
import org.xbup.lib.operation.Operation;
import org.xbup.lib.operation.XBTDocOperation;

/**
 * Compound operation for block change.
 *
 * @version 0.1.25 2015/06/30
 * @author XBUP Project (http://xbup.org)
 */
public class XBTCompoundBlockOperation extends XBTDocOperation implements CompoundOperation {

    private final List<Operation> operations = new ArrayList<>();

    public XBTCompoundBlockOperation() {
    }

    public XBTCompoundBlockOperation(Collection<Operation> initialOperations) {
        operations.addAll(initialOperations);
    }

    @Override
    public XBBasicOperationType getBasicType() {
        return XBBasicOperationType.MODIFY_NODE;
    }

    @Override
    public void execute() throws Exception {
        for (Operation operation : operations) {
            operation.execute();
        }
    }

    @Override
    public Operation executeWithUndo() throws Exception {
        LinkedList<Operation> undoOperations = new LinkedList<>();
        for (Operation operation : operations) {
            undoOperations.add(0, operation.executeWithUndo());
        }

        return new XBTCompoundBlockOperation(undoOperations);
    }

    @Override
    public void appendOperation(Operation operation) {
        operations.add(operation);
    }

    @Override
    public void appendOperations(Collection<Operation> appendedOperations) {
        operations.addAll(appendedOperations);
    }

    @Override
    public List<Operation> getOperations() {
        return operations;
    }

    @Override
    public boolean isEmpty() {
        return operations.isEmpty();
    }
}
