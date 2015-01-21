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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.declaration.XBGroupDecl;
import org.xbup.lib.core.block.definition.XBGroupDef;
import org.xbup.lib.core.block.definition.XBGroupParam;
import org.xbup.lib.core.block.definition.XBGroupParamConsist;
import org.xbup.lib.core.block.definition.XBGroupParamJoin;
import org.xbup.lib.core.block.definition.local.XBLGroupDef;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.param.XBSerializationMode;
import org.xbup.lib.core.serial.param.XBTSequenceSerialHandler;
import org.xbup.lib.core.serial.param.XBTSequenceSerializable;

/**
 * XBUP level 1 local group declaration.
 *
 * @version 0.1.24 2015/01/18
 * @author XBUP Project (http://xbup.org)
 */
public class XBLGroupDecl implements XBGroupDecl, XBTSequenceSerializable {

    private long[] catalogPath = null;
    private int revision;
    private XBGroupDef groupDef = null;

    public XBLGroupDecl() {
        catalogPath = null;
    }

    public XBLGroupDecl(long[] path) {
        this.catalogPath = path;
    }

    public XBLGroupDecl(Long[] path) {
        setCatalogObjectPath(path);
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

        return super.equals(obj);
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
