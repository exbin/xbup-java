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
 * @version 0.1 wr23.0 2013/11/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBTToXBDropper implements XBTListener, XBProducer {

    private XBListener target;
    private boolean typeFlag;
    private XBToken token;

    /** Creates a new instance of XBTToXBDropper */
    public XBTToXBDropper(XBListener target) {
        this.target = target;
        typeFlag = false;
        token = null;
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        flushToken();
        if (typeFlag) {
            target.attribXB(new UBNat32(0));
            typeFlag = false;
            token = new XBBeginToken(terminationMode);
        } else {
            target.beginXB(terminationMode);
        }
    }

    @Override
    public void typeXBT(XBBlockType type) throws XBProcessingException, IOException {
        flushToken();
        typeFlag = true;
    }

    @Override
    public void attribXBT(UBNatural value) throws XBProcessingException, IOException {
        flushToken();
        target.attribXB(value);
        typeFlag = false;
    }

    @Override
    public void dataXBT(InputStream data) throws XBProcessingException, IOException {
        flushToken();
        if (typeFlag) {
            target.attribXB(new UBNat32(0));
            typeFlag = false;
            token = new XBDataToken(data);
        } else {
            target.dataXB(data);
        }
    }

    @Override
    public void endXBT() throws XBProcessingException, IOException {
        flushToken();
        if (typeFlag) {
            target.attribXB(new UBNat32(0));
            typeFlag = false;
            token = new XBEndToken();
        } else {
            target.endXB();
        }
    }

    @Override
    public void attachXBListener(XBListener eventListener) {
        target = eventListener;
    }

    public void performXB() {
       if (token != null) {
            try {
                flushToken();
            } catch (XBProcessingException ex) {
                Logger.getLogger(XBTToXBDropper.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(XBTToXBDropper.class.getName()).log(Level.SEVERE, null, ex);
            }
       } else {
            // TODO trigger.produceXBT();
        }
    }

    private void flushToken() throws XBProcessingException, IOException {
        if (token != null) {
            switch (token.getTokenType()) {
                case BEGIN: {
                    target.beginXB(((XBBeginToken)token).getTerminationMode());
                    break;
                }
                case DATA: {
                    target.dataXB(((XBDataToken)token).getData());
                    break;
                }
                case END: {
                    target.endXB();
                    break;
                }
            }
            token = null;
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
