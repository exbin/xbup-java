/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.block.declaration.local;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import org.exbin.xbup.core.block.XBBasicBlockType;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.block.declaration.XBFormatDecl;
import org.exbin.xbup.core.block.declaration.XBGroupDecl;
import org.exbin.xbup.core.block.definition.XBFormatDef;
import org.exbin.xbup.core.block.definition.XBFormatParam;
import org.exbin.xbup.core.block.definition.XBFormatParamConsist;
import org.exbin.xbup.core.block.definition.XBFormatParamJoin;
import org.exbin.xbup.core.block.definition.local.XBLFormatDef;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.XBTListener;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.serial.basic.XBReceivingFinished;
import org.exbin.xbup.core.serial.basic.XBTBasicInputReceivingSerialHandler;
import org.exbin.xbup.core.serial.basic.XBTBasicOutputReceivingSerialHandler;
import org.exbin.xbup.core.serial.basic.XBTBasicReceivingSerializable;
import org.exbin.xbup.core.serial.param.XBPSequenceSerialHandler;
import org.exbin.xbup.core.serial.param.XBPSequenceSerializable;
import org.exbin.xbup.core.serial.param.XBSerializationMode;

/**
 * XBUP level 1 local format declaration.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBLFormatDecl implements XBFormatDecl, XBPSequenceSerializable, XBTBasicReceivingSerializable {

    private long[] catalogPath = null;
    private int revision = 0;
    private XBFormatDef formatDef = null;

    public XBLFormatDecl() {
        catalogPath = null;
        revision = 0;
    }

    public XBLFormatDecl(long[] revisionPath) {
        revision = (int) revisionPath[revisionPath.length - 1];
        catalogPath = Arrays.copyOf(revisionPath, revisionPath.length - 1);
    }

    public XBLFormatDecl(Long[] revisionPath) {
        setCatalogObjectPath(Arrays.copyOf(revisionPath, revisionPath.length - 1));
        revision = revisionPath[revisionPath.length - 1].intValue();
    }

    public XBLFormatDecl(long[] specPath, int revision) {
        this.catalogPath = specPath;
        this.revision = revision;
    }

    public XBLFormatDecl(Long[] specPath, int revision) {
        setCatalogObjectPath(specPath);
        this.revision = revision;
    }

    public XBLFormatDecl(XBFormatDef formatDef) {
        this.formatDef = formatDef;
    }

    public XBLFormatDecl(XBGroupDecl groupDecl) {
        formatDef = new XBLFormatDef(groupDecl);
    }

    public XBLFormatDecl(long[] revisionPath, XBFormatDef formatDef) {
        this(revisionPath);
        this.formatDef = formatDef;
    }

    @Override
    public List<XBGroupDecl> getGroupDecls() {
        List<XBGroupDecl> groups = new ArrayList<>();
        int groupsLimit = getGroupsLimit();
        for (int paramIndex = 0; paramIndex < groupsLimit; paramIndex++) {
            XBFormatParam formatParam = formatDef.getFormatParam(paramIndex);
            if (formatParam instanceof XBFormatParamJoin) {
                XBFormatDecl groupDecl = ((XBFormatParamJoin) formatParam).getFormatDecl();
                groups.addAll(groupDecl.getGroupDecls());
            } else {
                if (formatParam != null) {
                    groups.add(((XBFormatParamConsist) formatParam).getGroupDecl());
                }
            }
        }

        return groups;
    }

    public int getGroupsLimit() {
        return formatDef == null ? 0
                : formatDef.getRevisionDef() == null ? 0 : formatDef.getRevisionDef().getRevisionLimit(revision);
    }

    private void setCatalogObjectPath(Long[] path) {
        catalogPath = new long[path.length];
        for (int i = 0; i < path.length; i++) {
            catalogPath[i] = path[i];
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Arrays.hashCode(this.catalogPath);
        hash = 47 * hash + this.revision;
        return hash;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof XBLFormatDecl) {
            return Arrays.equals(((XBLFormatDecl) obj).catalogPath, catalogPath) && (((XBLFormatDecl) obj).revision == revision);
        } else if (obj instanceof XBFormatDecl) {
            obj.equals(this);
        }

        return super.equals(obj);
    }

    public void clear() {
        catalogPath = null;
        revision = 0;
        formatDef = null;
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBFixedBlockType(XBBasicBlockType.FORMAT_DECLARATION));
        if (serial.getSerializationMode() == XBSerializationMode.PULL) {
            catalogPath = new long[serial.pullAttribute().getNaturalInt()];
            for (int pathPosition = 0; pathPosition < catalogPath.length; pathPosition++) {
                catalogPath[pathPosition] = serial.pullLongAttribute();
            }
            revision = serial.pullAttribute().getNaturalInt();

            if (!serial.pullIfEmptyBlock()) {
                formatDef = new XBLFormatDef();
                serial.pullConsist(formatDef);
            } else {
                formatDef = null;
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
            if (formatDef != null) {
                serial.putConsist(formatDef);
            }
        }
        serial.end();
    }

    private enum RecvProcessingState {

        START, BEGIN, TYPE, CATALOG_PATH_SIZE, CATALOG_PATH, REVISION, FORMAT_DEFINITION, END
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
                formatDef = new XBLFormatDef();
                ((XBLFormatDef) formatDef).serializeRecvFromXB(new XBTBasicInputReceivingSerialHandler() {

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
            } else if (processingState == RecvProcessingState.REVISION) {
                // ignore
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
                    processingState = RecvProcessingState.FORMAT_DEFINITION;
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

    public Long[] getCatalogPathAsClassArray() {
        if (catalogPath == null) {
            return null;
        }

        Long[] classCatalogPath = new Long[catalogPath.length];
        for (int pathIndex = 0; pathIndex < catalogPath.length; pathIndex++) {
            classCatalogPath[pathIndex] = catalogPath[pathIndex];
        }

        return classCatalogPath;
    }

    public void setCatalogPath(long[] catalogPath) {
        this.catalogPath = catalogPath;
    }

    public void setCatalogPath(Long[] path) {
        setCatalogObjectPath(path);
    }

    @Override
    public XBFormatDef getFormatDef() {
        return formatDef;
    }

    public void setFormatDef(XBFormatDef formatDef) {
        this.formatDef = formatDef;
    }

    @Override
    public long getRevision() {
        return revision;
    }
}
