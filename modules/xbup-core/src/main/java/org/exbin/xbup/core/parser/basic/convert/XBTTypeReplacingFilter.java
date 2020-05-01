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
package org.exbin.xbup.core.parser.basic.convert;

import java.io.IOException;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.basic.XBTListener;

/**
 * This level 1 filter replaces type single time.
 *
 * @version 0.1.25 2015/02/05
 * @author ExBin Project (http://exbin.org)
 */
public class XBTTypeReplacingFilter extends XBTDefaultFilter {

    private boolean typeReplaced = false;
    private XBBlockType replacementBlockType = null;

    public XBTTypeReplacingFilter(XBBlockType blockType) {
        this.replacementBlockType = blockType;
    }

    public XBTTypeReplacingFilter(XBBlockType blockType, XBTListener listener) {
        this(blockType);
        attachXBTListener(listener);
    }

    @Override
    public void typeXBT(XBBlockType blockType) throws XBProcessingException, IOException {
        if (!typeReplaced) {
            super.typeXBT(replacementBlockType);
            typeReplaced = true;
        }

        super.typeXBT(blockType);
    }
}
