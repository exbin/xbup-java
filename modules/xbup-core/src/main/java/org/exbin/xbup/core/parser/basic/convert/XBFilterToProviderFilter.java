/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.parser.basic.convert;

import java.io.IOException;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.basic.XBFilter;
import org.exbin.xbup.core.parser.basic.XBListener;
import org.exbin.xbup.core.parser.basic.XBProvider;
import org.exbin.xbup.core.parser.basic.XBProviderFilter;

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
