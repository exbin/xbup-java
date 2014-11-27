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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.XBProducer;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.basic.XBSListener;
import org.xbup.lib.core.parser.basic.XBTSListener;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * XBUP level 1 to level 0 convertor.
 *
 * @version 0.1.24 2014/11/27
 * @author XBUP Project (http://xbup.org)
 */
public class XBTToXBConvertor implements XBTListener, XBTSListener, XBProducer {

    private XBListener listener;
    private UBNatural block;

    public XBTToXBConvertor() {
    }

    /**
     * Creates a new instance of XBTToXBConvertor
     *
     * @param listener target listener
     */
    public XBTToXBConvertor(XBListener listener) {
        this();
        this.listener = listener;
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        if (block != null) {
            listener.attribXB(block);
            block = null;
        }

        listener.beginXB(terminationMode);
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode, UBNatural blockSize) throws XBProcessingException, IOException {
        if (block != null) {
            listener.attribXB(block);
            block = null;
        }

        if (listener instanceof XBSListener) {
            ((XBSListener) listener).beginXB(terminationMode, blockSize);
        } else {
            listener.beginXB(terminationMode);
        }
    }

    @Override
    public void typeXBT(XBBlockType type) throws XBProcessingException, IOException {
        if (block != null) {
            listener.attribXB(block);
            block = null;
        }

        if (type instanceof XBFixedBlockType) {
            listener.attribXB(((XBFixedBlockType) type).getGroupID());
            block = ((XBFixedBlockType) type).getBlockID();
        } else {
            throw new XBParseException("Unable to parse non-static block type", XBProcessingExceptionType.UNSUPPORTED);
        }
    }

    @Override
    public void attribXBT(UBNatural attribute) throws XBProcessingException, IOException {
        if (block != null) {
            listener.attribXB(block);
            block = null;
        }

        listener.attribXB(attribute);
    }

    @Override
    public void dataXBT(InputStream data) throws XBProcessingException, IOException {
        if (block != null) {
            listener.attribXB(block);
            block = null;
        }

        listener.dataXB(data);
    }

    @Override
    public void endXBT() throws XBProcessingException, IOException {
        if (block != null) {
            listener.attribXB(block);
            block = null;
        }

        listener.endXB();
    }

    @Override
    public void attachXBListener(XBListener listener) {
        this.listener = listener;
    }

    public void performXB() {
        if (block != null) {
            try {
                listener.attribXB(block);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBTToXBConvertor.class.getName()).log(Level.SEVERE, null, ex);
            }

            block = null;
        } else {
            // TODO trigger.produceXBT();
        }
    }
}
