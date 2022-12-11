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
package org.exbin.xbup.core.parser.basic.convert;

import java.io.IOException;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.basic.XBListener;
import org.exbin.xbup.core.ubnumber.UBNatural;

/**
 * XBUP level 0 counting filter.
 *
 * Keeps track of current depth level and provides isFinished method.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBCountingFilter extends XBSDefaultFilter {

    private int depthLevel = 0;

    public XBCountingFilter() {
        super();
    }

    public XBCountingFilter(XBListener listener) {
        super();
        attachXBListener(listener);
    }

    @Override
    public void beginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        depthLevel++;
        super.beginXB(terminationMode);
    }

    @Override
    public void beginXB(XBBlockTerminationMode terminationMode, UBNatural blockSize) throws XBProcessingException, IOException {
        depthLevel++;
        super.beginXB(terminationMode, blockSize);
    }

    @Override
    public void endXB() throws XBProcessingException, IOException {
        depthLevel--;
        super.endXB();
    }

    /**
     * Returns block completness.
     *
     * @return true if no data passed or end of root block passed
     */
    public boolean isFinished() {
        return depthLevel == 0;
    }
}
