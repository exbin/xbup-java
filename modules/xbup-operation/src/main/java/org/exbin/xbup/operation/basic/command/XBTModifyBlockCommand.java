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

import org.exbin.xbup.core.block.XBTEditableDocument;
import org.exbin.xbup.operation.XBTOpDocCommand;
import org.exbin.xbup.operation.basic.XBBasicCommandType;
import org.exbin.xbup.operation.basic.XBTModifyBlockOperation;
import org.exbin.xbup.parser_tree.XBTTreeNode;

/**
 * Command for modifying block.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBTModifyBlockCommand extends XBTOpDocCommand {

    public XBTModifyBlockCommand(XBTEditableDocument document, XBTTreeNode node, XBTTreeNode newNode) {
        super(document);
        long position = node.getBlockIndex();
        super.setOperation(new XBTModifyBlockOperation(document, position, newNode));
    }

    @Override
    public XBBasicCommandType getBasicType() {
        return XBBasicCommandType.BLOCK_MODIFIED;
    }
}
