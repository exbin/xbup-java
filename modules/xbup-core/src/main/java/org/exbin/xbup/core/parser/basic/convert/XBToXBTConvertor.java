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
package org.exbin.xbup.core.parser.basic.convert;

import java.io.IOException;
import java.io.InputStream;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.basic.XBListener;
import org.exbin.xbup.core.parser.basic.XBSListener;
import org.exbin.xbup.core.parser.basic.XBTListener;
import org.exbin.xbup.core.parser.basic.XBTProducer;
import org.exbin.xbup.core.parser.basic.XBTSListener;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.ubnumber.UBNatural;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * XBUP level 1 To level 0 convertor.
 *
 * @version 0.1.25 2015/08/14
 * @author ExBin Project (http://exbin.org)
 */
public class XBToXBTConvertor implements XBListener, XBSListener, XBTProducer {

    private XBTListener listener;
    private boolean blockTypeProcessed = true;
    private UBNatural groupId;

    public XBToXBTConvertor(XBTListener listener) {
        this.listener = listener;
    }

    @Override
    public void beginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        if (!blockTypeProcessed) {
            listener.typeXBT(new XBFixedBlockType(groupId != null ? groupId.getLong() : 0, 0));
        }

        listener.beginXBT(terminationMode);
        blockTypeProcessed = false;
        groupId = null;
    }

    @Override
    public void beginXB(XBBlockTerminationMode terminationMode, UBNatural blockSize) throws XBProcessingException, IOException {
        if (!blockTypeProcessed) {
            listener.typeXBT(new XBFixedBlockType(groupId != null ? groupId.getLong() : 0, 0));
        }

        if (listener instanceof XBTSListener) {
            ((XBTSListener) listener).beginXBT(terminationMode, blockSize);
        } else {
            listener.beginXBT(terminationMode);
        }
        blockTypeProcessed = false;
        groupId = null;
    }

    @Override
    public void attribXB(XBAttribute value) throws XBProcessingException, IOException {
        if (blockTypeProcessed) {
            listener.attribXBT(value);
        } else {
            if (groupId != null) {
                listener.typeXBT(new XBFixedBlockType(groupId.getLong(), value.getNaturalLong()));
                blockTypeProcessed = true;
            } else {
                groupId = new UBNat32(value.getNaturalLong());
            }
        }
    }

    @Override
    public void dataXB(InputStream data) throws XBProcessingException, IOException {
        if (!blockTypeProcessed && groupId != null) {
            listener.typeXBT(new XBFixedBlockType(groupId != null ? groupId.getLong() : 0, 0));
        }
        listener.dataXBT(data);
        blockTypeProcessed = true;
    }

    @Override
    public void endXB() throws XBProcessingException, IOException {
        if (!blockTypeProcessed) {
            listener.typeXBT(new XBFixedBlockType(groupId != null ? groupId.getLong() : 0, 0));
            blockTypeProcessed = true;
        }

        listener.endXBT();
    }

    @Override
    public void attachXBTListener(XBTListener listener) {
        this.listener = listener;
    }
}
