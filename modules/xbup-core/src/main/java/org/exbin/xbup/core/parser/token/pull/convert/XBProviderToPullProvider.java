/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.parser.token.pull.convert;

import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.basic.XBProvider;
import org.exbin.xbup.core.parser.basic.XBSListener;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.parser.token.XBAttributeToken;
import org.exbin.xbup.core.parser.token.XBBeginToken;
import org.exbin.xbup.core.parser.token.XBDataToken;
import org.exbin.xbup.core.parser.token.XBEndToken;
import org.exbin.xbup.core.parser.token.XBSBeginToken;
import org.exbin.xbup.core.parser.token.XBToken;
import org.exbin.xbup.core.parser.token.pull.XBPullProvider;
import org.exbin.xbup.core.ubnumber.UBNatural;

/**
 * Provider To pull provider convertor for XBUP protocol level 0.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBProviderToPullProvider implements XBPullProvider {

    @Nonnull
    private final XBProvider provider;
    @Nullable
    private XBToken token;

    public XBProviderToPullProvider(XBProvider provider) {
        this.provider = provider;
    }

    @Nonnull
    @Override
    public XBToken pullXBToken() throws XBProcessingException, IOException {
        provider.produceXB(new XBSListener() {
            @Override
            public void beginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
                token = XBBeginToken.create(terminationMode);
            }

            @Override
            public void beginXB(XBBlockTerminationMode terminationMode, UBNatural blockSize) throws XBProcessingException, IOException {
                token = XBSBeginToken.create(terminationMode, blockSize);
            }

            @Override
            public void attribXB(XBAttribute value) throws XBProcessingException, IOException {
                token = XBAttributeToken.create(value);
            }

            @Override
            public void dataXB(InputStream data) throws XBProcessingException, IOException {
                token = XBDataToken.create(data);
            }

            @Override
            public void endXB() throws XBProcessingException, IOException {
                token = XBEndToken.create();
            }
        });

        return token;
    }
}
