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
import java.util.Arrays;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.definition.XBBlockDef;
import org.xbup.lib.core.block.definition.local.XBLBlockDef;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.param.XBPSequenceSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerializable;
import org.xbup.lib.core.serial.param.XBSerializationMode;

/**
 * XBUP level 1 local block declaration.
 *
 * @version 0.1.25 2015/02/02
 * @author XBUP Project (http://xbup.org)
 */
public class XBLBlockDecl implements XBBlockDecl, XBPSequenceSerializable {

    private long[] catalogPath;
    private int revision;
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

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBFixedBlockType(XBBasicBlockType.BLOCK_DECLARATION));
        if (serial.getSerializationMode() == XBSerializationMode.PULL) {
            catalogPath = new long[serial.pullAttribute().getInt()];
            for (int pathPosition = 0; pathPosition < catalogPath.length; pathPosition++) {
                catalogPath[pathPosition] = serial.pullLongAttribute();
            }
            revision = serial.pullAttribute().getInt();

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

    public void setRevision(int revision) {
        this.revision = revision;
    }
}
