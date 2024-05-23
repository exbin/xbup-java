/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.operation.basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBTEditableDocument;
import org.exbin.xbup.core.type.XBData;
import org.exbin.xbup.operation.CompoundOperation;
import org.exbin.xbup.operation.Operation;
import org.exbin.xbup.operation.XBTDocOperation;
import org.exbin.xbup.operation.undo.UndoableOperation;

/**
 * Compound operation for block change.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBTCompoundBlockOperation extends XBTDocOperation implements CompoundOperation {

    private final List<Operation> operations = new ArrayList<>();

    public XBTCompoundBlockOperation(XBTEditableDocument document) {
        super(document);
    }

    public XBTCompoundBlockOperation(XBTEditableDocument document, Collection<Operation> initialOperations) {
        super(document);
        operations.addAll(initialOperations);
    }

    @Nonnull
    @Override
    public XBBasicOperationType getBasicType() {
        return XBBasicOperationType.MODIFY_BLOCK;
    }

    @Override
    public void execute() {
        for (Operation operation : operations) {
            operation.execute();
        }
    }

    @Nonnull
    @Override
    public UndoableOperation executeWithUndo() {
        LinkedList<Operation> undoOperations = new LinkedList<>();
        for (Operation operation : operations) {
            Operation childOperation = ((UndoableOperation) operation).executeWithUndo();
            undoOperations.add(0, childOperation);
        }

        return new XBTCompoundBlockOperation(document, undoOperations);
    }

    @Override
    public void addOperation(Operation operation) {
        operations.add(operation);
    }

    @Override
    public void addOperations(Collection<Operation> appendedOperations) {
        operations.addAll(appendedOperations);
    }

    @Nonnull
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

    @Nonnull
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
