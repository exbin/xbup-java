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

import javax.annotation.Nonnull;
import org.exbin.xbup.core.parser.basic.XBConsumer;
import org.exbin.xbup.core.parser.token.pull.XBPullConsumer;
import org.exbin.xbup.core.parser.token.pull.XBPullProvider;

/**
 * Consumer to pull consumer convertor for XBUP protocol level 0.
 *
 * @version 0.2.1 2017/06/05
 * @author ExBin Project (http://exbin.org)
 */
public class XBConsumerToPullConsumer implements XBPullConsumer {

    @Nonnull
    private final XBConsumer consumer;

    public XBConsumerToPullConsumer(@Nonnull XBConsumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public void attachXBPullProvider(@Nonnull XBPullProvider pullProvider) {
        consumer.attachXBProvider(new XBPullProviderToProvider(pullProvider));
    }
}
