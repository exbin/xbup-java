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
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * XBUP level 1 to level 0 convertor.
 *
 * @version 0.1 wr23.0 2013/11/20
 * @author XBUP Project (http://xbup.org)
 */
public class XBTToXBConvertor implements XBTListener, XBProducer {

    private XBListener target;
    private UBNatural block;

    /**
     * Creates a new instance of XBTToXBConvertor
     * @param target target listener
     */
    public XBTToXBConvertor(XBListener target) {
        this.target = target;
        block = null;
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminatinationMode) throws XBProcessingException, IOException {
        if (block != null) {
            target.attribXB(block);
            block = null;
        }

        target.beginXB(terminatinationMode);
    }

    @Override
    public void typeXBT(XBBlockType type) throws XBProcessingException, IOException {
        if (block != null) {
            target.attribXB(block);
            block = null;
        }

        if (type instanceof XBFixedBlockType) {
            target.attribXB(((XBFixedBlockType) type).getGroupID());
            block = ((XBFixedBlockType) type).getBlockID();
        } else {
            throw new XBParseException("Unable to parse non-static block type", XBProcessingExceptionType.UNSUPPORTED);
        }
    }

    @Override
    public void attribXBT(UBNatural attribute) throws XBProcessingException, IOException {
        if (block != null) {
            target.attribXB(block);
            block = null;
        }

        target.attribXB(attribute);
    }

    @Override
    public void dataXBT(InputStream data) throws XBProcessingException, IOException {
        if (block != null) {
            target.attribXB(block);
            block = null;
        }

        target.dataXB(data);
    }

    @Override
    public void endXBT() throws XBProcessingException, IOException {
        if (block != null) {
            target.attribXB(block);
            block = null;
        }
        target.endXB();
    }

    @Override
    public void attachXBListener(XBListener listener) {
        target = listener;
    }

    public void performXB() {
        if (block != null) {
            try {
                target.attribXB(block);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBTToXBConvertor.class.getName()).log(Level.SEVERE, null, ex);
            }

            block = null;
        } else {
            // TODO trigger.produceXBT();
        }
    }

    /*
    @Override
    public void attachXBConsumer(XBConsumer consumer) {
        target = consumer;
        consumer.attachXBTriger(new XBConsumer.XBTrigger() {
            @Override
            public void produceXB() {
                performXB();
            }

            @Override
            public boolean eofXB() {
                if (trigger != null) {
                    return trigger.eofXBT();
                } else {
                    return true;
                }
            }
        });
    } */
}
