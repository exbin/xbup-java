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
import org.xbup.lib.core.parser.basic.XBSListener;
import org.xbup.lib.core.parser.basic.XBTSListener;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 1 To level 0 convertor.
 *
 * @version 0.1.25 2015/08/14
 * @author XBUP Project (http://xbup.org)
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
