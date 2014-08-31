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
import org.xbup.lib.core.block.XBTBlock;
import org.xbup.lib.core.block.XBTEditableBlock;
import org.xbup.lib.core.block.param.XBParamDecl;

/**
 * XBUP level 1 block definition interface.
 *
 * @version 0.1.20 2011/01/02
 * @author XBUP Project (http://xbup.org)
 */
public interface XBBlockDef {

    /**
     * Get list of parameters in order of appearance.
     *
     * @return list of parameters
     */
    public List<XBParamDecl> getParamDecls();

    /**
     * Get specific parameter declaration.
     *
     * @param index parameter declaration index
     * @return list of parameters
     */
    public XBParamDecl getParamDecl(int index);

    /**
     * Get count of parameter's declarations.
     *
     * @return count of declarations
     */
    public long getParamCount();

    /**
     * Get list of revisions in order of appliance.
     *
     * @return list of revisions
     */
    public List<XBRevisionDef> getRevisionDefs();

    /**
     * Get n-th parameter of the given block.
     *
     * @param block block to get parameter from
     * @param index index of the requested parameter
     * @return instance of block
     */
    public XBTBlock getParameter(XBTBlock block, int index);

    /**
     * Set n-th parameter of the given block.
     *
     * @param block block to modify parameter in
     * @param index index of parameter to modify
     * @param parameter parameter content
     */
    public void setParameter(XBTEditableBlock block, int index, XBTBlock parameter);

    /**
     * Get count of parameters defined in given block.
     *
     * @param block block to get count of parameters from
     * @return count of defined parameters
     */
    public int getParametersCount(XBTBlock block);
}
