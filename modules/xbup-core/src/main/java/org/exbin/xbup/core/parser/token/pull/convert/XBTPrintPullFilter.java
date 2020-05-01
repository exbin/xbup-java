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

import java.io.IOException;
import javax.annotation.Nonnull;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.token.XBTAttributeToken;
import org.exbin.xbup.core.parser.token.XBTBeginToken;
import org.exbin.xbup.core.parser.token.XBTDataToken;
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.parser.token.XBTTypeToken;
import org.exbin.xbup.core.parser.token.pull.XBTPullFilter;
import org.exbin.xbup.core.parser.token.pull.XBTPullProvider;

/**
 * XBUP level 1 pull filter printing out informations about tokens.
 *
 * @version 0.2.1 2017/06/05
 * @author ExBin Project (http://exbin.org)
 */
public class XBTPrintPullFilter implements XBTPullFilter {

    @Nonnull
    private XBTPullProvider pullProvider;
    @Nonnull
    private String prefix = "";

    public XBTPrintPullFilter(@Nonnull String prefix, @Nonnull XBTPullProvider pullProvider) {
        this(pullProvider);
        this.prefix = prefix;
    }

    public XBTPrintPullFilter(@Nonnull XBTPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }

    @Override
    @Nonnull
    public XBTToken pullXBTToken() throws XBProcessingException, IOException {
        XBTToken token = pullProvider.pullXBTToken();
        switch (token.getTokenType()) {
            case BEGIN: {
                System.out.println(prefix + "> Begin (" + ((XBTBeginToken) token).getTerminationMode().toString() + "):");
                break;
            }
            case TYPE: {
                XBBlockType blockType = ((XBTTypeToken) token).getBlockType();
                System.out.println(prefix + "  Type: " + (blockType instanceof XBFixedBlockType ? "(" + ((XBFixedBlockType) blockType).getGroupID().getInt() + "," + ((XBFixedBlockType) blockType).getBlockID().getInt() + ")" : blockType.getClass().getCanonicalName()));
                break;
            }
            case ATTRIBUTE: {
                System.out.println(prefix + "  Attribute: " + ((XBTAttributeToken) token).getAttribute().getNaturalLong());
                break;
            }
            case DATA: {
                System.out.println(prefix + "  Data:" + ((XBTDataToken) token).getData().available());
                break;
            }
            case END: {
                System.out.println(prefix + "< End.");
                break;
            }
            default:
                throw new XBProcessingException("Unexpected token type", XBProcessingExceptionType.UNKNOWN);
        }

        return token;
    }

    @Override
    public void attachXBTPullProvider(@Nonnull XBTPullProvider pullProvider) {
        this.pullProvider = pullProvider;
    }
}
