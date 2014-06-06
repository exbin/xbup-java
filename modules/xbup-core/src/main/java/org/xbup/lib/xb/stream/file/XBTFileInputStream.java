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
package org.xbup.lib.xb.stream.file;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.xb.catalog.XBCatalog;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.parser.basic.XBTListener;
import org.xbup.lib.xb.parser.token.XBTToken;
import org.xbup.lib.xb.parser.token.convert.XBTListenerToToken;
import org.xbup.lib.xb.parser.token.XBTTokenType;
import org.xbup.lib.xb.parser.token.pull.XBTPullReader;
import org.xbup.lib.xb.stream.XBTInputTokenStream;

/**
 *
 * @version 0.1 wr23.0 2013/11/25
 * @author XBUP Project (http://xbup.org)
 */
public class XBTFileInputStream extends XBTInputTokenStream {

    private FileInputStream source;
    private XBTPullReader reader;

    /** Creates a new instance of XBFileInputStream */
    public XBTFileInputStream(File file, XBCatalog catalog) throws FileNotFoundException, IOException {
        source = new FileInputStream(file);
        reader = new XBTPullReader(catalog);
        reader.open(source);
    }

    public XBTFileInputStream(String fileName, XBCatalog catalog) throws FileNotFoundException, IOException {
        source = new FileInputStream(fileName);
        reader = new XBTPullReader(catalog);
        reader.open(source);
    }

    public XBTFileInputStream(FileDescriptor fd, XBCatalog catalog) throws FileNotFoundException, IOException {
        source = new FileInputStream(fd);
        reader = new XBTPullReader(catalog);
        reader.open(source);
    }

    public void toXBTEvents(XBTListener listener) throws XBProcessingException, IOException {
        XBTListenerToToken.tokenToListener(reader.pullXBTToken(), listener);
    }

    @Override
    public void reset() throws IOException {
        source.reset();
        reader.reset();
    }

    @Override
    public void close() throws IOException {
        reader.close();
        source.close();
    }

    @Override
    public XBTToken pullXBTToken() throws XBProcessingException {
        try {
            return reader.pullXBTToken();
        } catch (IOException ex) {
            Logger.getLogger(XBTFileInputStream.class.getName()).log(Level.SEVERE, null, ex);
            return null; // Todo later
        }
    }

    public XBTTokenType getXBTType() throws XBProcessingException {
        XBTToken item = pullXBTToken();
        return item.getTokenType();
    }

    @Override
    public boolean finished() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void skip(long tokenCount) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
