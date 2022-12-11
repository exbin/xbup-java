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
package org.exbin.xbup.core.parser.token;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * XBUP protocol level 0 end token.
 *
 * @author ExBin Project (https://exbin.org)
 */
@Immutable
public abstract class XBEndToken implements XBToken {

    private XBEndToken() {
    }

    @Nonnull
    @Override
    public XBTokenType getTokenType() {
        return XBTokenType.END;
    }

    @Nullable
    private static XBEndToken instance = null;

    @Nonnull
    public static XBEndToken create() {
        if (instance == null) {
            instance = new XBEndTokenImpl();
        }

        return instance;
    }

    @Immutable
    private static class XBEndTokenImpl extends XBEndToken {

        XBEndTokenImpl() {
        }
    }
}
