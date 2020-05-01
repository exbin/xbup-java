/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
import org.exbin.xbup.core.block.XBEditableDocument;
import org.exbin.xbup.operation.basic.XBBasicCommandType;

/**
 * Abstract class for operation using XBUP level 0 document.
 *
 * @version 0.2.1 2017/06/06
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public abstract class XBDocCommand extends AbstractCommand {

    @Nonnull
    protected XBEditableDocument document;

    /**
     * Returns command type.
     *
     * @return command type
     */
    @Nonnull
    public abstract XBBasicCommandType getBasicType();

    @Nonnull
    public XBEditableDocument getDocument() {
        return document;
    }

    public void setDocument(XBEditableDocument document) {
        this.document = document;
    }

    /**
     * Default dispose is empty.
     *
     * @throws Exception exception
     */
    @Override
    public void dispose() throws Exception {
    }

    @Nonnull
    @Override
    public String getCaption() {
        return getBasicType().getCaption();
    }
}
