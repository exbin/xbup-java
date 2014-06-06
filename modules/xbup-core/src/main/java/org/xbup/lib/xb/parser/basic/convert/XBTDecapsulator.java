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
package org.xbup.lib.xb.parser.basic.convert;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.xbup.lib.xb.block.XBBlockType;
import org.xbup.lib.xb.block.declaration.XBDeclaration;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.parser.basic.XBTFilter;
import org.xbup.lib.xb.parser.basic.XBTListener;
import org.xbup.lib.xb.block.XBBlockTerminationMode;
import org.xbup.lib.xb.serial.XBSerialHandler;
import org.xbup.lib.xb.serial.XBSerialMethod;
import org.xbup.lib.xb.serial.XBSerializable;
import org.xbup.lib.xb.serial.XBSerializationFromXB;
import org.xbup.lib.xb.serial.XBSerializationType;
import org.xbup.lib.xb.ubnumber.UBNatural;

/**
 * Decode specifications head for events and links relevant block types to it.
 *
 * @version 0.1 wr23.0 2013/11/19
 * @author XBUP Project (http://xbup.org)
 */
public class XBTDecapsulator implements XBTFilter {

    private XBTListener listener;
    private final XBTListener declListener;
    private XBDeclaration declaration;

    private long depth;
    private int mode;
    private XBBlockTerminationMode beginTerm;
    
    public XBTDecapsulator() {
        declaration = new XBDeclaration();
        declaration.setRootNode(new XBSerializationFromXB(new XBSerializable() {

            @Override
            public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }, 0));

        declListener = declaration.convertFromXBT();
        mode = 0;
        depth = 0;
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        if (depth > 0) {
            declListener.beginXBT(terminationMode);
            depth++;
            return;
        }

        beginTerm = terminationMode;
        mode = 1;
    }

    @Override
    public void typeXBT(XBBlockType type) throws XBProcessingException, IOException {
        if (depth > 0) {
            declListener.typeXBT(type);
            return;
        }

        if ((type.getGroupID().getInt() == 0)&&(type.getBlockID().getInt() == 1)) {
            depth++;
            mode = 2;
            declListener.beginXBT(beginTerm);
            declListener.typeXBT(type);
        } else {
            listener.beginXBT(beginTerm);
            listener.typeXBT(type);
        }
    }

    @Override
    public void attribXBT(UBNatural value) throws XBProcessingException, IOException {
        if (depth > 0) {
            declListener.attribXBT(value);
            return;
        }

        listener.attribXBT(value);
    }

    @Override
    public void dataXBT(InputStream data) throws XBProcessingException, IOException {
        if (depth > 0) {
            declListener.dataXBT(data);
            return;
        }

        listener.dataXBT(data);
    }

    @Override
    public void endXBT() throws XBProcessingException, IOException {
        if (depth > 0) {
            declListener.endXBT();
            depth--;

            if (depth == 1) {
                mode++;
            }

            if (mode == 4) {
                mode = 0;
                depth = 0;
            } else {
                return;
            }
        }

        listener.endXBT();
    }

    @Override
    public void attachXBTListener(XBTListener listener) {
        this.listener = listener;
    }

    public XBDeclaration getDeclaration() {
        return declaration;
    }
}
