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
package org.exbin.xbup.operation.basic.command;

import org.exbin.xbup.core.block.XBTEditableDocument;
import org.exbin.xbup.operation.Operation;
import org.exbin.xbup.operation.XBTOpDocCommand;
import org.exbin.xbup.operation.basic.XBBasicCommandType;
import org.exbin.xbup.operation.basic.XBTCompoundBlockOperation;

/**
 * Compound command for block change.
 *
 * @version 0.1.25 2015/06/30
 * @author ExBin Project (http://exbin.org)
 */
public class XBTChangeBlockCommand extends XBTOpDocCommand {

    public XBTChangeBlockCommand(XBTEditableDocument document) {
        super(document);
        super.setOperation(new XBTCompoundBlockOperation(document));
    }

    @Override
    public XBBasicCommandType getBasicType() {
        return XBBasicCommandType.BLOCK_MODIFIED;
    }

    public void appendOperation(Operation operation) {
        ((XBTCompoundBlockOperation) getOperation()).appendOperation(operation);
    }
}
