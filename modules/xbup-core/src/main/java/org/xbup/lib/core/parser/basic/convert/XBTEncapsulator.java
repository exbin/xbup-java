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
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBContext;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.basic.XBTFilter;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * XBUP level 1 encapsulation filter.
 *
 * @version 0.1.23 2014/03/05
 * @author XBUP Project (http://xbup.org)
 */
public class XBTEncapsulator implements XBTFilter {

    private XBTListener listener;
    private final XBCatalog catalog;
    private XBContext context;
    private long counter;

    /**
     * Creates a new instance of XBTEncapsulator.
     * 
     * @param context
     * @param catalog 
     */
    public XBTEncapsulator(XBContext context, XBCatalog catalog) {
        this.context = context;
        this.catalog = catalog;
        listener = null;
        counter = 0;
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        if (counter == 0) {
            XBTStubCountingListener countingListener = new XBTStubCountingListener(listener);
            do {
                // TODO declProvider.produceXBT(countingListener);
            } while (!countingListener.isFinished());
        }
        
        listener.beginXBT(terminationMode);
        counter++;
    }

    @Override
    public void typeXBT(XBBlockType type) throws XBProcessingException, IOException {
        XBFixedBlockType result = catalog.findFixedType(context, type);
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
            throw new XBProcessingException("Unexpected stream flow", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
        }

        listener.endXBT();
        counter--;
        if (counter == 0) {
            listener.endXBT();
        }
    }

    @Override
    public void attachXBTListener(XBTListener listener) {
        this.listener = listener;
    }

    public XBContext getContext() {
        return context;
    }

    public void setContext(XBContext context) {
        this.context = context;
    }

    public class XBTStubCountingListener implements XBTListener {

        private int level;
        private final XBTListener listener;

        public XBTStubCountingListener(XBTListener listener) {
            level = 0;
            this.listener = listener;
        }

        @Override
        public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
            level++;
            listener.beginXBT(terminationMode);
        }

        @Override
        public void typeXBT(XBBlockType blockType) throws XBProcessingException, IOException {
            listener.typeXBT(blockType);
        }

        @Override
        public void attribXBT(UBNatural attribute) throws XBProcessingException, IOException {
            listener.attribXBT(attribute);
        }

        @Override
        public void dataXBT(InputStream data) throws XBProcessingException, IOException {
            listener.dataXBT(data);
        }

        @Override
        public void endXBT() throws XBProcessingException, IOException {
            level--;
            if (level > 0) {
                listener.endXBT();
            }
        }

        public boolean isFinished() {
            return level == 0;
        }
    }
}
