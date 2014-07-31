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
package org.xbup.lib.core.stream.filter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.event.XBTEventListener;
import org.xbup.lib.core.parser.token.event.convert.XBToXBTEventConvertor;
import org.xbup.lib.core.stream.XBTokenInputStream;
import org.xbup.lib.core.stream.XBTInputTokenStream;

/**
 * XBUP level 0 to level 1 stream convertor.
 *
 * TODO: Fill type instead of attributes
 * 
 * @version 0.1 wr18.0 2009/07/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBToXBTStreamUndropConvertor extends XBTInputTokenStream {

    private XBTokenInputStream source;
    private XBToXBTEventConvertor convertor;
    private XBBlockType type;
    ConvertorListener listener;

    public XBToXBTStreamUndropConvertor(XBTokenInputStream input, XBBlockType type) {
        this.source = input;
        this.type = type;
        listener = new ConvertorListener();
        convertor = new XBToXBTEventConvertor(listener);
    }

    @Override
    public XBTToken pullXBTToken() throws XBProcessingException {
        XBTToken token = listener.popXBT();
        if (token == null) {
            try {
                convertor.putXBToken(source.pullXBToken());
                token = listener.popXBT();
                if (token == null) {
                    convertor.putXBToken(source.pullXBToken());
                    token = listener.popXBT();
                }
            } catch (IOException ex) {
                Logger.getLogger(XBToXBTStreamUndropConvertor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return token;
    }

    @Override
    public void reset() throws IOException {
        source.reset();
    }

    @Override
    public boolean finished() throws IOException {
        return source.finished();
    }

    @Override
    public void skip(long tokenCount) throws XBProcessingException, IOException {
        source.skip(tokenCount);
    }

    @Override
    public void close() throws IOException {
        source.close();
    }

    private class ConvertorListener implements XBTEventListener {

        private XBTToken token, mem;

        public ConvertorListener() {
            token = null;
            mem = null;
        }

        public XBTToken popXBT() {
            XBTToken result;
            if (token != null) {
                result = token;
                token = null;
                return result;
            }
            result = mem;
            mem = null;
            return result;
        }

        @Override
        public void putXBTToken(XBTToken token) throws XBProcessingException, IOException {
            if (this.token == null) {
                this.token = token;
            } else {
                if (mem == null) {
                    throw new XBProcessingException("Capacity overflow during conversion");
                }
                this.mem = token;
            }
        }
    }
}
