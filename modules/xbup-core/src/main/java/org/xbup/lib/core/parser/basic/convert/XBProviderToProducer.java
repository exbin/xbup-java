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
package org.xbup.lib.core.parser.basic.convert;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.XBProducer;
import org.xbup.lib.core.parser.basic.XBProvider;

/**
 * XBUP level 0 provider to producer convertor.
 *
 * @version 0.1.23 2014/02/16
 * @author ExBin Project (http://exbin.org)
 */
public class XBProviderToProducer implements XBProducer {

    private XBProvider provider = null;

    public XBProviderToProducer(XBProvider provider) {
        this.provider = provider;
    }

    @Override
    public void attachXBListener(XBListener listener) {
        XBCountingFilter countingListener = new XBCountingFilter(listener);
        
        do {
            try {
                provider.produceXB(countingListener);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBProviderToProducer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (!countingListener.isFinished());
    }
}
