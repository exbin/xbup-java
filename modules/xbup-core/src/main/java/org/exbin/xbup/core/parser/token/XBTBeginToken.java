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
import org.exbin.xbup.core.block.XBBlockTerminationMode;

/**
 * XBUP protocol level 1 begin token.
 *
 * Class marks beggining of block. Terminated flag carry information about
 * method for block termination type in bitstream.
 *
 * @version 0.2.1 2017/05/22
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public abstract class XBTBeginToken implements XBTToken {

    /**
     * Returns termination mode of the token.
     *
     * @return termination mode
     */
    @Nonnull
    public abstract XBBlockTerminationMode getTerminationMode();

    @Nonnull
    @Override
    public XBTTokenType getTokenType() {
        return XBTTokenType.BEGIN;
    }

    private static XBTBeginTokenImpl sizeSpecifiedBeginToken = null;
    private static XBTBeginTokenImpl terminatedByZeroBeginToken = null;

    @Nonnull
    public static XBTBeginToken create(XBBlockTerminationMode terminationMode) {
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
    public static XBTBeginToken getSizeSpecifiedInstance() {
        if (sizeSpecifiedBeginToken == null) {
            sizeSpecifiedBeginToken = new XBTBeginTokenImpl(XBBlockTerminationMode.SIZE_SPECIFIED);
        }

        return sizeSpecifiedBeginToken;
    }

    @Nonnull
    public static XBTBeginToken getTerminatedByZeroInstance() {
        if (terminatedByZeroBeginToken == null) {
            terminatedByZeroBeginToken = new XBTBeginTokenImpl(XBBlockTerminationMode.TERMINATED_BY_ZERO);
        }

        return terminatedByZeroBeginToken;
    }

    @ParametersAreNonnullByDefault
    private static class XBTBeginTokenImpl extends XBTBeginToken {

        @Nonnull
        private final XBBlockTerminationMode terminationMode;

        private XBTBeginTokenImpl(XBBlockTerminationMode terminationMode) {
            this.terminationMode = terminationMode;
        }

        @Nonnull
        @Override
        public XBBlockTerminationMode getTerminationMode() {
            return terminationMode;
        }
    }
}
