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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.block.XBTBlock;
import org.xbup.lib.core.block.XBTEditableDocument;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBHead;
import org.xbup.lib.core.util.CopyStreamUtils;

/**
 * Basic object model parser XBUP level 1 document representation.
 *
 * @version 0.1.20 2010/09/26
 * @author XBUP Project (http://xbup.org)
 */
public class XBTTreeDocument extends XBTTree implements XBTEditableDocument {

    private boolean modified;
    private String fileName;
    private byte[] extendedAreaData;

    /**
     * Creates a new instance of XBTreeDocument
     */
    public XBTTreeDocument() {
        super(null);
        extendedAreaData = null;
    }

    public XBTTreeDocument(XBCatalog catalog) {
        super(catalog);
    }

    @Override
    public int toStreamUB(OutputStream stream) throws IOException {
        int size = XBHead.writeXBUPHead(stream);
        size += super.toStreamUB(stream);
        if (extendedAreaData != null) {
            size += extendedAreaData.length;
            if (extendedAreaData.length > 0) {
                stream.write(extendedAreaData);
            }
        }
        return size;
    }

    @Override
    public int fromStreamUB(InputStream stream) throws IOException, XBProcessingException {
        int size = XBHead.checkXBUPHead(stream);
        clear();
        size += super.fromStreamUB(stream);
        setExtended(stream);
        return (int) (size + getExtendedSize());
    }

    @Override
    public int getSizeUB() {
        int size = XBHead.getXBUPHeadSize();
        size += super.getSizeUB();
        if (getExtended() != null) {
            size += getExtendedSize();
        }
        return size;
    }

    @Override
    public InputStream getExtended() {
        if (extendedAreaData == null) {
            return null;
        }
        return new ByteArrayInputStream(extendedAreaData);
    }

    public byte[] getExtendedArray() {
        return extendedAreaData;
    }

    @Override
    public void setExtended(InputStream source) {
        if (source == null) {
            extendedAreaData = null;
        } else if (source instanceof ByteArrayInputStream) {
            // data.reset();
            extendedAreaData = new byte[((ByteArrayInputStream) source).available()];
            if (extendedAreaData.length > 0) {
                try {
                     source.read(extendedAreaData, 0, extendedAreaData.length);
                } catch (IOException ex) {
                    Logger.getLogger(XBTreeNode.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            // data.reset();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try {
                CopyStreamUtils.copyInputStreamToOutputStream(source, stream);
            } catch (IOException ex) {
                Logger.getLogger(XBTreeNode.class.getName()).log(Level.SEVERE, null, ex);
            }
            extendedAreaData = stream.toByteArray();
        }
    }

    @Override
    public long getExtendedSize() {
        if (extendedAreaData == null) {
            return 0;
        }
        return extendedAreaData.length;
    }

    @Override
    public void clear() {
        super.clear();
        if (extendedAreaData != null) {
            extendedAreaData = null;
        }
    }

    @Override
    public void setRootBlock(XBTBlock block) {
        if (!(block instanceof XBTTreeNode)) {
            throw new IllegalArgumentException("Unsupported type of root block");
        }

        super.setRootBlock((XBTTreeNode) block);
    }

    public boolean wasModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int fromFileUB() throws IOException, XBProcessingException {
        InputStream stream = new FileInputStream(fileName);
        int size = fromStreamUB(stream);
        stream.close();
        modified = false;
        return size;
    }

    public int toFileUB() throws IOException {
        OutputStream stream = new FileOutputStream(fileName);
        int size = toStreamUB(stream);
        stream.close();
        modified = false;
        return size;
    }

    @Override
    public void processSpec() {
        super.processSpec();
    }

    @Override
    public XBTBlock findNodeByIndex(long index) {
        return (XBTBlock) super.findNodeByIndex(index);
    }

    @Override
    public XBTBlock createNewBlock(XBTBlock parent) {
        return newNodeInstance((XBTTreeNode) parent);
    }

    @Override
    public long getDocumentSize() {
        long documentSize = getExtendedSize();
        if (getRootBlock() != null) {
            documentSize += getRootBlock().getBlockSize();
        }
        return documentSize;
    }
}
