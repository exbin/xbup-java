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
package org.xbup.lib.core.parser.token.pull.convert;

import java.io.IOException;
import java.io.InputStream;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBContext;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBTConsumer;
import org.xbup.lib.core.parser.basic.XBTFilter;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.basic.XBTProducer;
import org.xbup.lib.core.parser.basic.XBTProvider;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * XB Event Stream Encapsulation Filter
 * TODO: Upgrade for pull processing
 *
 * @version 0.1.19 2010/06/01
 * @author XBUP Project (http://xbup.org)
 */
public class XBTPullEncapsulator implements XBTFilter, XBTProducer, XBTConsumer {

    private XBTListener listener;
    private XBTProvider declProvider;
    private XBContext context;
    private long counter;

    /** Creates a new instance of XBTEncapsulator */
    public XBTPullEncapsulator(XBTProvider declProvider) {
        this.declProvider = declProvider;
        listener = null;
        counter = 0;
    }

    public XBTPullEncapsulator(XBTProvider declProvider, XBTListener target) {
        this(declProvider);
        attachXBTListener(target);
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        listener.beginXBT(terminationMode);
        counter++;
    }

    @Override
    public void typeXBT(XBBlockType type) throws XBProcessingException, IOException {
        XBFixedBlockType result = null; // TODO = getContext().toStaticType(type);
        if (result == null) {
            result = new XBFixedBlockType();
        }
        listener.typeXBT(result);
    }

    @Override
    public void attribXBT(UBNatural value) throws XBProcessingException, IOException {
        listener.attribXBT(value);
    }

    @Override
    public void dataXBT(InputStream data) throws XBProcessingException, IOException {
        listener.dataXBT(data);
    }

    @Override
    public void endXBT() throws XBProcessingException, IOException {
        if (counter == 0) {
            throw new XBProcessingException("Unexpected stream flow");
        }
        listener.endXBT();
        counter--;
//        if (counter == 0) listener.endXBT();
    }

    @Override
    public void attachXBTListener(XBTListener listener) {
        this.listener = listener;
    }

    @Override
    public void attachXBTProvider(XBTProvider provider) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public XBContext getContext() {
        return context;
    }

    public void setContext(XBContext context) {
        this.context = context;
    }
}
