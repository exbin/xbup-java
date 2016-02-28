/*
 * Copyright (C) ExBin Project
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
package org.xbup.lib.core.block.declaration.local;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.definition.XBBlockDef;
import org.xbup.lib.core.block.definition.local.XBLBlockDef;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.serial.basic.XBReceivingFinished;
import org.xbup.lib.core.serial.basic.XBTBasicInputReceivingSerialHandler;
import org.xbup.lib.core.serial.basic.XBTBasicOutputReceivingSerialHandler;
import org.xbup.lib.core.serial.basic.XBTBasicReceivingSerializable;
import org.xbup.lib.core.serial.param.XBPSequenceSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerializable;
import org.xbup.lib.core.serial.param.XBSerializationMode;

/**
 * XBUP level 1 local block declaration.
 *
 * @version 0.1.25 2015/02/05
 * @author ExBin Project (http://exbin.org)
 */
public class XBLBlockDecl implements XBBlockDecl, XBPSequenceSerializable, XBTBasicReceivingSerializable {

    private long[] catalogPath = null;
    private int revision = 0;
    private XBBlockDef blockDef = null;

    public XBLBlockDecl() {
        catalogPath = null;
        revision = 0;
    }

    public XBLBlockDecl(long[] revisionPath) {
        revision = (int) revisionPath[revisionPath.length - 1];
        catalogPath = Arrays.copyOf(revisionPath, revisionPath.length - 1);
    }

    public XBLBlockDecl(Long[] revisionPath) {
        setCatalogObjectPath(Arrays.copyOf(revisionPath, revisionPath.length - 1));
        revision = revisionPath[revisionPath.length - 1].intValue();
    }

    public XBLBlockDecl(long[] specPath, int revision) {
        this.catalogPath = specPath;
        this.revision = revision;
    }

    public XBLBlockDecl(Long[] specPath, int revision) {
        setCatalogObjectPath(specPath);
        this.revision = revision;
    }

    private void setCatalogObjectPath(Long[] path) {
        catalogPath = new long[path.length];
        for (int i = 0; i < path.length; i++) {
            catalogPath[i] = path[i];
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof XBLBlockDecl) {
            return Arrays.equals(((XBLBlockDecl) obj).catalogPath, catalogPath) && (((XBLBlockDecl) obj).revision == revision);
        } else if (obj instanceof XBBlockDecl) {
            return obj.equals(this);
        }

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Arrays.hashCode(this.catalogPath);
        hash = 47 * hash + this.revision;
        return hash;
    }

    public void clear() {
        catalogPath = null;
        revision = 0;
        blockDef = null;
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBFixedBlockType(XBBasicBlockType.BLOCK_DECLARATION));
        if (serial.getSerializationMode() == XBSerializationMode.PULL) {
            catalogPath = new long[serial.pullAttribute().getNaturalInt()];
            for (int pathPosition = 0; pathPosition < catalogPath.length; pathPosition++) {
                catalogPath[pathPosition] = serial.pullLongAttribute();
            }
            revision = serial.pullAttribute().getNaturalInt();

            if (!serial.pullIfEmptyBlock()) {
                blockDef = new XBLBlockDef();
                serial.pullConsist(blockDef);
            }
        } else {
            if (catalogPath != null) {
                serial.putAttribute(catalogPath.length);
                for (long pathIndex : catalogPath) {
                    serial.putAttribute(pathIndex);
                }
            } else {
                serial.putAttribute(0);
            }

            serial.putAttribute(revision);
            if (blockDef != null) {
                serial.putConsist(blockDef);
            }
        }
        serial.end();
    }

    private enum RecvProcessingState {

        START, BEGIN, TYPE, CATALOG_PATH_SIZE, CATALOG_PATH, REVISION, BLOCK_DEFINITION, END
    }

    @Override
    public void serializeRecvFromXB(XBTBasicInputReceivingSerialHandler serializationHandler) throws XBProcessingException, IOException {
        clear();
        serializationHandler.process(new RecvSerialization());
    }

    private class RecvSerialization implements XBTListener, XBReceivingFinished {

        private RecvProcessingState processingState = RecvProcessingState.START;
        private XBTListener activeListener = null;
        private int catalogPathPos = 0;

        @Override
        public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
            if (processingState == RecvProcessingState.TYPE || processingState == RecvProcessingState.CATALOG_PATH || processingState == RecvProcessingState.CATALOG_PATH_SIZE || processingState == RecvProcessingState.REVISION) {
                finishCatalogPath();
                blockDef = new XBLBlockDef();
                ((XBLBlockDef) blockDef).serializeRecvFromXB(new XBTBasicInputReceivingSerialHandler() {

                    @Override
                    public void process(XBTListener listener) {
                        activeListener = listener;
                    }
                });
            }

            if (activeListener != null) {
                activeListener.beginXBT(terminationMode);
                return;
            }

            if (processingState != RecvProcessingState.START) {
                throw new XBProcessingException("Unexpected token: begin", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }

            processingState = RecvProcessingState.BEGIN;
        }

        @Override
        public void typeXBT(XBBlockType type) throws XBProcessingException, IOException {
            if (activeListener != null) {
                activeListener.typeXBT(type);
                return;
            }

            if (processingState != RecvProcessingState.BEGIN) {
                throw new XBProcessingException("Unexpected token: type", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }

            processingState = RecvProcessingState.TYPE;
        }

        @Override
        public void attribXBT(XBAttribute value) throws XBProcessingException, IOException {
            if (activeListener != null) {
                activeListener.attribXBT(value);
                return;
            }

            if (processingState == RecvProcessingState.TYPE) {
                catalogPath = new long[value.getNaturalInt()];
                catalogPathPos = 0;
                processingState = RecvProcessingState.CATALOG_PATH_SIZE;
            } else if (processingState == RecvProcessingState.CATALOG_PATH_SIZE) {
                catalogPath[catalogPathPos] = value.getNaturalLong();
                catalogPathPos++;

                if (catalogPathPos == catalogPath.length) {
                    processingState = RecvProcessingState.CATALOG_PATH;
                }
            } else if (processingState == RecvProcessingState.CATALOG_PATH) {
                revision = value.getNaturalInt();
                processingState = RecvProcessingState.REVISION;
            } else {
                throw new XBProcessingException("Unexpected token: attribute", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        }

        @Override
        public void dataXBT(InputStream data) throws XBProcessingException, IOException {
            if (activeListener != null) {
                activeListener.dataXBT(data);
                return;
            }

            throw new XBProcessingException("Unexpected token: data", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        @Override
        public void endXBT() throws XBProcessingException, IOException {
            if (activeListener != null) {
                activeListener.endXBT();
                if (((XBReceivingFinished) activeListener).isFinished()) {
                    processingState = RecvProcessingState.BLOCK_DEFINITION;
                    activeListener = null;
                }

                return;
            }

            if (processingState == RecvProcessingState.START || processingState == RecvProcessingState.BEGIN || processingState == RecvProcessingState.END) {
                throw new XBProcessingException("Unexpected token: end", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }

            finishCatalogPath();
            processingState = RecvProcessingState.END;
        }

        private void finishCatalogPath() {
            if (processingState == RecvProcessingState.CATALOG_PATH_SIZE) {
                for (int i = catalogPathPos; i < catalogPath.length; i++) {
                    catalogPath[i] = 0;
                }
            }
        }

        @Override
        public boolean isFinished() {
            return processingState == RecvProcessingState.END;
        }
    }

    @Override
    public void serializeRecvToXB(XBTBasicOutputReceivingSerialHandler serializationHandler) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public XBCBlockSpec getBlockSpec(XBCatalog catalog) {
        return (XBCBlockSpec) catalog.findBlockTypeByPath(getCatalogObjectPath(), revision);
    }

    /**
     * Gets catalog path as array of Long instances.
     *
     * @return the catalogPath
     */
    public Long[] getCatalogObjectPath() {
        Long[] objectPath = new Long[catalogPath.length];
        for (int i = 0; i < objectPath.length; i++) {
            objectPath[i] = catalogPath[i];
        }
        return objectPath;
    }

    public long[] getCatalogPath() {
        return catalogPath;
    }

    public void setCatalogPath(long[] catalogPath) {
        this.catalogPath = catalogPath;
    }

    public void setCatalogPath(Long[] path) {
        setCatalogObjectPath(path);
    }

    @Override
    public long getRevision() {
        return revision;
    }

    @Override
    public XBBlockDef getBlockDef() {
        return blockDef;
    }

    public void setBlockDef(XBBlockDef blockDef) {
        this.blockDef = blockDef;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }
}
