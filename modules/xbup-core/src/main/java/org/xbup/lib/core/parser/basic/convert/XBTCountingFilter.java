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
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.basic.XBTFilter;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * XBUP level 1 counting filter.
 *
 * Keeps track of current depth level and provides isFinished method.
 *
 * @version 0.1.23 2014/10/04
 * @author XBUP Project (http://xbup.org)
 */
public class XBTCountingFilter implements XBTFilter {

    private int level = 0;
    private XBTListener listener;

    public XBTCountingFilter() {
    }

    public XBTCountingFilter(XBTListener listener) {
        this();
        this.listener = listener;
    }

    @Override
    public void attachXBTListener(XBTListener listener) {
        this.listener = listener;
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        level++;
        listener.beginXBT(terminationMode);
    }

    @Override
    public void typeXBT(XBBlockType blockType) throws XBProcessingException, IOException {
        listener.typeXBT(blockType);
    }

    @Override
    public void attribXBT(UBNatural attribute) throws XBProcessingException, IOException {
        listener.attribXBT(attribute);
    }

    @Override
    public void dataXBT(InputStream data) throws XBProcessingException, IOException {
        listener.dataXBT(data);
    }

    @Override
    public void endXBT() throws XBProcessingException, IOException {
        level--;
        listener.endXBT();
    }

    /**
     * Block completness.
     *
     * @return true if no data passed or end of root block passed
     */
    public boolean isFinished() {
        return level == 0;
    }
}
