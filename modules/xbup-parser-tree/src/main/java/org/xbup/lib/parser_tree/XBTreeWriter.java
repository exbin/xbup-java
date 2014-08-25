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
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.block.XBBlock;
import org.xbup.lib.core.block.XBBlockDataMode;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.XBProducer;
import org.xbup.lib.core.parser.basic.XBProvider;
import org.xbup.lib.core.parser.basic.convert.XBDefaultFilter;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Basic object model parser XBUP level 0 document block / tree node.
 *
 * @version 0.1.23 2014/02/11
 * @author XBUP Project (http://xbup.org)
 */
public class XBTreeWriter implements XBProvider {

    private final XBTreeNode source;
    private final boolean recursive;
    XBProducer subProducer = null;
    private int position;

    public XBTreeWriter(XBTreeNode source, boolean recursive) {
        this.source = source;
        this.recursive = recursive;
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
        if (pos < source.getAttributes().size()) {
            listener.attribXB(source.getAttributes().get(pos));
            return;
        }

        pos -= source.getAttributes().size();
        if (pos < source.getChildCount()) {
            if (subProducer == null) {
                subProducer = (XBProducer) source.getChildAt(pos).convertToXBR(recursive);
                XBDefaultFilter filter = new XBDefaultFilter();
                filter.attachXBListener(listener);
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

            ((XBTreeWriter) subProducer).produceXB(listener);
            position--;
            return;
        }

        if (pos == source.getChildCount()) {
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
            if (source.getAttributes() != null) {
                Iterator<UBNatural> iter = source.getAttributes().iterator();
                UBNatural attrib;
                while (iter.hasNext()) {
                    attrib = new UBNat32(iter.next());
                    listener.attribXB(attrib);
                }
            }

            if (recursive && (source.getChildren() != null)) {
                Iterator<XBBlock> iter = source.getChildren().iterator();
                while (iter.hasNext()) {
                    // TODO ((XBTreeNode) iter.next()).convertToXBR(recursive).attachXBListener(listener);
                }
            }
        }

        listener.endXB();
    }
}
