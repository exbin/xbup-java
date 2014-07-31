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

import java.io.IOException;
import java.util.List;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.XBSerialHandler;
import org.xbup.lib.core.serial.XBSerialMethod;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.XBSerializationType;

/**
 * XBUP level 1 revision definition.
 *
 * @version 0.1 wr21.0 2011/12/02
 * @author XBUP Project (http://xbup.org)
 */
public class XBRevisionDef implements XBSerializable {

    private long revision;

    public XBRevisionDef(long revision) {
        this.revision = revision;
    }

    public XBRevisionDef() {
        this(0);
    }

    /**
     * @return the revision
     */
    public long getRevision() {
        return revision;
    }

    /**
     * @param revision the revision to set
     */
    public void setRevision(long revision) {
        this.revision = revision;
    }

    @Override
    public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
