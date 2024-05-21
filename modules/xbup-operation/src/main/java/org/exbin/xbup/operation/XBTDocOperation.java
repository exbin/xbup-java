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
package org.exbin.xbup.operation;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBTEditableDocument;
import org.exbin.xbup.core.type.XBData;
import org.exbin.xbup.operation.basic.XBBasicOperationType;
import org.exbin.xbup.operation.undo.UndoableOperation;

/**
 * Abstract class for operation using XBUP level 1 document.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public abstract class XBTDocOperation implements UndoableOperation {

    @Nonnull
    protected final XBTEditableDocument document;
    @Nonnull
    protected XBData data;

    public XBTDocOperation(XBTEditableDocument document) {
        this.document = document;
        data = new XBData();
    }

    /**
     * Returns type of the operation.
     *
     * @return basic type
     */
    @Nonnull
    public abstract XBBasicOperationType getBasicType();

    @Nonnull
    public XBTEditableDocument getDocument() {
        return document;
    }

    @Nonnull
    public XBData getData() {
        return data;
    }

    public void setData(XBData data) {
        this.data = data;
    }

    /**
     * Performs dispose of the operation.
     *
     * Default dispose is empty.
     *
     * @throws Exception exception
     */
    @Override
    public void dispose() throws Exception {
    }

    @Nonnull
    @Override
    public String getName() {
        return getBasicType().getCaption();
    }
}
