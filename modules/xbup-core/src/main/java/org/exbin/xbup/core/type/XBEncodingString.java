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
package org.exbin.xbup.core.type;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.serial.child.XBChildInputSerialHandler;
import org.exbin.xbup.core.serial.child.XBChildOutputSerialHandler;
import org.exbin.xbup.core.serial.child.XBChildSerializable;
import org.exbin.xbup.core.serial.child.XBTChildInputSerialHandler;
import org.exbin.xbup.core.serial.child.XBTChildOutputSerialHandler;
import org.exbin.xbup.core.serial.child.XBTChildSerializable;
import org.exbin.xbup.core.serial.param.XBPSequenceSerialHandler;
import org.exbin.xbup.core.serial.param.XBPSequenceSerializable;
import org.exbin.xbup.core.util.StreamUtils;

/**
 * Encapsulation class for String using given charset.
 *
 * @version 0.1.24 2015/01/28
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBEncodingString implements XBPSequenceSerializable {

    private String value;
    private XBCharset charset;
    static long[] XBUP_FORMATREV_CATALOGPATH = {1, 3, 1, 2, 0, 0};
    static long[] XBUP_BLOCKREV_CATALOGPATH = {1, 3, 1, 2, 0, 0};

    public XBEncodingString() {
        this.value = "";
        charset = new XBCharset();
    }

    public XBEncodingString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Charset getCharset() {
        return charset.getCharset();
    }

    public void setCharset(Charset charset) {
        this.charset.setCharset(charset);
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBDeclBlockType(XBUP_BLOCKREV_CATALOGPATH));
        serial.join(charset);
        serial.consist(new DataBlockSerializator());
        serial.end();
    }

    @ParametersAreNonnullByDefault
    public class DataBlockSerializator implements XBChildSerializable, XBTChildSerializable {

        @Override
        public void serializeFromXB(XBChildInputSerialHandler serial) throws XBProcessingException, IOException {
            serial.begin();
            InputStream source = serial.nextData();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try {
                StreamUtils.copyInputStreamToOutputStream(source, stream);
            } catch (IOException ex) {
                Logger.getLogger(XBEncodingString.class.getName()).log(Level.SEVERE, null, ex);
            }

            setValue(new String(stream.toByteArray(), charset.getCharset()));
            serial.end();
        }

        @Override
        public void serializeToXB(XBChildOutputSerialHandler serial) throws XBProcessingException, IOException {
            serial.begin(XBBlockTerminationMode.SIZE_SPECIFIED);
            if (getValue() != null) {
                serial.addData(new ByteArrayInputStream(getValue().getBytes(charset.getCharset())));
            } else {
                serial.addData(new ByteArrayInputStream(new byte[0]));
            }

            serial.end();
        }

        @Override
        public void serializeFromXB(XBTChildInputSerialHandler serial) throws XBProcessingException, IOException {
            serial.pullBegin();
            InputStream source = serial.pullData();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try {
                StreamUtils.copyInputStreamToOutputStream(source, stream);
            } catch (IOException ex) {
                Logger.getLogger(XBEncodingString.class.getName()).log(Level.SEVERE, null, ex);
            }

            setValue(new String(stream.toByteArray(), charset.getCharset()));
            serial.pullEnd();
        }

        @Override
        public void serializeToXB(XBTChildOutputSerialHandler serial) throws XBProcessingException, IOException {
            serial.putBegin(XBBlockTerminationMode.SIZE_SPECIFIED);
            if (getValue() != null) {
                serial.putData(new ByteArrayInputStream(getValue().getBytes(charset.getCharset())));
            } else {
                serial.putData(new ByteArrayInputStream(new byte[0]));
            }

            serial.putEnd();
        }
    }
}
