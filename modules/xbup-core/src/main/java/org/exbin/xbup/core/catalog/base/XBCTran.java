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
package org.exbin.xbup.core.catalog.base;

/**
 * Interface for catalog item transformation entity.
 *
 * @version 0.1.21 2011/12/29
 * @author ExBin Project (http://exbin.org)
 */
public interface XBCTran extends XBCBase {

    /**
     * Gets owner which is directory.
     *
     * @return block specification
     */
    public XBCBlockSpec getOwner();

    /**
     * Gets target of the procedure.
     *
     * @return block revision
     */
    public XBCBlockRev getTarget();

    /**
     * Gets limitation of the procedure.
     *
     * @return specification's limitation
     */
    public XBCLimitSpec getLimit();

    /**
     * Gets exception of the procedure.
     *
     * @return block revision
     */
    public XBCBlockRev getExcept();
}
