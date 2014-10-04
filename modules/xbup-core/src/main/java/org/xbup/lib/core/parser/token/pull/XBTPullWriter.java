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
package org.xbup.lib.core.parser.token.pull;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.block.declaration.XBContext;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.parser.token.pull.convert.XBTToXBPullConvertor;

/**
 * XBUP level 1 pull writer.
 *
 * @version 0.1.23 2014/02/22
 * @author XBUP Project (http://xbup.org)
 */
public class XBTPullWriter implements Closeable, XBTPullConsumer {

    // Structure for typeTarget
    private class TypeTargetItem {

        public int skip;
        public XBContext context;
    };

    private final XBCatalog catalog;
    private XBPullWriter pullWriter;

    /**
     * List of TypeContexts for current tree path levels.
     */
    private final SortedMap<Integer, XBContext> typeMap;
    /**
     * List of TypeContexts for later use.
     */
    private final SortedMap<Integer, TypeTargetItem> typeTarget;
//    private int currentGroup;
//    private int currentType;
//    private int attrMode;
    private int level;

    /**
     * Creates a new instance.
     *
     * @param catalog
     */
    public XBTPullWriter(XBCatalog catalog) {
        this.catalog = catalog;
        try {
            pullWriter = new XBPullWriter();
        } catch (IOException ex) {
            Logger.getLogger(XBTPullWriter.class.getName()).log(Level.SEVERE, null, ex);
        }

        typeMap = new TreeMap<>();
        typeTarget = new TreeMap<>();
    }

    /**
     * Open byte output stream.
     *
     * @param stream
     * @throws java.io.IOException
     */
    public void open(OutputStream stream) throws IOException {
        pullWriter.open(stream);
    }

    @Override
    public void close() throws IOException {
        pullWriter.close();
    }

    @Override
    public void attachXBTPullProvider(XBTPullProvider pullProvider) {
        XBTToXBPullConvertor convertor = new XBTToXBPullConvertor();
        convertor.attachXBTPullProvider(pullProvider);
        pullWriter.attachXBPullProvider(convertor);
    }
}
