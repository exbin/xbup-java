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
package org.exbin.xbup.client;

import javax.annotation.Nonnull;
import org.exbin.xbup.core.parser.basic.XBListener;
import org.exbin.xbup.core.parser.basic.XBMatchingProvider;
import org.exbin.xbup.core.parser.token.event.XBEventListener;
import org.exbin.xbup.core.parser.token.pull.XBPullProvider;

/**
 * Catalog service message interface.
 *
 * @author ExBin Project (https://exbin.org)
 */
public interface XBCatalogServiceMessage {

    /**
     * Gets output stream.
     *
     * @return token output stream
     */
    @Nonnull
    XBEventListener getXBOutputStream();

    /**
     * Gets input stream.
     *
     * @return token input stream
     */
    @Nonnull
    XBPullProvider getXBInputStream();

    /**
     * Gets output listener.
     *
     * @return listener
     */
    @Nonnull
    XBListener getXBOutput();

    /**
     * Gets input checker.
     *
     * @return input stream checker
     */
    @Nonnull
    XBMatchingProvider getXBInput();

    /**
     * Closes message.
     */
    public void close();
}
