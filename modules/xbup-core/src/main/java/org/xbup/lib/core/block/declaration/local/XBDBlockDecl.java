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

import org.xbup.lib.core.block.definition.XBBlockParamJoin;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import java.io.IOException;
import java.util.List;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.declaration.catalog.XBPBlockDecl;
import org.xbup.lib.core.block.definition.XBBlockDef;
import org.xbup.lib.core.block.definition.XBBlockParam;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.sequence.XBSerialSequence;
import org.xbup.lib.core.serial.sequence.XBSerialSequenceIList;
import org.xbup.lib.core.serial.sequence.XBTSequenceSerialHandler;
import org.xbup.lib.core.serial.sequence.XBTSequenceSerializable;
import org.xbup.lib.core.ubnumber.UBENatural;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBENat32;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 1 block declaration using existing block definition.
 *
 * @version 0.1.24 2014/10/21
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

    @Override
    public long getRevision() {
        return revision.getInt();
    }

    public void setRevision(int revision) {
        this.revision = new UBNat32(revision);
    }

    public int getBlocksLimit() {
        return 0; // TODO blocksLimit.getInt();
    }

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

    @Override
    public XBBlockDef getBlockDef() {
        return blockDef;
    }

    public void setBlockDef(XBBlockDef blockDef) {
        this.blockDef = blockDef;
    }

    @Override
    public void serializeXB(XBTSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
        final long[] xbGroupLimitBlockType = {1, 5};
        final long[] xbRevisionBlockType = {1, 5};
        XBSerialSequence seq = new XBSerialSequence(new XBFixedBlockType(XBBasicBlockType.BLOCK_DECLARATION));

        // Join GroupsLimit (UBNatural)
        seq.join(new XBTSequenceSerializable() {

            @Override
            public void serializeXB(XBTSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
                serializationHandler.appendSequence(new XBSerialSequence(new XBDeclBlockType(new XBPBlockDecl(xbGroupLimitBlockType, getBlocksLimit()))));
            }
        });

        // Join FormatSpecCatalogPath (UBPath)
        // seq.join(new UBPath32(getCatalogPath()));
        // Join Revision (UBNatural)
        seq.join(new XBTSequenceSerializable() {

            @Override
            public void serializeXB(XBTSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
                XBSerialSequence subSequence = new XBSerialSequence(new XBDeclBlockType(new XBPBlockDecl(xbRevisionBlockType, revision)));
                serializationHandler.appendSequence(subSequence);
            }
        });

        if (blockDef != null) {
            // TODO: Move to format serialization
            // List GroupSpecification
            seq.listConsist(new XBSerialSequenceIList() {

                private int position = 0;

                @Override
                public void setSize(UBENatural count) {
                    List<XBBlockParam> paramDecls = blockDef.getBlockParams();
                    int i = count.getInt() - paramDecls.size();

                    if (i > 0) {
                        while (i > 0) {
                            paramDecls.add(new XBBlockParamJoin());
                            i--;
                        }
                    } else {
                        while (i < 0) {
                            paramDecls.remove(paramDecls.size() - 1);
                            i++;
                        }
                    }
                }

                @Override
                public UBENatural getSize() {
                    List<XBBlockParam> blocks = blockDef.getBlockParams();
                    if (blocks == null) {
                        return new UBENat32();
                    }

                    return new UBENat32(blocks.size());
                }

                @Override
                public XBSerializable next() {
                    throw new UnsupportedOperationException("Not supported yet.");
                    // return (XBBlockParamJoin) blockDef.getParamDecl(position++);
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
                    throw new UnsupportedOperationException("Not supported yet.");
                    /*
                     List<XBDRevisionDef> blockDefs = blockDef.getRevisionDefs();
                     int i = count.getInt() - blockDefs.size();

                     if (i > 0) {
                     while (i > 0) {
                     blockDefs.add(new XBDRevisionDef());
                     i--;
                     }
                     } else {
                     while (i < 0) {
                     blockDefs.remove(blockDefs.size() - 1);
                     i++;
                     }
                     } */
                }

                @Override
                public UBENatural getSize() {
                    throw new UnsupportedOperationException("Not supported yet.");
                    /*List<XBDRevisionDef> blockDefs = blockDef.getRevisionDefs();
                     if (blockDefs == null) {
                     return new UBENat32();
                     }
                     return new UBENat32(blockDefs.size()); */
                }

                @Override
                public XBSerializable next() {
                    throw new UnsupportedOperationException("Not supported yet.");
                    // return ((XBDRevisionDef) blockDef.getRevisionDefs().get(position++));
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
                    throw new UnsupportedOperationException("Not supported yet.");
                    /*
                     List<XBDRevisionDef> revisionDefs = blockDef.getRevisionDefs();
                     int i = count.getInt() - revisionDefs.size();

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
                     } */
                }

                @Override
                public UBENatural getSize() {
                    throw new UnsupportedOperationException("Not supported yet.");
                    /*
                     List<XBDRevisionDef> revisionDefs = blockDef.getRevisionDefs();
                     if (revisionDefs == null) {
                     return new UBENat32();
                     }

                     return new UBENat32(revisionDefs.size());
                     */
                }

                @Override
                public XBSerializable next() {
                    throw new UnsupportedOperationException("Not supported yet.");
                    // return blockDef.getRevisionDefs().get(position++);
                }

                @Override
                public void reset() {
                    position = 0;
                }
            });
        }

        serializationHandler.appendSequence(seq);
    }
}
