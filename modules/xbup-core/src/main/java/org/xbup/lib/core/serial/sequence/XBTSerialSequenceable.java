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
package org.xbup.lib.core.serial.sequence;

import java.io.IOException;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.child.XBTChildListener;
import org.xbup.lib.core.serial.child.XBTChildProvider;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * XBUP level 1 serialization sequence access interface.
 *
 * @version 0.1.24 2014/12/03
 * @author XBUP Project (http://xbup.org)
 */
public interface XBTSerialSequenceable extends XBTChildListener, XBTChildProvider {

    /**
     * Returns currently used serialization mode.
     *
     * @return sequencing mode depending on whether data are serialized from or
     * to token stream
     */
    public XBSerializationMode getSerializationMode();

    /**
     * Performs begin token with default termination mode behavior.
     *
     * @throws java.io.IOException
     */
    public void begin() throws XBProcessingException, IOException;

    /**
     * Performs end token.
     *
     * @throws java.io.IOException
     */
    public void end() throws XBProcessingException, IOException;

    /**
     * Performs matching to given block type.
     *
     * Reading will check if value fully equals, writting will write it.
     *
     * @param blockType block type to match to
     * @throws java.io.IOException
     */
    public void matchType(XBBlockType blockType) throws XBProcessingException, IOException;

    /**
     * Performs attribute token using attributeValue, either as source or
     * target.
     *
     * @param attributeValue
     * @throws java.io.IOException
     */
    public void attribute(UBNatural attributeValue) throws XBProcessingException, IOException;

    /**
     * Performs serialization of child.
     *
     * @param child serializable object
     * @throws java.io.IOException
     */
    public void child(XBSerializable child) throws XBProcessingException, IOException;

    /**
     * Performs serialization of child adding both attributes and child blocks.
     *
     * @param child serializable object
     * @throws java.io.IOException
     */
    public void join(XBSerializable child) throws XBProcessingException, IOException;

    /**
     * Performs serialization matching child value.
     *
     * Reading will check if value fully equals, writting will write it.
     *
     * @param child serializable object to match value to
     * @throws java.io.IOException
     */
    public void matchChild(XBSerializable child) throws XBProcessingException, IOException;

    /**
     * Appends sequence record.
     *
     * @param sequence
     * @throws XBProcessingException
     * @throws IOException
     */
    public void appendSequence(XBSerialSequence sequence) throws XBProcessingException, IOException;

}
