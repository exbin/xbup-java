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
 * Convert stand-alone block types to fixed types.
 *
 * @version 0.1.23 2014/10/04
 * @author XBUP Project (http://xbup.org)
 */
public class XBTTypeReliantor implements XBTFilter {

    private XBTListener listener;
    private final XBCatalog catalog;
    private XBContext context;
    private long counter;

    /**
     * Creates a new instance.
     *
     * @param context initial context
     * @param catalog catalog used for catalog types
     */
    public XBTTypeReliantor(XBContext context, XBCatalog catalog) {
        this.context = context;
        this.catalog = catalog;
        listener = null;
        counter = 0;
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        if (counter == 0) {
            XBTCountingFilter countingFilter = new XBTCountingFilter(listener);
            do {
                // TODO declProvider.produceXBT(countingListener);
            } while (!countingFilter.isFinished());
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
}
