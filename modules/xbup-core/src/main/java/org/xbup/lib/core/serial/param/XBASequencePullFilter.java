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
package org.xbup.lib.core.serial.param;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.event.XBTEventFilter;
import org.xbup.lib.core.parser.token.event.XBTEventListener;
import org.xbup.lib.core.parser.token.event.convert.XBTCompactingEventFilter;
import org.xbup.lib.core.serial.XBSerializable;

/**
 * Level 2 filter performing block building using sequence operations.
 *
 * @version 0.1.24 2015/01/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBASequencePullFilter implements XBTEventFilter {

    private XBTCompactingEventFilter eventListener;

    private final List<XBSerializable> childSequence = new ArrayList<>();

    public XBASequencePullFilter(XBTCompactingEventFilter eventListener) {
        attachListener(eventListener);
    }
    
    public XBASequencePullFilter(XBTEventListener eventListener) {
        attachListener(eventListener);
    }

    @Override
    public void attachXBTEventListener(XBTEventListener eventListener) {
        attachListener(eventListener);
    }

    private void attachListener(XBTEventListener eventListener) {
        if (eventListener instanceof XBTCompactingEventFilter) {
            this.eventListener = (XBTCompactingEventFilter) eventListener;
        } else {
            this.eventListener = new XBTCompactingEventFilter(eventListener);
        }
    }

    @Override
    public void putXBTToken(XBTToken token) throws XBProcessingException, IOException {
    }
}
