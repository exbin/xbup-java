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
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import org.exbin.xbup.core.block.XBBlockTerminationMode;

/**
 * XBUP protocol level 0 begin token.
 *
 * Class marks beggining of block. Terminated flag carry information about
 * method for block termination type in bitstream.
 *
 * @version 0.2.1 2017/05/22
 * @author ExBin Project (http://exbin.org)
 */
@Immutable
@ParametersAreNonnullByDefault
public abstract class XBBeginToken implements XBToken {

    /**
     * Returns termination mode of the token.
     *
     * @return termination mode
     */
    @Nonnull
    public abstract XBBlockTerminationMode getTerminationMode();

    @Nonnull
    @Override
    public XBTokenType getTokenType() {
        return XBTokenType.BEGIN;
    }

    private static XBBeginTokenImpl sizeSpecifiedBeginToken = null;
    private static XBBeginTokenImpl terminatedByZeroBeginToken = null;

    @Nonnull
    public static XBBeginToken create(XBBlockTerminationMode terminationMode) {
        switch (terminationMode) {
            case SIZE_SPECIFIED:
                return getSizeSpecifiedInstance();
            case TERMINATED_BY_ZERO:
                return getTerminatedByZeroInstance();
            default:
                throw new IllegalStateException("Unexpected termination mode");
        }
    }

    @Nonnull
    public static XBBeginToken getSizeSpecifiedInstance() {
        if (sizeSpecifiedBeginToken == null) {
            sizeSpecifiedBeginToken = new XBBeginTokenImpl(XBBlockTerminationMode.SIZE_SPECIFIED);
        }

        return sizeSpecifiedBeginToken;
    }

    @Nonnull
    public static XBBeginToken getTerminatedByZeroInstance() {
        if (terminatedByZeroBeginToken == null) {
            terminatedByZeroBeginToken = new XBBeginTokenImpl(XBBlockTerminationMode.TERMINATED_BY_ZERO);
        }

        return terminatedByZeroBeginToken;
    }

    @ParametersAreNonnullByDefault
    public static class XBBeginTokenImpl extends XBBeginToken {

        @Nonnull
        private final XBBlockTerminationMode terminationMode;

        private XBBeginTokenImpl(XBBlockTerminationMode terminationMode) {
            this.terminationMode = terminationMode;
        }

        @Nonnull
        @Override
        public XBBlockTerminationMode getTerminationMode() {
            return terminationMode;
        }
    }
}
