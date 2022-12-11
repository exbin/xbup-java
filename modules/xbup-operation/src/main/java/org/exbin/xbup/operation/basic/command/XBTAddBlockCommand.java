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
package org.exbin.xbup.operation.basic.command;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBTEditableBlock;
import org.exbin.xbup.core.block.XBTEditableDocument;
import org.exbin.xbup.operation.XBTOpDocCommand;
import org.exbin.xbup.operation.basic.XBBasicCommandType;
import org.exbin.xbup.operation.basic.XBTAddBlockOperation;

/**
 * Command for adding child block.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBTAddBlockCommand extends XBTOpDocCommand {

    public XBTAddBlockCommand(XBTEditableDocument document, long parentPosition, int childIndex, XBTEditableBlock newNode) {
        super(document);
//        long position;
//        int childIndex = 0;
//        if ( == null) {
//            position = -1;
//        } else {
//            position = parentNode.getBlockIndex();
//            childIndex = parentNode.getChildrenCount();
//        }
        super.setOperation(new XBTAddBlockOperation(document, parentPosition, childIndex, newNode));
    }

    @Nonnull
    @Override
    public XBBasicCommandType getBasicType() {
        return XBBasicCommandType.BLOCK_ADDED;
    }
}
