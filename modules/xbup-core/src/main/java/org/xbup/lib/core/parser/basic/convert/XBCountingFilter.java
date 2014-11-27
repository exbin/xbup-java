/*
 * Copyright (C) XBUP Project
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
import java.io.InputStream;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.basic.XBFilter;
import org.xbup.lib.core.parser.basic.XBSListener;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * XBUP level 0 counting filter.
 *
 * Keeps track of current depth level and provides isFinished method.
 *
 * @version 0.1.24 2014/11/27
 * @author XBUP Project (http://xbup.org)
 */
public class XBCountingFilter implements XBFilter, XBSListener {

    private int depthLevel = 0;
    private XBListener listener;

    public XBCountingFilter() {
    }

    public XBCountingFilter(XBListener listener) {
        this();
        this.listener = listener;
    }

    @Override
    public void attachXBListener(XBListener listener) {
        this.listener = listener;
    }

    @Override
    public void beginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        depthLevel++;
        listener.beginXB(terminationMode);
    }

    @Override
    public void beginXB(XBBlockTerminationMode terminationMode, UBNatural blockSize) throws XBProcessingException, IOException {
        depthLevel++;
        if (listener instanceof XBSListener) {
            ((XBSListener) listener).beginXB(terminationMode, blockSize);
        } else {
            listener.beginXB(terminationMode);
        }
    }

    @Override
    public void attribXB(UBNatural attribute) throws XBProcessingException, IOException {
        listener.attribXB(attribute);
    }

    @Override
    public void dataXB(InputStream data) throws XBProcessingException, IOException {
        listener.dataXB(data);
    }

    @Override
    public void endXB() throws XBProcessingException, IOException {
        depthLevel--;
        listener.endXB();
    }

    /**
     * Block completness.
     *
     * @return true if no data passed or end of root block passed
     */
    public boolean isFinished() {
        return depthLevel == 0;
    }
}
