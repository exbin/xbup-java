/*
 * Copyright (C) ExBin Project
 *
 * This application or library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This application or library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along this application.  If not, see <http://www.gnu.org/licenses/>.
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
