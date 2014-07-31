/*
 * Copyright (C) XBUP Project
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

import org.xbup.lib.core.block.XBEditableDocument;
import org.xbup.lib.operation.basic.XBBasicCommandType;

/**
 * Interface for XBUP document command.
 *
 * @version 0.1 wr20.0 2010/09/12
 * @author XBUP Project (http://xbup.org)
 */
public interface XBCommand {

    /** Get revert step type */
    public XBBasicCommandType getOpType();

    /** Get caption as text */
    public String getCaption();

    /**
     * Perform operation on given document.
     *
     * @param document a document to perform operation on.
     */
    public void perform(XBEditableDocument document) throws Exception;

    /**
     * Perform revert operation on given document.
     *
     * @param document a document to perform operation on.
     */
    public void revert(XBEditableDocument document) throws Exception;

    /**
     * Returns true if command support revert operation.
     *
     * @return true if revert supported.
     */
    public boolean supportRevert();
}
