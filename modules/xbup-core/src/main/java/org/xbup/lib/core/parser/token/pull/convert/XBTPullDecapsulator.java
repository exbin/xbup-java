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

import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.declaration.XBDeclaration;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.basic.XBTProvider;
import org.xbup.lib.core.parser.basic.XBTProviderFilter;

/**
 * Decode specifications head for events and links relevant block types to it.
 *
 * @version 0.1 wr23.0 2013/11/18
 * @author XBUP Project (http://xbup.org)
 */
public class XBTPullDecapsulator implements XBTProviderFilter {

    private XBTProvider provider;
    private XBTListener listener;
    private XBTListener declListener;
    private XBDeclaration declaration;

    private long depth;
    private int mode;
    private boolean finishFlag;
    private boolean beginTerm;
    private XBBlockType type = null;

    /** Creates a new instance of XBTDecapsulator */
    public XBTPullDecapsulator() {
        declaration = new XBDeclaration();

        declListener = declaration.convertFromXBT();
        mode = 0;
        depth = 0;
    }

    public void attachXBTListener(XBTListener listener) {
        this.listener = listener;
    }

    /**
     * @return the declaration
     */
    public XBDeclaration getDeclaration() {
        return declaration;
    }

    @Override
    public void produceXBT(XBTListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
        /* if (depth > 0) {
            declProvider.produceXBT();
        } else {
            if (type != null) {
                try {
                    listener.typeXBT(type);
                    type = null;
                } catch (XBProcessingException ex) {
                    Logger.getLogger(XBTPullDecapsulator.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(XBTPullDecapsulator.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                trigger.produceXBT();
                while (!finishFlag) {
                    trigger.produceXBT();
                }
            }
        } */
    }

    @Override
    public void attachXBTProvider(XBTProvider provider) {
        this.provider = provider;
    }
}
