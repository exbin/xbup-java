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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.event.XBEventWriter;
import org.xbup.lib.core.parser.token.event.convert.XBTToXBEventConvertor;
import org.xbup.lib.core.stream.XBTOutputStream;

/**
 *
 * @version 0.1.16 2008/08/02
 * @author XBUP Project (http://xbup.org)
 */
public class XBTFileOutputStream extends XBTOutputStream {

    private FileOutputStream target;
    private XBTToXBEventConvertor convertor;
    private XBEventWriter writer;

    /** Creates a new instance of XBFileOutputStream */
    public XBTFileOutputStream(File file) throws FileNotFoundException, IOException {
        target = new FileOutputStream(file);
        writer = new XBEventWriter();
        writer.open(target);
        convertor = new XBTToXBEventConvertor(writer);
    }

    public XBTFileOutputStream(String fileName) throws FileNotFoundException, IOException {
        target = new FileOutputStream(fileName);
        writer = new XBEventWriter();
        writer.open(target);
        convertor = new XBTToXBEventConvertor(writer);
    }

    public XBTFileOutputStream(FileDescriptor fd) throws FileNotFoundException, IOException {
        target = new FileOutputStream(fd);
        writer = new XBEventWriter();
        writer.open(target);
        convertor = new XBTToXBEventConvertor(writer);
    }

    @Override
    public void putXBTToken(XBTToken token) throws IOException, XBProcessingException {
        convertor.putXBTToken(token);
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }

    @Override
    public void flush() throws IOException {
        // writer.flush();
    }
}
