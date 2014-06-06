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
package org.xbup.lib.xb.parser.command;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import org.xbup.lib.xb.block.XBBlock;
import org.xbup.lib.xb.block.XBBlockDataMode;
import org.xbup.lib.xb.block.XBEditableBlock;
import org.xbup.lib.xb.block.XBBlockTerminationMode;
import org.xbup.lib.xb.ubnumber.UBNatural;

/**
 * XBUP level 0 command writer block.
 *
 * @version 0.1 wr23.0 2014/04/23
 * @author XBUP Project (http://xbup.org)
 */
public class XBWriterBlock implements XBEditableBlock, Closeable {

    private final long[] blockPath;
    private final XBWriter writer;

    public XBWriterBlock(XBWriter writer, long[] blockPath) {
        this.blockPath = blockPath;
        this.writer = writer;
    }

    @Override
    public XBBlock getParent() {
        if (blockPath.length == 0) {
            return null;
        } else {
            return writer.getBlock(Arrays.copyOf(blockPath, blockPath.length - 1));
        }
    }

    @Override
    public XBBlockDataMode getDataMode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBBlockTerminationMode getTerminationMode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<UBNatural> getAttributes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public UBNatural getAttribute(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getAttributesCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<XBBlock> getChildren() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBBlock getChildAt(int childIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getChildCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public InputStream getData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getDataSize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getBlockSize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void close() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setParent(XBBlock parent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setTerminationMode(XBBlockTerminationMode terminationMode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setDataMode(XBBlockDataMode dataMode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setAttributes(List<UBNatural> attributes) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setAttribute(UBNatural attribute, int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setAttributesCount(int count) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setChildren(List<XBBlock> blocks) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setChildAt(XBBlock block, int childIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setChildCount(int count) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setData(InputStream data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
