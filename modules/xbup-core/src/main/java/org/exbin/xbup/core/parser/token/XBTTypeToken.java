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
import org.exbin.xbup.core.block.XBBlockType;

/**
 * XBUP protocol level 1 block type token.
 *
 * @author ExBin Project (https://exbin.org)
 */
public abstract class XBTTypeToken implements XBTToken {

    @Nonnull
    public abstract XBBlockType getBlockType();

    @Override
    @Nonnull
    public XBTTokenType getTokenType() {
        return XBTTokenType.TYPE;
    }

    @Nonnull
    public static XBTTypeToken create(@Nonnull XBBlockType blockType) {
        return new XBTTypeTokenImpl(blockType);
    }

    private static class XBTTypeTokenImpl extends XBTTypeToken {

        @Nonnull
        private final XBBlockType blockType;

        private XBTTypeTokenImpl(@Nonnull XBBlockType blockType) {
            this.blockType = blockType;
        }

        @Nonnull
        @Override
        public XBBlockType getBlockType() {
            return blockType;
        }
    }
}
