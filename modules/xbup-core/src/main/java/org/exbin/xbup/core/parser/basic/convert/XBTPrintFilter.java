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
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.basic.XBTFilter;
import org.exbin.xbup.core.parser.basic.XBTListener;
import org.exbin.xbup.core.parser.token.XBAttribute;

/**
 * Default XBUP level 1 printing filter.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBTPrintFilter implements XBTFilter, XBTListener {

    private XBTListener listener = null;

    public XBTPrintFilter() {
    }

    public XBTPrintFilter(XBTListener listener) {
        this.listener = listener;
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        System.out.println("> Begin (" + terminationMode.toString() + "):");
        listener.beginXBT(terminationMode);
    }

    @Override
    public void typeXBT(XBBlockType blockType) throws XBProcessingException, IOException {
        System.out.println("  Type ");
        listener.typeXBT(blockType);
    }

    @Override
    public void attribXBT(XBAttribute value) throws XBProcessingException, IOException {
        System.out.println("  Attribute: " + value.getNaturalLong());
        listener.attribXBT(value);
    }

    @Override
    public void dataXBT(InputStream data) throws XBProcessingException, IOException {
        System.out.println("  Data:" + data.available());
        listener.dataXBT(data);
    }

    @Override
    public void endXBT() throws XBProcessingException, IOException {
        System.out.println("< End.");
        listener.endXBT();
    }

    @Override
    public void attachXBTListener(XBTListener listener) {
        this.listener = listener;
    }
}
