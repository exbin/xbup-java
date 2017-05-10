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
import javax.annotation.Nullable;
import org.exbin.xbup.core.block.definition.XBGroupDef;
import org.exbin.xbup.core.serial.XBSerializable;

/**
 * Group declaration interface, either for catalog or local definition.
 *
 * @version 0.2.1 2017/05/10
 * @author ExBin Project (http://exbin.org)
 */
public interface XBGroupDecl extends XBSerializable {

    /**
     * Returns linked group definition.
     *
     * @return group definition
     */
    @Nullable
    XBGroupDef getGroupDef();

    /**
     * Returns linked revision.
     *
     * @return revision
     */
    long getRevision();

    /**
     * Returns list of block declarations.
     *
     * @return list of block declarations
     */
    @Nullable
    List<XBBlockDecl> getBlockDecls();
}
