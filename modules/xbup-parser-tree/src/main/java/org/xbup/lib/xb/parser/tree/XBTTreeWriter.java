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
package org.xbup.lib.xb.parser.tree;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.xb.block.XBBlockDataMode;
import org.xbup.lib.xb.block.XBTBlock;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.parser.basic.XBTListener;
import org.xbup.lib.xb.parser.basic.XBTProducer;
import org.xbup.lib.xb.parser.basic.XBTProvider;
import org.xbup.lib.xb.parser.basic.convert.XBTDefaultFilter;
import org.xbup.lib.xb.ubnumber.UBNatural;
import org.xbup.lib.xb.ubnumber.type.UBNat32;

/**
 * Basic object model parser XBUP level 1 document block / tree node.
 *
 * @version 0.1 wr23.0 2014/02/20
 * @author XBUP Project (http://xbup.org)
 */
public class XBTTreeWriter implements XBTProvider {

    private final XBTTreeNode source;
    private final boolean recursive;
    XBTProducer subProducer = null;
    private int position;

    public XBTTreeWriter(XBTTreeNode source, boolean recursive) {
        this.source = source;
        this.recursive = recursive;
    }

    public void produceXBT(XBTListener listener, boolean recursive) throws XBProcessingException, IOException {
        int pos = position;
        position++;
        if (pos == 1) {
            listener.beginXBT(source.getTerminationMode());
            return;
        }

        // TODO type
        listener.typeXBT(null);

        if (source.getDataMode() == XBBlockDataMode.DATA_BLOCK) {
            if (pos == 2) {
                listener.dataXBT(source.getData());
            } else if (pos == 3) {
                listener.endXBT();
                position = 0;
            } // else throw
            return;
        }

        pos--;
        if (pos < source.getAttributes().size()) {
            listener.attribXBT(source.getAttributes().get(pos));
            return;
        }

        pos -= source.getAttributes().size();
        if (pos < source.getChildCount()) {
            if (subProducer == null) {
                subProducer = (XBTProducer) source.getChildAt(pos).convertToXBTR(recursive);
                XBTDefaultFilter filter = new XBTDefaultFilter();
                filter.attachXBTListener(listener);
                /* TODO filter.attachXBTriger(new XBTrigger() {
                 @Override
                 public void produceXB() {
                 MyXBProducer.this.produceXB();
                 }

                 @Override
                 public boolean eofXB() {
                 return eofXB();
                 }
                 }); */
            }
            ((XBTTreeWriter) subProducer).produceXBT(listener);
            position--;
            return;
        }

        if (pos == source.getChildCount()) {
            listener.endXBT();
            position = 0;
        }
    }

    @Override
    public void produceXBT(XBTListener listener) {
        try {
            produceXBT(listener, true);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBTreeNode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // @Override
    public void attachXBTListener(XBTListener listener) {
        try {
            generateXBT(listener, true);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBTreeNode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void generateXBT(XBTListener listener, boolean recursive) throws XBProcessingException, IOException {
        listener.beginXBT(source.getTerminationMode());
        if (source.getDataMode() == XBBlockDataMode.DATA_BLOCK) {
            listener.dataXBT(source.getData());
        } else {
            listener.typeXBT(source.getBlockType());
            if (source.getAttributes() != null) {
                Iterator<UBNatural> iter = source.getAttributes().iterator();
                UBNatural attrib;
                while (iter.hasNext()) {
                    attrib = new UBNat32(iter.next());
                    listener.attribXBT(attrib);
                }
            }
            if (recursive & (source.getChildren() != null)) {
                Iterator<XBTBlock> iter = source.getChildren().iterator();
                while (iter.hasNext()) {
                    // TODO ((XBTTreeNode) iter.next()).toXBTEvents(listener);
                }
            }
        }

        listener.endXBT();
    }
}
