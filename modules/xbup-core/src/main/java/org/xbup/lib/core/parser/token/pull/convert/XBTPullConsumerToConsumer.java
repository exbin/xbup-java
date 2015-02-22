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
package org.xbup.lib.core.parser.token.pull.convert;

import org.xbup.lib.core.parser.basic.XBTConsumer;
import org.xbup.lib.core.parser.basic.XBTProvider;
import org.xbup.lib.core.parser.token.pull.XBTPullConsumer;

/**
 * Pull consumer to consumer convertor for XBUP protocol level 1.
 *
 * @version 0.1.23 2014/02/06
 * @author XBUP Project (http://xbup.org)
 */
public class XBTPullConsumerToConsumer implements XBTConsumer {

    private XBTProvider provider;

    public XBTPullConsumerToConsumer(XBTPullConsumer pullConsumer) {
        pullConsumer.attachXBTPullProvider(new XBTProviderToPullProvider(provider));
    }

    @Override
    public void attachXBTProvider(XBTProvider provider) {
        this.provider = provider;
    }
}
