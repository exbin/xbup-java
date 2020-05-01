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
package org.exbin.xbup.operation.basic.command;

import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBTEditableDocument;
import org.exbin.xbup.operation.Operation;
import org.exbin.xbup.operation.XBTDocOperation;
import org.exbin.xbup.operation.XBTOpDocCommand;
import org.exbin.xbup.operation.basic.XBBasicCommandType;
import org.exbin.xbup.operation.basic.XBTCompoundBlockOperation;

/**
 * Compound command for block change.
 *
 * @version 0.1.25 2015/06/30
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBTChangeBlockCommand extends XBTOpDocCommand {

    public XBTChangeBlockCommand(XBTEditableDocument document) {
        super(document);
        super.setOperation(new XBTCompoundBlockOperation(document));
    }

    @Nonnull
    @Override
    public XBBasicCommandType getBasicType() {
        return XBBasicCommandType.BLOCK_MODIFIED;
    }

    public void appendOperation(Operation operation) {
        Optional<XBTDocOperation> commandOperation = getOperation();
        if (commandOperation.isPresent()) {
            ((XBTCompoundBlockOperation) commandOperation.get()).appendOperation(operation);
        } else {
            setOperation((XBTDocOperation) operation);
        }
    }
}
