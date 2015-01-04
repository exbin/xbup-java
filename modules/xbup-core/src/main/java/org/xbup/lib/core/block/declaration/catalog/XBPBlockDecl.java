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
package org.xbup.lib.core.block.declaration.catalog;

import java.io.IOException;
import java.util.Arrays;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.definition.XBBlockDef;
import org.xbup.lib.core.block.definition.catalog.XBPBlockDef;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.sequence.XBSerializationMode;
import org.xbup.lib.core.serial.sequence.XBTSequenceSerialHandler;
import org.xbup.lib.core.serial.sequence.XBTSequenceSerializable;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * XBUP level 1 block declaration using catalog path.
 *
 * @version 0.1.24 2015/01/05
 * @author XBUP Project (http://xbup.org)
 */
public class XBPBlockDecl implements XBBlockDecl, XBTSequenceSerializable {

    private long[] catalogPath;
    private int revision;

    public XBPBlockDecl() {
        catalogPath = null;
    }

    public XBPBlockDecl(long[] path) {
        this(path, 0);
    }

    public XBPBlockDecl(Long[] path) {
        this(path, 0);
    }

    public XBPBlockDecl(long[] path, UBNatural revision) {
        this(path, revision != null ? revision.getInt() : 0);
    }

    public XBPBlockDecl(Long[] path, UBNatural revision) {
        this(path, revision != null ? revision.getInt() : 0);
    }

    public XBPBlockDecl(long[] path, int revision) {
        this.catalogPath = path;
    }

    public XBPBlockDecl(Long[] path, int revision) {
        setCatalogObjectPath(path);
    }

    private void setCatalogObjectPath(Long[] path) {
        catalogPath = new long[path.length];
        for (int i = 0; i < path.length; i++) {
            catalogPath[i] = path[i];
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof XBPBlockDecl) {
            return Arrays.equals(((XBPBlockDecl) obj).catalogPath, catalogPath) && (((XBPBlockDecl) obj).revision == revision);
        } else if (obj instanceof XBCBlockDecl) {
            return ((XBCBlockDecl) obj).equals(this);
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

    @Override
    public void serializeXB(XBTSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
        serializationHandler.begin();
        serializationHandler.matchType(new XBFixedBlockType(XBBasicBlockType.BLOCK_DECLARATION));
        if (serializationHandler.getSerializationMode() == XBSerializationMode.PULL) {
            catalogPath = new long[serializationHandler.pullAttribute().getInt()];
            for (int pathPosition = 0; pathPosition < catalogPath.length; pathPosition++) {
                catalogPath[pathPosition] = serializationHandler.pullLongAttribute();
            }
            revision = serializationHandler.pullAttribute().getInt();
        } else {
            serializationHandler.putAttribute(catalogPath.length - 1);
            for (long pathIndex : catalogPath) {
                serializationHandler.putAttribute(pathIndex);
            }

            serializationHandler.putAttribute(revision);
        }
        serializationHandler.end();
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
        return new XBPBlockDef(catalogPath);
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }
}
