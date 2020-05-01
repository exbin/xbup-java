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
package org.exbin.xbup.core.parser.token.pull.convert;

import java.io.IOException;
import javax.annotation.Nonnull;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.basic.XBListener;
import org.exbin.xbup.core.parser.basic.XBProvider;
import org.exbin.xbup.core.parser.token.XBToken;
import org.exbin.xbup.core.parser.token.convert.XBListenerToToken;
import org.exbin.xbup.core.parser.token.pull.XBPullProvider;

/**
 * Pull provider to provider convertor for XBUP protocol level 0.
 *
 * @version 0.2.1 2017/06/05
 * @author ExBin Project (http://exbin.org)
 */
public class XBPullProviderToProvider implements XBProvider {

    @Nonnull
    private final XBPullProvider pullProvider;

    public XBPullProviderToProvider(@Nonnull XBPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    @Override
    public void produceXB(@Nonnull XBListener listener) throws XBProcessingException, IOException {
        XBToken token = pullProvider.pullXBToken();
        XBListenerToToken.tokenToListener(token, listener);
    }
}
