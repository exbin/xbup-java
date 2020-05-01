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
package org.exbin.xbup.core.parser.token;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.ubnumber.UBNatural;

/**
 * XBUP protocol level 0 size precomputed begin token.
 *
 * Class marks beggining of block. Terminated flag carry information about
 * method for block termination type in bitstream.
 *
 * Additional blockSize represents precomputed size of block if available.
 *
 * @version 0.2.1 2017/05/22
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBSBeginToken extends XBBeginToken {

    @Nonnull
    private final XBBlockTerminationMode terminationMode;
    @Nullable
    private UBNatural blockSize;

    private XBSBeginToken(XBBlockTerminationMode terminationMode, @Nullable UBNatural blockSize) {
        this.terminationMode = terminationMode;
        this.blockSize = blockSize;
    }

    @Nonnull
    @Override
    public XBTokenType getTokenType() {
        return XBTokenType.BEGIN;
    }

    @Nonnull
    @Override
    public XBBlockTerminationMode getTerminationMode() {
        return terminationMode;
    }

    @Nullable
    public UBNatural getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(@Nullable UBNatural blockSize) {
        this.blockSize = blockSize;
    }

    @Nonnull
    public static XBSBeginToken create(XBBlockTerminationMode terminationMode, @Nullable UBNatural blockSize) {
        return new XBSBeginToken(terminationMode, blockSize);
    }
}
