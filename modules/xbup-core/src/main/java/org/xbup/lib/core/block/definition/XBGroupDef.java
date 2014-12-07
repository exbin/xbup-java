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
package org.xbup.lib.core.block.definition;

import java.util.List;
import org.xbup.lib.core.block.declaration.XBBlockDecl;

/**
 * XBUP level 1 group definition interface.
 *
 * @version 0.1.24 2014/12/07
 * @author XBUP Project (http://xbup.org)
 */
public interface XBGroupDef {

    /**
     * Gets list of group parameters.
     *
     * @return list of group parameters
     */
    public List<XBGroupParam> getGroupParams();

    /**
     * Gets revision definition.
     *
     * @return revision definition
     */
    public XBRevisionDef getRevisionDef();

    /**
     * Gets block declaration for given block index.
     *
     * @param blockId block index
     * @return block declaration
     */
    public XBBlockDecl getBlockDecl(int blockId);
}
