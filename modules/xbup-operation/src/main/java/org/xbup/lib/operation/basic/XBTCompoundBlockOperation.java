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
package org.xbup.lib.operation.basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.xbup.lib.core.block.XBTEditableDocument;
import org.xbup.lib.core.type.XBData;
import org.xbup.lib.operation.CompoundOperation;
import org.xbup.lib.operation.Operation;
import org.xbup.lib.operation.XBTDocOperation;

/**
 * Compound operation for block change.
 *
 * @version 0.2.0 2016/02/27
 * @author ExBin Project (http://exbin.org)
 */
public class XBTCompoundBlockOperation extends XBTDocOperation implements CompoundOperation {

    private final List<Operation> operations = new ArrayList<>();

    public XBTCompoundBlockOperation(XBTEditableDocument document) {
        super(document);
    }

    public XBTCompoundBlockOperation(XBTEditableDocument document, Collection<Operation> initialOperations) {
        super(document);
        operations.addAll(initialOperations);
    }

    @Override
    public XBBasicOperationType getBasicType() {
        return XBBasicOperationType.MODIFY_BLOCK;
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

        return new XBTCompoundBlockOperation(document, undoOperations);
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

    @Override
    public void setData(XBData data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBData getData() {
        XBData newData = new XBData();
        int position = 0;
        for (Operation operation : operations) {
            XBData operationData = ((XBTDocOperation) operation).getData();
            newData.insert(position, operationData);
            position += operationData.getDataSize();
        }

        return newData;
    }
}
