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
package org.xbup.lib.xb.block.declaration;

import java.io.IOException;
import org.xbup.lib.xb.block.definition.XBFormatDef;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.xbup.lib.xb.block.XBBasicBlockType;
import org.xbup.lib.xb.block.XBBlockDataMode;
import org.xbup.lib.xb.block.XBBlockType;
import org.xbup.lib.xb.block.XBFixedBlockType;
import org.xbup.lib.xb.block.XBTBlock;
import org.xbup.lib.xb.block.definition.XBRevisionDef;
import org.xbup.lib.xb.catalog.declaration.XBCPBlockDecl;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.serial.XBSerialHandler;
import org.xbup.lib.xb.serial.XBSerialMethod;
import org.xbup.lib.xb.serial.XBSerializable;
import org.xbup.lib.xb.serial.XBSerializationType;
import org.xbup.lib.xb.serial.sequence.XBSerialSequence;
import org.xbup.lib.xb.serial.sequence.XBSerialSequenceIList;
import org.xbup.lib.xb.serial.sequence.XBTSerialSequence;
import org.xbup.lib.xb.serial.sequence.XBTSerialSequenceListenerMethod;
import org.xbup.lib.xb.serial.sequence.XBTSerialSequenceProviderMethod;
import org.xbup.lib.xb.ubnumber.UBENatural;
import org.xbup.lib.xb.ubnumber.type.UBENat32;
import org.xbup.lib.xb.ubnumber.type.UBNat32;
import org.xbup.lib.xb.ubnumber.type.UBPath32;

/**
 * XBUP level 1 format declaration.
 *
 * @version 0.1 wr23.0 2014/03/04
 * @author XBUP Project (http://xbup.org)
 */
public class XBFormatDecl implements XBSerializable {

    private UBPath32 catalogPath = new UBPath32();
    private UBNat32 revision = new UBNat32(0);
    private List<XBGroupDecl> groups = new ArrayList<XBGroupDecl>();
    private UBNat32 groupsLimit = new UBNat32(0);
    private List<XBFormatDef> formatDefs;
    private List<XBRevisionDef> revisionDefs;

    public XBFormatDecl() {
        catalogPath = new UBPath32();
    }

    public XBFormatDecl(Long[] path) {
        this();
        catalogPath = new UBPath32(path);
    }

    public XBFormatDecl(long[] xbFormatPath) {
        List<Long> path = new ArrayList<Long>();
        for (int i = 0; i < xbFormatPath.length; i++) {
            path.add(xbFormatPath[i]);
        }
        setCatalogPath(new UBPath32(path.toArray(new Long[0])));
    }

    /**
     * @return List of relevant Groups.
     */
    public List<XBGroupDecl> getGroups() {
        return groups;
    }

    public static XBFormatDecl processDeclaration(XBTBlock specBlock) {
        if (specBlock.getDataMode() == XBBlockDataMode.NODE_BLOCK) {
            if (specBlock.getAttributesCount() > 1) {
                if ((specBlock.getBlockType().getGroupID().getLong() == 0) && (specBlock.getBlockType().getBlockID().getLong() == XBBasicBlockType.FORMAT_DECLARATION.ordinal())) {
                    XBFormatDecl decl = new XBFormatDecl();
                    decl.setGroupsLimit(specBlock.getAttribute(0).getLong());
                    Long[] catalogPath = new Long[specBlock.getAttribute(1).getInt()];
                    int i;
                    for (i = 0; i < catalogPath.length; i++) {
                        catalogPath[i] = specBlock.getAttribute(i + 2).getLong();
                    }
                    decl.setRevision(specBlock.getAttribute(i + 2).getLong());
                    return decl;
                }
            }
        }
        return null;
    }

    public XBFixedBlockType toStaticType(XBBlockType type) {
        if (groups == null) {
            return null;
        }
        for (int groupId = 0; groupId < groups.size(); groupId++) {
            Integer blockId = groups.get(groupId).matchType(type);
            if (blockId != null) {
                return new XBFixedBlockType(groupId, blockId);
            }
        }
        return null;
    }

    public long getGroupsLimit() {
        return groupsLimit.getLong();
    }

    public void setGroupsLimit(long groupsLimit) {
        this.groupsLimit.setValue(groupsLimit);
    }

    public long getRevision() {
        return revision.getLong();
    }

    public void setRevision(long revision) {
        this.revision.setValue(revision);
    }

    public UBPath32 getCatalogPath() {
        return catalogPath;
    }

    public void setCatalogPath(UBPath32 catalogPath) {
        this.catalogPath = catalogPath;
    }

    public void setGroups(List<XBGroupDecl> groups) {
        this.groups = groups;
    }

    public List<XBFormatDef> getFormatDef() {
        return formatDefs;
    }

    public void setFormatDef(List<XBFormatDef> formatDef) {
        this.formatDefs = formatDef;
    }

    public List<XBRevisionDef> getRevisionDef() {
        return revisionDefs;
    }

    public void setRevisionDef(List<XBRevisionDef> revisionDef) {
        this.revisionDefs = revisionDef;
    }

    @Override
    public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
        return serialType == XBSerializationType.FROM_XB
                ? Arrays.asList(new XBSerialMethod[]{new XBTSerialSequenceProviderMethod()})
                : Arrays.asList(new XBSerialMethod[]{new XBTSerialSequenceListenerMethod()});
    }

    @Override
    public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
        XBSerialSequence seq = new XBSerialSequence(new XBFixedBlockType(XBBasicBlockType.FORMAT_DECLARATION));
        // Join GroupsLimit (UBNatural)
        seq.join(new XBSerializable() {
            @Override
            public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
                return serialType == XBSerializationType.FROM_XB
                        ? Arrays.asList(new XBSerialMethod[]{new XBTSerialSequenceProviderMethod()})
                        : Arrays.asList(new XBSerialMethod[]{new XBTSerialSequenceListenerMethod()});
            }

            @Override
            public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
                long[] xbGroupLimitBlockType = {1, 5};
                XBSerialSequence subSequence = new XBSerialSequence(new XBDBlockType(new XBCPBlockDecl(xbGroupLimitBlockType)), groupsLimit);
                XBTSerialSequence serial = (XBTSerialSequence) serializationHandler;
                serial.sequenceXB(subSequence);
            }
        });
        // Join FormatSpecCatalogPath (UBPath)
        seq.join(catalogPath);
        // Join Revision (UBNatural)
        seq.join(new XBSerializable() {
            @Override
            public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
                return serialType == XBSerializationType.FROM_XB
                        ? Arrays.asList(new XBSerialMethod[]{new XBTSerialSequenceProviderMethod()})
                        : Arrays.asList(new XBSerialMethod[]{new XBTSerialSequenceListenerMethod()});
            }

            @Override
            public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
                long[] xbRevisionBlockType = {1, 5};
                XBSerialSequence subSequence = new XBSerialSequence(new XBDBlockType(new XBCPBlockDecl(xbRevisionBlockType)), revision);
                XBTSerialSequence serial = (XBTSerialSequence) serializationHandler;
                serial.sequenceXB(subSequence);
            }
        });
        // List GroupSpecification
        seq.listConsist(new XBSerialSequenceIList() {

            private int position = 0;

            @Override
            public void setSize(UBENatural count) {
                int i = count.getInt() - groups.size();
                if ((i > 0) && (groups == null)) {
                    groups = new ArrayList<>();
                }
                if (i > 0) {
                    while (i > 0) {
                        groups.add(new XBGroupDecl());
                        i--;
                    }
                } else {
                    while (i < 0) {
                        groups.remove(groups.size() - 1);
                        i++;
                    }
                }
            }

            @Override
            public UBENatural getSize() {
                if (groups == null) {
                    return new UBENat32();
                }
                return new UBENat32(groups.size());
            }

            @Override
            public XBSerializable next() {
                // TODO detect serialization
                return groups.get(position++);
                /*
                 XBTSerialMethod serialMethod = groups.get(position++).getXBTSerializationMethod();
                 if (serialMethod instanceof XBTSerialSequenceListenerMethod) {
                 return ((XBTSerialSequenceListenerMethod) serialMethod).getXBSerialSequence();
                 } else {
                 throw new UnsupportedOperationException("Not supported yet.");
                 } */
            }

            @Override
            public void reset() {
                position = 0;
            }
        });
        // List FormatConstructor
        seq.listConsist(new XBSerialSequenceIList() {

            private int position = 0;

            @Override
            public void setSize(UBENatural count) {
                int i = count.getInt() - formatDefs.size();
                if ((i > 0) && (formatDefs == null)) {
                    formatDefs = new ArrayList<XBFormatDef>();
                }
                if (i > 0) {
                    while (i > 0) {
                        formatDefs.add(new XBFormatDef());
                        i--;
                    }
                } else {
                    while (i < 0) {
                        formatDefs.remove(formatDefs.size() - 1);
                        i++;
                    }
                }
            }

            @Override
            public UBENatural getSize() {
                if (formatDefs == null) {
                    return new UBENat32();
                }
                return new UBENat32(formatDefs.size());
            }

            @Override
            public XBSerializable next() {
                // TODO detect later
                return formatDefs.get(position++);
                /*
                 XBTSerialMethod serialMethod = formatDefs.get(position++).getXBTSerializationMethod();
                 if (serialMethod instanceof XBTSerialSequenceListenerMethod) {
                 return ((XBTSerialSequenceListenerMethod) serialMethod).getXBSerialSequence();
                 } else {
                 throw new UnsupportedOperationException("Not supported yet.");
                 } */
            }

            @Override
            public void reset() {
                position = 0;
            }
        });
        // List Revision
        seq.listConsist(new XBSerialSequenceIList() {

            private int position = 0;

            @Override
            public void setSize(UBENatural count) {
                int i = count.getInt() - revisionDefs.size();
                if ((i > 0) && (revisionDefs == null)) {
                    revisionDefs = new ArrayList<XBRevisionDef>();
                }
                if (i > 0) {
                    while (i > 0) {
                        revisionDefs.add(new XBRevisionDef());
                        i--;
                    }
                } else {
                    while (i < 0) {
                        revisionDefs.remove(revisionDefs.size() - 1);
                        i++;
                    }
                }
            }

            @Override
            public UBENatural getSize() {
                if (revisionDefs == null) {
                    return new UBENat32();
                }
                return new UBENat32(revisionDefs.size());
            }

            @Override
            public XBSerializable next() {
                // TODO detect later
                return revisionDefs.get(position++);
                /*
                 XBTSerialMethod serialMethod = revisionDefs.get(position++).getXBTSerializationMethod();
                 if (serialMethod instanceof XBTSerialSequenceListenerMethod) {
                 return ((XBTSerialSequenceListenerMethod) serialMethod).getXBSerialSequence();
                 } else {
                 throw new UnsupportedOperationException("Not supported yet.");
                 } */
            }

            @Override
            public void reset() {
                position = 0;
            }
        });

        XBTSerialSequence serial = (XBTSerialSequence) serializationHandler;
        serial.sequenceXB(seq);
    }
}
