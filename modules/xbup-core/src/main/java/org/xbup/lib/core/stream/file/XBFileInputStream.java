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
package org.xbup.lib.core.stream.file;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.XBToken;
import org.xbup.lib.core.parser.token.XBTokenType;
import org.xbup.lib.core.parser.token.pull.XBPullReader;
import org.xbup.lib.core.stream.XBTokenInputStream;

/**
 *
 * @version 0.1 wr17.0 2009/07/13
 * @author XBUP Project (http://xbup.org)
 */
public class XBFileInputStream extends XBTokenInputStream {

    private FileInputStream source;
    private XBPullReader reader;

    /** Creates a new instance of XBFileInputStream */
    public XBFileInputStream(File file) throws FileNotFoundException, IOException {
        source = new FileInputStream(file);
        reader = new XBPullReader();
        reader.open(source);
    }

    public XBFileInputStream(String fileName) throws FileNotFoundException, IOException {
        source = new FileInputStream(fileName);
        reader = new XBPullReader();
        reader.open(source);
    }

    public XBFileInputStream(FileDescriptor fd) throws FileNotFoundException, IOException {
        source = new FileInputStream(fd);
        reader = new XBPullReader();
        reader.open(source);
    }

    @Override
    public void close() throws IOException {
        reader.close();
        getSource().close();
    }

    @Override
    public XBToken pullXBToken() throws XBProcessingException {
        try {
            return reader.pullXBToken();
        } catch (IOException ex) {
            Logger.getLogger(XBFileInputStream.class.getName()).log(Level.SEVERE, null, ex);
            return null; // Todo later
        }
    }

    @Override
    public void reset() throws IOException {
        // TODO Properly reset
        source.reset();
        reader.reset();
    }

    public XBTokenType getItem() throws XBProcessingException {
        XBToken item = pullXBToken();
        return item.getTokenType();
    }

    /**
     * @return the source
     */
    public FileInputStream getSource() {
        return source;
    }

    @Override
    public boolean finished() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void skip(long tokenCount) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
