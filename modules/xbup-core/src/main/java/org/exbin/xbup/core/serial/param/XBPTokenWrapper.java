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
package org.exbin.xbup.core.serial.param;

import java.io.IOException;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.XBTToken;

/**
 * Token wrapper for level 2 serialization event.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBPTokenWrapper implements XBPSerializable {

    private XBTToken token;

    public XBPTokenWrapper(XBTToken token) {
        this.token = token;
    }

    @Nonnull
    public XBTToken getToken() {
        return token;
    }

    public void setToken(XBTToken token) {
        this.token = token;
    }

    @Override
    public void serializeFromXB(XBPInputSerialHandler serializationHandler) throws XBProcessingException, IOException {
        token = serializationHandler.pullToken(token.getTokenType());
    }

    @Override
    public void serializeToXB(XBPOutputSerialHandler serializationHandler) throws XBProcessingException, IOException {
        serializationHandler.putToken(token);
    }
}
