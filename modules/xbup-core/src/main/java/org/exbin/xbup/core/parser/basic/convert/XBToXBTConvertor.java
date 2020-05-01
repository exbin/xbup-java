/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
