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
 * XBUP protocol level 1 end token.
 *
 * @author ExBin Project (https://exbin.org)
 */
@Immutable
public abstract class XBTEndToken implements XBTToken {

    @Nonnull
    @Override
    public XBTTokenType getTokenType() {
        return XBTTokenType.END;
    }

    @Nullable
    private static XBTEndToken instance = null;

    @Nonnull
    public static XBTEndToken create() {
        if (instance == null) {
            instance = new XBTEndTokenImpl();
        }

        return instance;
    }

    @Immutable
    private static class XBTEndTokenImpl extends XBTEndToken {

        XBTEndTokenImpl() {
        }
    }
}
