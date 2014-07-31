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
package org.xbup.lib.core.serial.sequence;

import org.xbup.lib.core.serial.XBSerialHandler;
import org.xbup.lib.core.serial.XBSerialMethod;

/**
 * Interface for serialization methods for serialization into XBUP level 1
 * protocol using sequence definition.
 *
 * @version 0.1 wr23.0 2014/03/03
 * @author XBUP Project (http://xbup.org)
 */
public class XBTSerialSequenceListenerMethod implements XBSerialMethod {

    private int methodIndex = 0;

    public XBTSerialSequenceListenerMethod() {
    }

    public XBTSerialSequenceListenerMethod(int methodIndex) {
        this();
        this.methodIndex = methodIndex;
    }
    
    /**
     * Get serialization method index.
     *
     * @return method index
     */
    @Override
    public int getMethodIndex() {
        return methodIndex;
    }

    @Override
    public Class<? extends XBSerialHandler> getHandlerClass() {
        return XBTSerialSequenceListenerHandler.class;
    }
}
