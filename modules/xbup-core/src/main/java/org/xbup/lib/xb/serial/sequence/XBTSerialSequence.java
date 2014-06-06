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
package org.xbup.lib.xb.serial.sequence;

import java.io.IOException;
import org.xbup.lib.xb.parser.XBProcessingException;

/**
 * XBUP level 1 serialization sequence access interface.
 *
 * @version 0.1 wr23.0 2014/03/03
 * @author XBUP Project (http://xbup.org)
 */
public interface XBTSerialSequence {

    /**
     * Put sequence record.
     *
     * @param sequence
     * @throws XBProcessingException
     * @throws IOException
     */
    public void sequenceXB(XBSerialSequence sequence) throws XBProcessingException, IOException;
}
