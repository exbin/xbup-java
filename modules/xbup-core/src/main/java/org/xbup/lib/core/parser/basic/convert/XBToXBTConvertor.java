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
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.basic.XBTProducer;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 1 To level 0 convertor.
 *
 * @version 0.1 wr23.0 2013/11/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBToXBTConvertor implements XBListener, XBTProducer {

    private XBTListener target;
    private boolean expectType;
    private UBNatural group;

    @Override
    public void beginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        if (expectType && group != null) {
            target.typeXBT(new XBFixedBlockType(group.getLong(),0));
        }

        target.beginXBT(terminationMode);
        expectType = true;
        group = null;
    }

    @Override
    public void attribXB(UBNatural value) throws XBProcessingException, IOException {
        if (expectType) {
            if (group != null) {
                target.typeXBT(new XBFixedBlockType(group.getLong(),value.getLong()));
                expectType = false;
            } else {
                group = new UBNat32(value.getLong());
            }
        } else {
            target.attribXBT(value);
        }
    }

    @Override
    public void dataXB(InputStream data) throws XBProcessingException, IOException {
        if (expectType && group != null) {
            target.typeXBT(new XBFixedBlockType(group.getLong(),0));
        }

        target.dataXBT(data);
    }

    @Override
    public void endXB() throws XBProcessingException, IOException {
        if (expectType && group != null) {
            target.typeXBT(new XBFixedBlockType(group.getLong(),0));
        }

        target.endXBT();
    }

    @Override
    public void attachXBTListener(XBTListener listener) {
        target = listener;
    }

    /*public void performXBT() {
        if (expectType) {
            trigger.produceXB();
        }

        trigger.produceXB();
    }

    @Override
    public void attachXBTConsumer(XBTConsumer consumer) {
        target = consumer;
        consumer.attachXBTTriger(new XBTConsumer.XBTTrigger() {
            @Override
            public void produceXBT() {
                performXBT();
            }

            @Override
            public boolean eofXBT() {
                if (trigger != null) {
                    return trigger.eofXB();
                } else {
                    return true;
                }
            }
        });
    } */
}
