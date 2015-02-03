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
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.XBProducer;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.token.XBBeginToken;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.token.XBDataToken;
import org.xbup.lib.core.parser.token.XBEndToken;
import org.xbup.lib.core.parser.token.XBToken;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 1 to level 0 convertor which drops types.
 *
 * @version 0.1.24 2014/10/04
 * @author XBUP Project (http://xbup.org)
 */
public class XBTToXBTypeDroppingConvertor implements XBTListener, XBProducer {

    private XBListener listener;
    private boolean blockTypeProcessed;
    private XBToken token;

    public XBTToXBTypeDroppingConvertor(XBListener target) {
        this.listener = target;
        blockTypeProcessed = false;
        token = null;
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        flushToken();
        if (blockTypeProcessed) {
            listener.attribXB(new UBNat32(0));
            blockTypeProcessed = false;
            token = new XBBeginToken(terminationMode);
        } else {
            listener.beginXB(terminationMode);
        }
    }

    @Override
    public void typeXBT(XBBlockType type) throws XBProcessingException, IOException {
        flushToken();
        blockTypeProcessed = true;
    }

    @Override
    public void attribXBT(UBNatural value) throws XBProcessingException, IOException {
        flushToken();
        listener.attribXB(value);
        blockTypeProcessed = false;
    }

    @Override
    public void dataXBT(InputStream data) throws XBProcessingException, IOException {
        flushToken();
        if (blockTypeProcessed) {
            listener.attribXB(new UBNat32(0));
            blockTypeProcessed = false;
            token = new XBDataToken(data);
        } else {
            listener.dataXB(data);
        }
    }

    @Override
    public void endXBT() throws XBProcessingException, IOException {
        flushToken();
        if (blockTypeProcessed) {
            listener.attribXB(new UBNat32(0));
            blockTypeProcessed = false;
            token = new XBEndToken();
        } else {
            listener.endXB();
        }
    }

    @Override
    public void attachXBListener(XBListener eventListener) {
        listener = eventListener;
    }

    public void performXB() {
        if (token != null) {
            try {
                flushToken();
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBTToXBTypeDroppingConvertor.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            // TODO trigger.produceXBT();
        }
    }

    private void flushToken() throws XBProcessingException, IOException {
        if (token != null) {
            switch (token.getTokenType()) {
                case BEGIN: {
                    listener.beginXB(((XBBeginToken) token).getTerminationMode());
                    break;
                }

                case DATA: {
                    listener.dataXB(((XBDataToken) token).getData());
                    break;
                }

                case END: {
                    listener.endXB();
                    break;
                }
            }

            token = null;
        }
    }
}
