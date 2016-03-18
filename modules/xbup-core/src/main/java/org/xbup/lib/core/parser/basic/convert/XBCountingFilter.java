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
package org.xbup.lib.core.parser.basic.convert;

import java.io.IOException;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * XBUP level 0 counting filter.
 *
 * Keeps track of current depth level and provides isFinished method.
 *
 * @version 0.1.25 2015/02/06
 * @author ExBin Project (http://exbin.org)
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
