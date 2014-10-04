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
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.definition.XBBlockDef;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.sequence.XBSerialSequence;
import org.xbup.lib.core.serial.sequence.XBTSequenceSerialHandler;
import org.xbup.lib.core.serial.sequence.XBTSequenceSerializable;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 1 block declaration using existing block definition.
 *
 * @version 0.1.24 2014/09/02
 * @author XBUP Project (http://xbup.org)
 */
public class XBDBlockDecl implements XBBlockDecl, XBTSequenceSerializable {

    private XBBlockDef blockDef = null;
    private UBNatural revision = new UBNat32(0);

    public XBDBlockDecl() {
    }

    public XBDBlockDecl(XBBlockDef blockDef, UBNatural revision) {
        this.blockDef = blockDef;
        this.revision = revision;
    }

    public XBDBlockDecl(XBBlockDef blockDef, int revision) {
        this(blockDef, new UBNat32(revision));
    }

    public XBDBlockDecl(XBBlockDef blockDef) {
        this(blockDef, 0);
    }

    public boolean matchType(XBBlockType type) {
        // TODO: Process internal types
        if (type instanceof XBDeclBlockType) {
            return this.equals(((XBDeclBlockType) type).getBlockDecl());
        }
        return false;
    }

    /**
     * @return the revision
     */
    public int getRevision() {
        return revision.getInt();
    }

    /**
     * @param revision the revision to set
     */
    public void setRevision(int revision) {
        this.revision = new UBNat32(revision);
    }

    /**
     * @return the blocksLimit
     */
    public int getBlocksLimit() {
        return 0; // TODO blocksLimit.getInt();
    }

    /**
     * @param blocksLimit the blocksLimit to set
     */
    public void setBlocksLimit(int blocksLimit) {
        // TODO this.blocksLimit = new UBNat32(blocksLimit);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof XBDBlockDecl) {
            return obj == this;
        }

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return 1;
        /*        int hash = 5;
         hash = 61 * hash + Arrays.deepHashCode(this.catalogPath);
         return hash; */
    }

    /**
     * @return the blockDef
     */
    public XBBlockDef getBlockDef() {
        return blockDef;
    }

    /**
     * @param blockDef the blockDef to set
     */
    public void setBlockDef(XBBlockDef blockDef) {
        this.blockDef = blockDef;
    }

    @Override
    public void serializeXB(XBTSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
        long[] xbGroupLimitBlockType = {1, 5};
        long[] xbRevisionBlockType = {1, 5};
        XBSerialSequence seq = new XBSerialSequence(new XBFixedBlockType(XBBasicBlockType.BLOCK_DECLARATION));

        /*
        // Join GroupsLimit (UBNatural)
        seq.join(new XBTSequenceSerializable() {

            @Override
            public void serializeXB(XBTSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
                serializationHandler.sequenceXB(new XBSerialSequence(new XBDBlockType(new XBCBlockDecl(null, xbGroupLimitBlockType)), new UBNat32(getBlocksLimit())));
            }
        });

        // Join FormatSpecCatalogPath (UBPath)
        // seq.join(new UBPath32(getCatalogPath()));
        // Join Revision (UBNatural)
        seq.join(new XBTSequenceSerializable() {

            @Override
            public void serializeXB(XBTSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
                XBSerialSequence subSequence = new XBSerialSequence(new XBDBlockType(new XBCBlockDecl(null, xbRevisionBlockType)), revision));
                serializationHandler.sequenceXB(subSequence);
            }
        });
        
                
        if (blockDef != null) {
            // TODO: Move to format serialization
            // List GroupSpecification
            seq.listConsist(new XBSequenceIList() {

                private int position = 0;

                public void setSize(UBENatural count) {
                    List<XBBlockDecl> blocks = blockDef.getBlocks();
                    int i = count.getInt() - blocks.size();
                    if ((i > 0) && (blocks == null)) {
                        blocks = new ArrayList<XBBlockDecl>();
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

                public UBENatural getSize() {
                    List<XBBlockDecl> blocks = blockDef.getBlocks();
                    if (blocks == null) {
                        return new UBENat32();
                    }
                    return new UBENat32(blocks.size());
                }

                public XBSerializable get() {
                    return blockDef.getBlocks().get(position++).getXBSerialSequence();
                }

                public void reset() {
                    position = 0;
                }
            });

            // List FormatConstructor
            seq.listConsist(new XBSequenceIList() {

                private int position = 0;

                public void setSize(UBENatural count) {
                    List<XBBlockDef> blockDefs = blockDef.getBlockDefs();
                    int i = count.getInt() - blockDefs.size();
                    if ((i > 0) && (blockDefs == null)) {
                        blockDefs = new ArrayList<XBBlockDef>();
                    }
                    if (i > 0) {
                        while (i > 0) {
                            blockDefs.add(new XBDBlockDef());
                            i--;
                        }
                    } else {
                        while (i < 0) {
                            blockDefs.remove(blockDefs.size() - 1);
                            i++;
                        }
                    }
                }

                public UBENatural getSize() {
                    List<XBBlockDef> blockDefs = blockDef.getBlockDefs();
                    if (blockDefs == null) {
                        return new UBENat32();
                    }
                    return new UBENat32(blockDefs.size());
                }

                public XBTSerializable get() {
                    return ((XBDBlockDef) blockDef.getBlockDefs().get(position++)).getXBSerialSequence();
                }

                public void reset() {
                    position = 0;
                }
            });

            // List Revision
            seq.listConsist(new XBSequenceIList() {

                private int position = 0;

                public void setSize(UBENatural count) {
                    List<XBRevisionDef> revisionDefs = blockDef.getRevisionDefs();
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

                public UBENatural getSize() {
                    List<XBRevisionDef> revisionDefs = blockDef.getRevisionDefs();
                    if (revisionDefs == null) {
                        return new UBENat32();
                    }

                    return new UBENat32(revisionDefs.size());
                }

                public XBTSerializable get() {
                    return blockDef.getRevisionDefs().get(position++).getXBSerialSequence();
                }

                public void reset() {
                    position = 0;
                }
            });
        }*/

        serializationHandler.sequenceXB(seq);
    }
}
