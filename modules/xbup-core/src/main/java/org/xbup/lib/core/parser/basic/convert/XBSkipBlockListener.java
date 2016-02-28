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
import java.io.InputStream;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.token.XBAttribute;

/**
 * Level 0 counting listener reporting one skipped block.
 *
 * @version 0.1.25 2015/02/06
 * @author ExBin Project (http://exbin.org)
 */
public class XBSkipBlockListener implements XBListener {

    private boolean isSkipped = false;
    private long level = 0;

    public XBSkipBlockListener() {
        isSkipped = true;
    }

    @Override
    public void beginXB(XBBlockTerminationMode terminationMode) throws XBParseException, IOException {
        if (isSkipped) {
            throw new XBParseException("Unexpected token after block skip", XBProcessingExceptionType.WRITING_AFTER_END);
        }

        level++;
    }

    @Override
    public void attribXB(XBAttribute value) throws XBParseException, IOException {
        if (isSkipped) {
            throw new XBParseException("Unexpected token after block skip", XBProcessingExceptionType.WRITING_AFTER_END);
        }
    }

    @Override
    public void dataXB(InputStream data) throws XBParseException, IOException {
        if (isSkipped) {
            throw new XBParseException("Unexpected token after block skip", XBProcessingExceptionType.WRITING_AFTER_END);
        }
    }

    @Override
    public void endXB() throws XBParseException, IOException {
        if (isSkipped) {
            throw new XBParseException("Unexpected token after block skip", XBProcessingExceptionType.WRITING_AFTER_END);
        }

        if (level == 0) {
            isSkipped = true;
        } else {
            level--;
        }
    }

    public boolean isSkipped() {
        return isSkipped;
    }
}
