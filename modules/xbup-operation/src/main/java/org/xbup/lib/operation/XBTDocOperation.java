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
package org.xbup.lib.operation;

import org.xbup.lib.core.block.XBTEditableDocument;
import org.xbup.lib.core.type.XBData;
import org.xbup.lib.operation.basic.XBBasicOperationType;

/**
 * Abstract class for operation using XBUP level 1 document.
 *
 * @version 0.2.0 2016/02/27
 * @author ExBin Project (http://exbin.org)
 */
public abstract class XBTDocOperation implements Operation {

    protected final XBTEditableDocument document;
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
    public abstract XBBasicOperationType getBasicType();

    public XBTEditableDocument getDocument() {
        return document;
    }

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
     * @throws Exception
     */
    @Override
    public void dispose() throws Exception {
    }

    @Override
    public String getCaption() {
        return getBasicType().getCaption();
    }
}
