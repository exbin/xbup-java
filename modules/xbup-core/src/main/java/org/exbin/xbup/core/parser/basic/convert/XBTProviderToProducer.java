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
package org.exbin.xbup.core.parser.basic.convert;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.basic.XBTListener;
import org.exbin.xbup.core.parser.basic.XBTProducer;
import org.exbin.xbup.core.parser.basic.XBTProvider;

/**
 * XBUP level 1 provider to producer convertor.
 *
 * @version 0.1.23 2013/11/18
 * @author ExBin Project (http://exbin.org)
 */
public class XBTProviderToProducer implements XBTProducer {

    private XBTProvider provider = null;

    public XBTProviderToProducer(XBTProvider provider) {
        this.provider = provider;
    }

    @Override
    public void attachXBTListener(XBTListener listener) {
        XBTCountingFilter countingListener = new XBTCountingFilter(listener);
        
        do {
            try {
                provider.produceXBT(countingListener);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBProviderToProducer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (!countingListener.isFinished());
    }
}
