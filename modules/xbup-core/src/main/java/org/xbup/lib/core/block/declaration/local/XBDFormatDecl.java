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
import org.xbup.lib.core.block.definition.local.XBDFormatDef;
import java.util.List;
import org.xbup.lib.core.block.declaration.XBFormatDecl;
import org.xbup.lib.core.block.declaration.XBGroupDecl;
import org.xbup.lib.core.block.definition.XBFormatDef;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.sequence.XBTSequenceSerialHandler;
import org.xbup.lib.core.serial.sequence.XBTSequenceSerializable;

/**
 * XBUP level 1 format declaration.
 *
 * @version 0.1.24 2014/12/01
 * @author XBUP Project (http://xbup.org)
 */
public class XBDFormatDecl implements XBFormatDecl, XBTSequenceSerializable {

    private long revision;
    private XBFormatDef formatDef;

    public XBDFormatDecl() {
    }

    public XBDFormatDecl(XBFormatDef formatDef) {
        this.formatDef = formatDef;
    }
    
    public XBDFormatDecl(XBGroupDecl groupDecl) {
        formatDef = new XBDFormatDef(groupDecl);
    }
    
    
    /*
    public static XBDFormatDecl processDeclaration(XBTBlock specBlock) {
        if (specBlock.getDataMode() == XBBlockDataMode.NODE_BLOCK) {
            if (specBlock.getAttributesCount() > 1) {
                if ((specBlock.getBlockType().getAsBasicType() == XBBasicBlockType.FORMAT_DECLARATION)) {
                    XBDFormatDecl decl = new XBDFormatDecl();
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
    }*/

    @Override
    public long getRevision() {
        return revision;
    }

    public void setRevision(long revision) {
        this.revision = revision;
    }

    @Override
    public XBFormatDef getFormatDef() {
        return formatDef;
    }

    public void setFormatDef(XBFormatDef formatDef) {
        this.formatDef = formatDef;
    }

    public List<XBGroupDecl> getGroups() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public int getGroupsLimit() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void serializeXB(XBTSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
//    @Override
//    public void serializeXB(XBTSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
//        XBSerialSequence seq = new XBSerialSequence(new XBFixedBlockType(XBBasicBlockType.FORMAT_DECLARATION));
//
//        // Join GroupsLimit (UBNatural)
//        seq.join(new XBTSequenceSerializable() {
//
//            @Override
//            public void serializeXB(XBTSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
//                long[] xbGroupLimitBlockType = {1, 5};
//                XBSerialSequence subSequence = new XBSerialSequence(new XBDeclBlockType(new XBPBlockDecl(xbGroupLimitBlockType)));
//                subSequence.join(groupsLimit);
//
//                serializationHandler.sequenceXB(subSequence);
//            }
//        });
//
//        // Join FormatSpecCatalogPath (UBPath)
//        // seq.join(catalogPath);
//        // Join Revision (UBNatural)
//        seq.join(new XBTSequenceSerializable() {
//
//            @Override
//            public void serializeXB(XBTSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
//                long[] xbRevisionBlockType = {1, 5};
//                XBSerialSequence subSequence = new XBSerialSequence(new XBDeclBlockType(new XBPBlockDecl(xbRevisionBlockType)));
//                subSequence.join(revision);
//
//                serializationHandler.sequenceXB(subSequence);
//            }
//        });
//
//        // List GroupSpecification
//        seq.listConsist(new XBSerialSequenceIList() {
//
//            private int position = 0;
//
//            @Override
//            public void setSize(UBENatural count) {
//                int i = count.getInt() - groups.size();
//                if ((i > 0) && (groups == null)) {
//                    groups = new ArrayList<>();
//                }
//                if (i > 0) {
//                    while (i > 0) {
//                        groups.add(new XBDGroupDecl());
//                        i--;
//                    }
//                } else {
//                    while (i < 0) {
//                        groups.remove(groups.size() - 1);
//                        i++;
//                    }
//                }
//            }
//
//            @Override
//            public UBENatural getSize() {
//                if (groups == null) {
//                    return new UBENat32();
//                }
//                return new UBENat32(groups.size());
//            }
//
//            @Override
//            public XBSerializable next() {
//                // TODO detect serialization
//                XBGroupDecl group = groups.get(position++);
//                if (group instanceof XBCGroupDecl) {
//                    return (XBCGroupDecl) group;
//                } else {
//                    throw new UnsupportedOperationException("Not supported yet.");
//                }
//
//                /*
//                 XBTSerialMethod serialMethod = groups.get(position++).getXBTSerializationMethod();
//                 if (serialMethod instanceof XBTSerialSequenceListenerMethod) {
//                 return ((XBTSerialSequenceListenerMethod) serialMethod).getXBSerialSequence();
//                 } else {
//                 throw new UnsupportedOperationException("Not supported yet.");
//                 } */
//            }
//
//            @Override
//            public void reset() {
//                position = 0;
//            }
//        });
//
//        // List FormatConstructor
//        seq.listConsist(new XBSerialSequenceIList() {
//
//            private int position = 0;
//
//            @Override
//            public void setSize(UBENatural count) {
//                int i = count.getInt() - formatDefs.size();
//                if ((i > 0) && (formatDefs == null)) {
//                    formatDefs = new ArrayList<>();
//                }
//                if (i > 0) {
//                    while (i > 0) {
//                        formatDefs.add(new XBDFormatDef());
//                        i--;
//                    }
//                } else {
//                    while (i < 0) {
//                        formatDefs.remove(formatDefs.size() - 1);
//                        i++;
//                    }
//                }
//            }
//
//            @Override
//            public UBENatural getSize() {
//                if (formatDefs == null) {
//                    return new UBENat32();
//                }
//                return new UBENat32(formatDefs.size());
//            }
//
//            @Override
//            public XBSerializable next() {
//                // TODO detect later
//                return formatDefs.get(position++);
//                /*
//                 XBTSerialMethod serialMethod = formatDefs.get(position++).getXBTSerializationMethod();
//                 if (serialMethod instanceof XBTSerialSequenceListenerMethod) {
//                 return ((XBTSerialSequenceListenerMethod) serialMethod).getXBSerialSequence();
//                 } else {
//                 throw new UnsupportedOperationException("Not supported yet.");
//                 } */
//            }
//
//            @Override
//            public void reset() {
//                position = 0;
//            }
//        });
//        // List Revision
//        seq.listConsist(new XBSerialSequenceIList() {
//
//            private int position = 0;
//
//            @Override
//            public void setSize(UBENatural count) {
//                int i = count.getInt() - revisionDefs.size();
//                if ((i > 0) && (revisionDefs == null)) {
//                    revisionDefs = new ArrayList<>();
//                }
//                if (i > 0) {
//                    while (i > 0) {
//                        revisionDefs.add(new XBDRevisionDef());
//                        i--;
//                    }
//                } else {
//                    while (i < 0) {
//                        revisionDefs.remove(revisionDefs.size() - 1);
//                        i++;
//                    }
//                }
//            }
//
//            @Override
//            public UBENatural getSize() {
//                if (revisionDefs == null) {
//                    return new UBENat32();
//                }
//                return new UBENat32(revisionDefs.size());
//            }
//
//            @Override
//            public XBSerializable next() {
//                // TODO detect later
//                return revisionDefs.get(position++);
//                /*
//                 XBTSerialMethod serialMethod = revisionDefs.get(position++).getXBTSerializationMethod();
//                 if (serialMethod instanceof XBTSerialSequenceListenerMethod) {
//                 return ((XBTSerialSequenceListenerMethod) serialMethod).getXBSerialSequence();
//                 } else {
//                 throw new UnsupportedOperationException("Not supported yet.");
//                 } */
//            }
//
//            @Override
//            public void reset() {
//                position = 0;
//            }
//        });
//
//        serializationHandler.sequenceXB(seq);
//    }
}
