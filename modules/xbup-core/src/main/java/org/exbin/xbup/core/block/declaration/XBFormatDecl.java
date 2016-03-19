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
package org.exbin.xbup.core.block.declaration;

import java.util.List;
import org.exbin.xbup.core.block.definition.XBFormatDef;
import org.exbin.xbup.core.serial.XBSerializable;

/**
 * Format declaration interface, either for catalog or local definition.
 *
 * @version 0.1.24 2014/09/27
 * @author ExBin Project (http://exbin.org)
 */
public interface XBFormatDecl extends XBSerializable {

    /**
     * Returns linked format definition.
     *
     * @return format definition
     */
    public XBFormatDef getFormatDef();

    /**
     * Returns list of group declarations.
     *
     * @return list of group declarations
     */
    public List<XBGroupDecl> getGroupDecls();

    /**
     * Returns linked revision.
     *
     * @return revision
     */
    public long getRevision();
}
