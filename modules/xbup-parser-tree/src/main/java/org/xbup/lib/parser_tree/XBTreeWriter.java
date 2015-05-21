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
package org.xbup.lib.parser_tree;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.block.XBBlock;
import org.xbup.lib.core.block.XBBlockDataMode;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.XBProducer;
import org.xbup.lib.core.parser.basic.XBProvider;
import org.xbup.lib.core.parser.basic.convert.XBDefaultFilter;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Basic object model parser XBUP level 0 document block / tree node.
 *
 * @version 0.1.24 2014/10/19
 * @author XBUP Project (http://xbup.org)
 */
public class XBTreeWriter implements XBProvider {

    private final XBBlock source;
    XBProducer subProducer = null;
    private int position;

    public XBTreeWriter(XBBlock source) {
        this.source = source;
    }

    public void produceXB(XBListener listener, boolean recursive) throws XBProcessingException, IOException {
        int pos = position;
        position++;
        if (pos == 1) {
            listener.beginXB(source.getTerminationMode());
            return;
        }

        if (source.getDataMode() == XBBlockDataMode.DATA_BLOCK) {
            if (pos == 2) {
                listener.dataXB(source.getData());
            } else if (pos == 3) {
                listener.endXB();
                position = 0;
            } // else throw
            return;
        }

        pos--;
        if (pos < source.getAttributesCount()) {
            listener.attribXB(source.getAttributeAt(pos));
            return;
        }

        pos -= source.getAttributesCount();
        if (pos < source.getChildrenCount()) {
            if (subProducer == null) {
                subProducer = (XBProducer) new XBTreeWriter(source.getChildAt(pos));
                XBDefaultFilter filter = new XBDefaultFilter();
                filter.attachXBListener(listener);
            }

            ((XBTreeWriter) subProducer).produceXB(listener);
            position--;
            return;
        }

        if (pos == source.getChildrenCount()) {
            listener.endXB();
            position = 0;
        }
    }

    @Override
    public void produceXB(XBListener listener) {
        try {
            produceXB(listener, true);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBTreeNode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // @Override
    public void attachXBListener(XBListener listener) {
        try {
            generateXB(listener, true);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBTreeNode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void generateXB(XBListener listener, boolean recursive) throws XBProcessingException, IOException {
        listener.beginXB(source.getTerminationMode());
        if (source.getDataMode() == XBBlockDataMode.DATA_BLOCK) {
            listener.dataXB(source.getData());
        } else {
            XBAttribute[] attributes = source.getAttributes();
            for (XBAttribute attribute : attributes) {
                listener.attribXB(new UBNat32(attribute.getNaturalLong()));
            }

            if (recursive && (source.getChildren() != null)) {
                XBBlock[] children = source.getChildren();
                for (XBBlock child : children) {
                    XBTreeWriter subWriter = new XBTreeWriter(child);
                    subWriter.attachXBListener(listener);
                }
            }
        }

        listener.endXB();
    }
}
