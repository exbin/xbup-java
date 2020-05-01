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
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.basic.XBTListener;
import org.exbin.xbup.core.ubnumber.UBNatural;

/**
 * XBUP level 1 counting filter.
 *
 * Keeps track of current depth level and provides isFinished method.
 *
 * @version 0.1.25 2015/02/06
 * @author ExBin Project (http://exbin.org)
 */
public class XBTCountingFilter extends XBTSDefaultFilter {

    private int level = 0;

    public XBTCountingFilter() {
        super();
    }

    public XBTCountingFilter(XBTListener listener) {
        super();
        attachXBTListener(listener);
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        level++;
        super.beginXBT(terminationMode);
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode, UBNatural blockSize) throws XBProcessingException, IOException {
        level++;
        super.beginXBT(terminationMode, blockSize);
    }

    @Override
    public void endXBT() throws XBProcessingException, IOException {
        level--;
        super.endXBT();
    }

    /**
     * Reports block completness.
     *
     * @return true if no data passed or end of root block passed
     */
    public boolean isFinished() {
        return level == 0;
    }
}
