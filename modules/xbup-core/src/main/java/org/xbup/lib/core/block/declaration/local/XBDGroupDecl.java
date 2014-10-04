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

import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.declaration.XBGroupDecl;
import org.xbup.lib.core.block.definition.local.XBDGroupDef;
import org.xbup.lib.core.block.definition.local.XBDRevisionDef;
import org.xbup.lib.core.block.declaration.catalog.XBPBlockDecl;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.sequence.XBSerialSequence;
import org.xbup.lib.core.serial.sequence.XBSerialSequenceIList;
import org.xbup.lib.core.serial.sequence.XBTSequenceSerialHandler;
import org.xbup.lib.core.serial.sequence.XBTSequenceSerializable;
import org.xbup.lib.core.ubnumber.UBENatural;
import org.xbup.lib.core.ubnumber.type.UBENat32;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 1 group declaration.
 *
 * @version 0.1.24 2014/09/02
 * @author XBUP Project (http://xbup.org)
 */
public class XBDGroupDecl implements XBGroupDecl, XBTSequenceSerializable {

    private UBNat32 revision = new UBNat32(0);
    private List<XBBlockDecl> blocks = new ArrayList<>();
    private UBNat32 blocksLimit = new UBNat32(0);
    private List<XBDGroupDef> groupDefs;
    private List<XBDRevisionDef> revisionDefs;

    public XBDGroupDecl() {
    }

    /**
     * @return List of relevant Blocks.
     */
    public List<XBBlockDecl> getBlocks() {
        return blocks;
    }

    public Integer matchType(XBBlockType type) {
        for (int blockId = 0; blockId < blocks.size(); blockId++) {
            // TODO: if (blocks.get(blockId).matchType(type)) return blockId;
        }
        return null;
    }

    public long getRevision() {
        return revision.getInt();
    }

    public void setRevision(long revision) {
        this.revision = new UBNat32(revision);
    }

    public void setBlocks(List<XBBlockDecl> blocks) {
        this.blocks = blocks;
    }

    public int getBlocksLimit() {
        return blocksLimit.getInt();
    }

    public void setBlocksLimit(int blocksLimit) {
        this.blocksLimit = new UBNat32(blocksLimit);
    }

    @Override
    public void serializeXB(XBTSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
        XBSerialSequence seq = new XBSerialSequence(new XBFixedBlockType(XBBasicBlockType.GROUP_DECLARATION));

        // Join GroupsLimit (UBNatural)
        seq.join(new XBTSequenceSerializable() {

            @Override
            public void serializeXB(XBTSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
                long[] xbGroupLimitBlockType = {1, 5};
                XBSerialSequence seq = new XBSerialSequence(new XBDeclBlockType(new XBPBlockDecl(xbGroupLimitBlockType)), blocksLimit);
                // Join FormatSpecCatalogPath (UBPath)
                // seq.join(new UBPath32(getCatalogPath()));
                // Join Revision (UBNatural)
                seq.join(new XBTSequenceSerializable() {
                    @Override
                    public void serializeXB(XBTSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
                        long[] xbRevisionBlockType = {1, 5};
                        XBSerialSequence subSequence = new XBSerialSequence(new XBDeclBlockType(new XBPBlockDecl(xbRevisionBlockType)), revision);

                        serializationHandler.sequenceXB(subSequence);
                    }
                });

                // List GroupSpecification
                seq.listConsist(new XBSerialSequenceIList() {

                    private int position = 0;

                    @Override
                    public void setSize(UBENatural count) {
                        int i = count.getInt() - blocks.size();
                        if ((i > 0) && (blocks == null)) {
                            blocks = new ArrayList<>();
                        }

                        if (i > 0) {
                            while (i > 0) {
                                blocks.add(new XBDBlockDecl());
                                i--;
                            }
                        } else {
                            while (i < 0) {
                                blocks.remove(blocks.size() - 1);
                                i++;
                            }
                        }
                    }

                    @Override
                    public UBENatural getSize() {
                        if (blocks == null) {
                            return new UBENat32();
                        }
                        return new UBENat32(blocks.size());
                    }

                    @Override
                    public XBSerializable next() {
                        return (XBSerializable) blocks.get(position++);
                        /* TODO detect
                         XBTSerialMethod serialMethod = blocks.get(position++).getXBTSerializationMethod();
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
                        int i = count.getInt() - groupDefs.size();
                        if ((i > 0) && (groupDefs == null)) {
                            groupDefs = new ArrayList<>();
                        }
                        if (i > 0) {
                            while (i > 0) {
                                groupDefs.add(new XBDGroupDef());
                                i--;
                            }
                        } else {
                            while (i < 0) {
                                groupDefs.remove(groupDefs.size() - 1);
                                i++;
                            }
                        }
                    }

                    @Override
                    public UBENatural getSize() {
                        if (groupDefs == null) {
                            return new UBENat32();
                        }
                        return new UBENat32(groupDefs.size());
                    }

                    @Override
                    public XBSerializable next() {
                        return groupDefs.get(position++);
                        /* TODO
                         XBTSerialMethod serialMethod = groupDefs.get(position++).getXBTSerializationMethod();
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
                            revisionDefs = new ArrayList<>();
                        }
                        if (i > 0) {
                            while (i > 0) {
                                revisionDefs.add(new XBDRevisionDef());
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
                        return revisionDefs.get(position++);
                        /* TODO
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

                serializationHandler.sequenceXB(seq);
            }
        });

        serializationHandler.sequenceXB(seq);
    }
}
