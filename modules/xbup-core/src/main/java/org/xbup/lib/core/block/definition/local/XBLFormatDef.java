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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.xbup.lib.core.block.definition.XBFormatDef;
import java.util.List;
import org.xbup.lib.core.block.declaration.XBGroupDecl;
import org.xbup.lib.core.block.definition.XBFormatParam;
import org.xbup.lib.core.block.definition.XBFormatParamConsist;
import org.xbup.lib.core.block.definition.XBRevisionDef;
import org.xbup.lib.core.block.definition.XBRevisionParam;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.param.XBSerializationMode;
import org.xbup.lib.core.serial.param.XBTSequenceSerialHandler;
import org.xbup.lib.core.serial.param.XBTSequenceSerializable;

/**
 * XBUP level 1 local format definition.
 *
 * @version 0.1.24 2015/01/18
 * @author XBUP Project (http://xbup.org)
 */
public class XBLFormatDef implements XBFormatDef, XBTSequenceSerializable {

    private List<XBFormatParam> formatParams = new ArrayList<>();
    private XBLRevisionDef revisionDef;

    public XBLFormatDef() {
    }

    public XBLFormatDef(XBGroupDecl groupDecl) {
        formatParams.add(new XBFormatParamConsist(groupDecl));
    }

    public XBLFormatDef(XBLRevisionDef revisionDef) {
        this.revisionDef = revisionDef;
    }

    @Override
    public List<XBFormatParam> getFormatParams() {
        return formatParams;
    }

    public void setFormats(List<XBFormatParam> formats) {
        this.formatParams = formats;
    }

    @Override
    public XBRevisionDef getRevisionDef() {
        return revisionDef;
    }

    @Override
    public XBGroupDecl getGroupDecl(int groupId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBFormatParam getFormatParam(int paramIndex) {
        return formatParams.get(paramIndex);
    }

    public void realignRevision() {
        revisionDef = new XBLRevisionDef();
        XBRevisionParam revisionParam = new XBRevisionParam();
        revisionParam.setLimit(formatParams.size());
        revisionDef.getRevParams().add(revisionParam);
    }

    @Override
    public void serializeXB(XBTSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
        serializationHandler.begin();
        if (serializationHandler.getSerializationMode() == XBSerializationMode.PULL) {
            serializationHandler.pullData();
        } else {
            serializationHandler.putData(new ByteArrayInputStream(new byte[0]));
        }
        serializationHandler.end();
    }
}
