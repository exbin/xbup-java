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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.XBTDataToken;
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.parser.token.XBTTokenType;
import org.exbin.xbup.core.parser.token.pull.XBTPullFilter;
import org.exbin.xbup.core.parser.token.pull.XBTPullProvider;
import org.exbin.xbup.core.util.StreamUtils;

/**
 * Level 1 filter providing making accesible next token.
 *
 * This filter should be usable for level 2 expanding conversions.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBTPullPreLoader implements XBTPullFilter {

    private XBTToken nextToken;
    private int depth = 0;
    private XBTPullProvider pullProvider;
    private List<XBTToken> prefixBuffer = null;

    public XBTPullPreLoader(XBTPullProvider pullProvider) {
        attachProvider(pullProvider);
    }

    @Override
    public void attachXBTPullProvider(XBTPullProvider pullProvider) {
        attachProvider(pullProvider);
    }

    private void attachProvider(XBTPullProvider pullProvider) {
        this.pullProvider = pullProvider;
        try {
            nextToken = pullProvider.pullXBTToken();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBTPullPreLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Nonnull
    @Override
    public XBTToken pullXBTToken() throws XBProcessingException, IOException {
        XBTToken returnToken;
        if (prefixBuffer != null) {
            if (!prefixBuffer.isEmpty()) {
                return prefixBuffer.remove(0);
            }

            prefixBuffer = null;
        }
        returnToken = nextToken;
        switch (nextToken.getTokenType()) {
            case BEGIN: {
                depth++;
                break;
            }
            case END: {
                depth--;
                break;
            }
        }

        if (depth > 0) {
            nextToken = pullProvider.pullXBTToken();
            if (nextToken.getTokenType() == XBTTokenType.DATA) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                StreamUtils.copyInputStreamToOutputStream(((XBTDataToken) nextToken).getData(), stream);
                nextToken = XBTDataToken.create(new ByteArrayInputStream(stream.toByteArray()));
            }
        }

        return returnToken;
    }

    @Nullable
    public XBTToken getNextToken() {
        return nextToken;
    }

    @Nullable
    public XBTTokenType getNextTokenType() {
        return nextToken != null ? nextToken.getTokenType() : null;
    }

    public List<XBTToken> getPrefixBuffer() {
        return prefixBuffer;
    }

    public void setPrefixBuffer(List<XBTToken> prefixBuffer) {
        this.prefixBuffer = prefixBuffer;
    }

    // TODO: Make it later so, that more effective skip is used when source
    // supports it
    /**
     * Skips remaining attributes if child is requested.
     *
     * @throws java.io.IOException if input/output error
     */
    public void skipAttributes() throws XBProcessingException, IOException {
        while (getNextTokenType() == XBTTokenType.ATTRIBUTE) {
            pullXBTToken();
        }
    }

    /**
     * Skips children until end token is reached.
     *
     * @throws java.io.IOException if input/output error
     */
    public void skipChildren() throws XBProcessingException, IOException {
        while (getNextTokenType() == XBTTokenType.BEGIN) {
            skipChild();
        }
    }

    /**
     * Skips single child.
     *
     * @throws java.io.IOException if input/output error
     */
    public void skipChild() throws XBProcessingException, IOException {
        int subDepth = 0;
        do {
            XBTToken token = pullXBTToken();
            switch (token.getTokenType()) {
                case BEGIN: {
                    subDepth++;
                    break;
                }
                case END: {
                    subDepth--;
                    break;
                }
            }
        } while (subDepth > 0);
    }
}
