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
package org.xbup.lib.core.block.declaration.local;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.declaration.XBGroupDecl;
import org.xbup.lib.core.block.definition.XBGroupDef;
import org.xbup.lib.core.block.definition.XBGroupParam;
import org.xbup.lib.core.block.definition.XBGroupParamConsist;
import org.xbup.lib.core.block.definition.XBGroupParamJoin;
import org.xbup.lib.core.block.definition.local.XBLGroupDef;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.serial.basic.XBReceivingFinished;
import org.xbup.lib.core.serial.basic.XBTBasicInputReceivingSerialHandler;
import org.xbup.lib.core.serial.basic.XBTBasicOutputReceivingSerialHandler;
import org.xbup.lib.core.serial.basic.XBTBasicReceivingSerializable;
import org.xbup.lib.core.serial.param.XBPSequenceSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerializable;
import org.xbup.lib.core.serial.param.XBSerializationMode;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * XBUP level 1 local group declaration.
 *
 * @version 0.1.25 2015/02/05
 * @author XBUP Project (http://xbup.org)
 */
public class XBLGroupDecl implements XBGroupDecl, XBPSequenceSerializable, XBTBasicReceivingSerializable {

    private long[] catalogPath = null;
    private int revision;
    private XBGroupDef groupDef = null;

    public XBLGroupDecl() {
        catalogPath = null;
        revision = 0;
    }

    public XBLGroupDecl(long[] revisionPath) {
        revision = (int) revisionPath[revisionPath.length - 1];
        catalogPath = Arrays.copyOf(revisionPath, revisionPath.length - 1);
    }

    public XBLGroupDecl(Long[] revisionPath) {
        setCatalogObjectPath(Arrays.copyOf(revisionPath, revisionPath.length - 1));
        revision = revisionPath[revisionPath.length - 1].intValue();
    }

    public XBLGroupDecl(long[] specPath, int revision) {
        this.catalogPath = specPath;
        this.revision = revision;
    }

    public XBLGroupDecl(Long[] specPath, int revision) {
        setCatalogObjectPath(specPath);
        this.revision = revision;
    }

    public XBLGroupDecl(XBLGroupDef groupDef) {
        this.groupDef = groupDef;
    }

    public XBLGroupDecl(XBBlockDecl block) {
        groupDef = new XBLGroupDef(block);
    }

    private void setCatalogObjectPath(Long[] path) {
        catalogPath = new long[path.length];
        for (int i = 0; i < path.length; i++) {
            catalogPath[i] = path[i];
        }
    }

    @Override
    public List<XBBlockDecl> getBlockDecls() {
        List<XBBlockDecl> blocks = new ArrayList<>();
        int blocksLimit = getBlocksLimit();
        for (int paramIndex = 0; paramIndex < blocksLimit; paramIndex++) {
            XBGroupParam groupParam = groupDef.getGroupParam(paramIndex);
            if (groupParam instanceof XBGroupParamJoin) {
                XBGroupDecl groupDecl = ((XBGroupParamJoin) groupParam).getGroupDecl();
                blocks.addAll(groupDecl.getBlockDecls());
            } else {
                blocks.add(((XBGroupParamConsist) groupParam).getBlockDecl());
            }
        }

        return blocks;
    }

    public int getBlocksLimit() {
        return groupDef.getRevisionDef().getRevisionLimit(revision);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Arrays.hashCode(this.catalogPath);
        hash = 47 * hash + this.revision;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof XBLGroupDecl) {
            return Arrays.equals(((XBLGroupDecl) obj).catalogPath, catalogPath) && (((XBLGroupDecl) obj).revision == revision);
        }
        if (obj instanceof XBGroupDecl) {
            return obj.equals(this);
        }

        return super.equals(obj);
    }

    public void clear() {
        catalogPath = null;
        revision = 0;
        groupDef = null;
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBFixedBlockType(XBBasicBlockType.GROUP_DECLARATION));
        if (serial.getSerializationMode() == XBSerializationMode.PULL) {
            catalogPath = new long[serial.pullAttribute().getInt()];
            for (int pathPosition = 0; pathPosition < catalogPath.length; pathPosition++) {
                catalogPath[pathPosition] = serial.pullLongAttribute();
            }
            revision = serial.pullAttribute().getInt();

            if (!serial.pullIfEmptyBlock()) {
                groupDef = new XBLGroupDef();
                serial.pullConsist(groupDef);
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
            if (groupDef != null) {
                serial.putConsist(groupDef);
            }
        }
        serial.end();
    }

    private enum RecvProcessingState {

        START, BEGIN, TYPE, CATALOG_PATH_SIZE, CATALOG_PATH, REVISION, GROUP_DEFINITION, END
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
                groupDef = new XBLGroupDef();
                ((XBLGroupDef) groupDef).serializeRecvFromXB(new XBTBasicInputReceivingSerialHandler() {

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
        public void attribXBT(UBNatural value) throws XBProcessingException, IOException {
            if (activeListener != null) {
                activeListener.attribXBT(value);
                return;
            }

            if (processingState == RecvProcessingState.TYPE) {
                catalogPath = new long[value.getInt()];
                catalogPathPos = 0;
                processingState = RecvProcessingState.CATALOG_PATH_SIZE;
            } else if (processingState == RecvProcessingState.CATALOG_PATH_SIZE) {
                catalogPath[catalogPathPos] = value.getLong();
                catalogPathPos++;

                if (catalogPathPos == catalogPath.length) {
                    processingState = RecvProcessingState.CATALOG_PATH;
                }
            } else if (processingState == RecvProcessingState.CATALOG_PATH) {
                revision = value.getInt();
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
                    processingState = RecvProcessingState.GROUP_DEFINITION;
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
    public XBGroupDef getGroupDef() {
        return groupDef;
    }

    public void setGroupDef(XBGroupDef groupDef) {
        this.groupDef = groupDef;
    }

    @Override
    public long getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }
}
