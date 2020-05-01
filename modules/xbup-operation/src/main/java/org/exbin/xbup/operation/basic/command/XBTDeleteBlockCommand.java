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

import org.exbin.xbup.core.block.XBTEditableDocument;
import org.exbin.xbup.operation.XBTOpDocCommand;
import org.exbin.xbup.operation.basic.XBBasicCommandType;
import org.exbin.xbup.operation.basic.XBTDeleteBlockOperation;
import org.exbin.xbup.parser_tree.XBTTreeNode;

/**
 * Command for deleting block.
 *
 * @version 0.2.0 2015/09/19
 * @author ExBin Project (http://exbin.org)
 */
public class XBTDeleteBlockCommand extends XBTOpDocCommand {

    public XBTDeleteBlockCommand(XBTEditableDocument document, XBTTreeNode node) {
        super(document);
        super.setOperation(new XBTDeleteBlockOperation(document, node));
    }

    @Override
    public XBBasicCommandType getBasicType() {
        return XBBasicCommandType.BLOCK_DELETED;
    }
}
