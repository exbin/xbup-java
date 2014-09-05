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
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBTFilter;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.declaration.XBContext;
import org.xbup.lib.core.block.declaration.XBDeclaration;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * Convert block types from fixed types to stand-alone types.
 *
 * @version 0.1.24 2014/09/03
 * @author XBUP Project (http://xbup.org)
 */
public class XBTTypeSeparator implements XBTFilter {

    private XBTListener listener = null;
    private List<ContextRecord> contexts = new ArrayList<>();
    private XBDeclaration declaration = null;
    private XBTListener declListener = null;

    private long documentDepth = 0;
    private int mode = 0;
    private XBBlockTerminationMode beginTerm;

    public XBTTypeSeparator() {
    }

    public XBTTypeSeparator(XBContext initialContext) {
        this();
        contexts.add(new ContextRecord(initialContext, 0));
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        listener.beginXBT(terminationMode);

        if (declListener != null) {
            declListener.beginXBT(terminationMode);
        }

        documentDepth++;
        beginTerm = terminationMode;
        mode = 1;
    }

    @Override
    public void typeXBT(XBBlockType type) throws XBProcessingException, IOException {
        listener.typeXBT(type);

        if (declListener != null) {
            declListener.typeXBT(type);
        }

        if (type.getAsBasicType() == XBBasicBlockType.DECLARATION) {
            
            documentDepth++;
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
        if (documentDepth > 0) {
            declListener.attribXBT(value);
            return;
        }

        listener.attribXBT(value);
    }

    @Override
    public void dataXBT(InputStream data) throws XBProcessingException, IOException {
        if (documentDepth > 0) {
            declListener.dataXBT(data);
            return;
        }

        listener.dataXBT(data);
    }

    @Override
    public void endXBT() throws XBProcessingException, IOException {
        if (documentDepth > 0) {
            declListener.endXBT();
            documentDepth--;

            if (documentDepth == 1) {
                mode++;
            }

            if (mode == 4) {
                mode = 0;
                documentDepth = 0;
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

    public XBContext getCurrentContext() {
        return contexts.size() > 0 ? contexts.get(contexts.size() - 1).context : null;
    }

    private class ContextRecord {

        public ContextRecord(XBContext context, long documentLevel) {
            this.context = context;
            this.documentLevel = documentLevel;
        }

        public XBContext context;
        public long documentLevel;
    }

    private enum ProcessingMode {
        
    }
}
