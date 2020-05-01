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
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.parser.XBParseException;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.XBListener;
import org.exbin.xbup.core.parser.basic.XBProducer;
import org.exbin.xbup.core.parser.basic.XBSListener;
import org.exbin.xbup.core.parser.basic.XBTListener;
import org.exbin.xbup.core.parser.basic.XBTSListener;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.ubnumber.UBNatural;

/**
 * XBUP level 1 to level 0 convertor.
 *
 * @version 0.1.24 2014/11/27
 * @author ExBin Project (http://exbin.org)
 */
public class XBTToXBConvertor implements XBTListener, XBTSListener, XBProducer {

    private XBListener listener;
    private UBNatural blockIdAttribute;

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
        if (blockIdAttribute != null) {
            listener.attribXB(blockIdAttribute);
            blockIdAttribute = null;
        }

        listener.beginXB(terminationMode);
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode, UBNatural blockSize) throws XBProcessingException, IOException {
        if (blockIdAttribute != null) {
            listener.attribXB(blockIdAttribute);
            blockIdAttribute = null;
        }

        if (listener instanceof XBSListener) {
            ((XBSListener) listener).beginXB(terminationMode, blockSize);
        } else {
            listener.beginXB(terminationMode);
        }
    }

    @Override
    public void typeXBT(XBBlockType type) throws XBProcessingException, IOException {
        if (blockIdAttribute != null) {
            listener.attribXB(blockIdAttribute);
            blockIdAttribute = null;
        }

        if (type instanceof XBFixedBlockType) {
            listener.attribXB(((XBFixedBlockType) type).getGroupID());
            blockIdAttribute = ((XBFixedBlockType) type).getBlockID();
        } else {
            throw new XBParseException("Unable to parse non-static block type", XBProcessingExceptionType.UNSUPPORTED);
        }
    }

    @Override
    public void attribXBT(XBAttribute attribute) throws XBProcessingException, IOException {
        if (blockIdAttribute != null) {
            listener.attribXB(blockIdAttribute);
            blockIdAttribute = null;
        }

        listener.attribXB(attribute);
    }

    @Override
    public void dataXBT(InputStream data) throws XBProcessingException, IOException {
        if (blockIdAttribute != null) {
            listener.attribXB(blockIdAttribute);
            blockIdAttribute = null;
        }

        listener.dataXB(data);
    }

    @Override
    public void endXBT() throws XBProcessingException, IOException {
        if (blockIdAttribute != null) {
            listener.attribXB(blockIdAttribute);
            blockIdAttribute = null;
        }

        listener.endXB();
    }

    @Override
    public void attachXBListener(XBListener listener) {
        this.listener = listener;
    }
}
