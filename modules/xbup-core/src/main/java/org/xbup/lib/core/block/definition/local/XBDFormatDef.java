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

import java.util.ArrayList;
import org.xbup.lib.core.block.definition.XBFormatDef;
import java.util.List;
import org.xbup.lib.core.block.declaration.XBGroupDecl;
import org.xbup.lib.core.block.definition.XBFormatParam;
import org.xbup.lib.core.block.definition.XBFormatParamConsist;
import org.xbup.lib.core.block.definition.XBRevisionDef;
import org.xbup.lib.core.serial.XBSerializable;

/**
 * XBUP level 1 format definition.
 *
 * @version 0.1.24 2014/11/30
 * @author XBUP Project (http://xbup.org)
 */
public class XBDFormatDef implements XBSerializable, XBFormatDef {

    private List<XBFormatParam> formats = new ArrayList<>();
    private XBDRevisionDef revisionDef;

    public XBDFormatDef() {
    }

    public XBDFormatDef(XBGroupDecl groupDecl) {
        formats.add(new XBFormatParamConsist(groupDecl));
    }

    @Override
    public List<XBFormatParam> getFormatParams() {
        return formats;
    }

    public void setFormats(List<XBFormatParam> formats) {
        this.formats = formats;
    }

    @Override
    public XBRevisionDef getRevisionDef() {
        return revisionDef;
    }

    @Override
    public XBGroupDecl getGroupDecl(int groupId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
