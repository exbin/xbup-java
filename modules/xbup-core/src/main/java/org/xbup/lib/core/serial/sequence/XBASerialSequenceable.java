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
import org.xbup.lib.core.serial.child.XBAChildListener;
import org.xbup.lib.core.serial.child.XBAChildProvider;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * XBUP level 2 serialization sequence access interface.
 *
 * @version 0.1.24 2014/12/02
 * @author XBUP Project (http://xbup.org)
 */
public interface XBASerialSequenceable extends XBAChildListener, XBAChildProvider {

    /**
     * Returns currently used serialization mode.
     *
     * @return sequencing mode depending on whether data are serialized from or
     * to token stream
     */
    public SerializationMode getSerializationMode();

    /**
     * Performs begin token with default termination mode behavior.
     */
    public void begin();

    /**
     * Performs end token.
     */
    public void end();

    /**
     * Performs matching to given block type.
     *
     * Reading will check if value fully equals, writting will write it.
     *
     * @param blockType block type to match to
     */
    public void matchType(XBBlockType blockType);

    /**
     * Performs attribute token using attributeValue, either as source or
     * target.
     *
     * @param attributeValue
     */
    public void attribute(UBNatural attributeValue);

    /**
     * Performs serialization of child.
     *
     * @param child serializable object
     */
    public void child(XBSerializable child);

    /**
     * Performs serialization of child adding both attributes and child blocks.
     *
     * @param child serializable object
     */
    public void append(XBSerializable child);

    /**
     * Performs serialization matching child value.
     *
     * Reading will check if value fully equals, writting will write it.
     *
     * @param child serializable object to match value to
     */
    public void matchChild(XBSerializable child);

    /**
     * Appends sequence record.
     *
     * @param sequence
     * @throws XBProcessingException
     * @throws IOException
     */
    public void appendSequence(XBSerialSequence sequence) throws XBProcessingException, IOException;

    /**
     * Serialization mode to distinguish if serialization is performed from or
     * to token stream.
     */
    public enum SerializationMode {

        READ, WRITE
    }
}
