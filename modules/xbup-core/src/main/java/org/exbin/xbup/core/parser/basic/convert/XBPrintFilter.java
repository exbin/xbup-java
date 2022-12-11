/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.basic.XBFilter;
import org.exbin.xbup.core.parser.basic.XBListener;
import org.exbin.xbup.core.parser.basic.XBSListener;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.ubnumber.UBNatural;

/**
 * Default XBUP level 0 printing filter.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBPrintFilter implements XBFilter, XBSListener {

    private XBListener listener = null;

    public XBPrintFilter() {
    }

    public XBPrintFilter(XBListener listener) {
        this.listener = listener;
    }

    @Override
    public void beginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        System.out.println("> Begin (" + terminationMode.toString() + "):");
        if (listener != null) {
            listener.beginXB(terminationMode);
        }
    }

    @Override
    public void beginXB(XBBlockTerminationMode terminationMode, UBNatural blockSize) throws XBProcessingException, IOException {
        System.out.println("> Begin (" + terminationMode.toString() + "):");
        if (listener instanceof XBSListener) {
            ((XBSListener) listener).beginXB(terminationMode, blockSize);
        } else if (listener != null) {
            listener.beginXB(terminationMode);
        }
    }

    @Override
    public void attribXB(XBAttribute value) throws XBProcessingException, IOException {
        System.out.println("  Attribute: " + value.getNaturalLong());
        if (listener != null) {
            listener.attribXB(value);
        }
    }

    @Override
    public void dataXB(InputStream data) throws XBProcessingException, IOException {
        System.out.println("  Data:" + data.available());
        if (listener != null) {
            listener.dataXB(data);
        }
    }

    @Override
    public void endXB() throws XBProcessingException, IOException {
        System.out.println("< End.");
        if (listener != null) {
            listener.endXB();
        }
    }

    @Override
    public void attachXBListener(XBListener listener) {
        this.listener = listener;
    }
}
