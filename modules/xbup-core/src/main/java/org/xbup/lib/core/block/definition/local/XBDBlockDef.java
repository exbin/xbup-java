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
package org.xbup.lib.core.block.definition.local;

import java.util.List;
import org.xbup.lib.core.block.XBTBlock;
import org.xbup.lib.core.block.XBTEditableBlock;
import org.xbup.lib.core.block.definition.XBBlockDef;
import org.xbup.lib.core.block.param.XBParamDecl;
import org.xbup.lib.core.parser.param.XBParamPosition;
import org.xbup.lib.core.serial.XBSerializable;

/**
 * XBUP level 1 block definition.
 *
 * @version 0.1.21 2011/12/02
 * @author XBUP Project (http://xbup.org)
 */
public class XBDBlockDef implements XBBlockDef, XBSerializable {

    private List<XBParamDecl> params;
    private List<XBDRevisionDef> revisionDefs;

    public XBDBlockDef() {
    }

    @Override
    public List<XBDRevisionDef> getRevisionDefs() {
        return revisionDefs;
    }

    public void setRevisionDefs(List<XBDRevisionDef> revisionDefs) {
        this.revisionDefs = revisionDefs;
    }

    @Override
    public XBTBlock getParameter(XBTBlock block, int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Sets n-th parameter of the given block.
     *
     * @param targetBlock block to modify parameter in
     * @param index index of parameter to modify
     * @param parameter parameter content
     */
    public void setParameter(XBTEditableBlock targetBlock, int index, XBTBlock parameter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getParametersCount(XBTBlock block) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBParamPosition getParamPosition(XBSerializable source, int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setParams(List<XBParamDecl> params) {
        this.params = params;
    }

    @Override
    public XBParamDecl getParamDecl(int index) {
        return params.get(index);
    }

    @Override
    public List<XBParamDecl> getParamDecls() {
        return params;
    }

    @Override
    public long getParamCount() {
        return params.size();
    }
}
