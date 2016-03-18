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
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBFilter;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.XBProvider;
import org.xbup.lib.core.parser.basic.XBProviderFilter;

/**
 * Default XBUP level 0 provider filter using regular filter.
 *
 * @version 0.1.25 2015/07/06
 * @author ExBin Project (http://exbin.org)
 */
public class XBFilterToProviderFilter implements XBProviderFilter {

    private XBProvider provider = null;
    private XBFilter filter;

    public XBFilterToProviderFilter() {
    }

    public XBFilterToProviderFilter(XBProvider provider) {
        this.provider = provider;
    }

    @Override
    public void produceXB(XBListener listener) throws XBProcessingException, IOException {
        filter.attachXBListener(listener);
        provider.produceXB(filter);
    }

    @Override
    public void attachXBProvider(XBProvider provider) {
        this.provider = provider;
    }

    public XBFilter getFilter() {
        return filter;
    }

    public void setFilter(XBFilter filter) {
        this.filter = filter;
    }
}
