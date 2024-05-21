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
import org.exbin.xbup.operation.basic.XBBasicCommandType;

/**
 * Abstract class for operation using XBUP level 1 document.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public abstract class XBTDocCommand extends AbstractCommand {

    @Nonnull
    protected final XBTEditableDocument document;

    public XBTDocCommand(XBTEditableDocument document) {
        this.document = document;
    }

    /**
     * Returns type of the command.
     *
     * @return command type
     */
    @Nonnull
    public abstract XBBasicCommandType getBasicType();

    @Nonnull
    public XBTEditableDocument getDocument() {
        return document;
    }

    /**
     * Default dispose is empty.
     */
    @Override
    public void dispose() {
    }

    @Nonnull
    @Override
    public String getName() {
        return getBasicType().getCaption();
    }
}
