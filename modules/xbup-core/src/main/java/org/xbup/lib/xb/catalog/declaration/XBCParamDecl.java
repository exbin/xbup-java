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
package org.xbup.lib.xb.catalog.declaration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.xbup.lib.xb.block.XBBasicBlockType;
import org.xbup.lib.xb.block.XBFixedBlockType;
import org.xbup.lib.xb.block.declaration.XBBlockDecl;
import org.xbup.lib.xb.block.declaration.XBDParamDecl;
import org.xbup.lib.xb.block.declaration.XBParamDecl;
import org.xbup.lib.xb.block.declaration.XBParamType;
import org.xbup.lib.xb.catalog.XBACatalog;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.serial.XBSerialHandler;
import org.xbup.lib.xb.serial.XBSerialMethod;
import org.xbup.lib.xb.serial.XBSerializable;
import org.xbup.lib.xb.serial.XBSerializationType;
import org.xbup.lib.xb.serial.sequence.XBSerialSequence;
import org.xbup.lib.xb.serial.sequence.XBTSerialSequence;
import org.xbup.lib.xb.serial.sequence.XBTSerialSequenceListenerMethod;
import org.xbup.lib.xb.serial.sequence.XBTSerialSequenceProviderMethod;

/**
 * XBUP level 1 parameter declaration.
 *
 * Parameter is one of four modes: Join, Consist, Join List, Consist List.
 * Without declaration (blockDecl) it means single atribute for join and data
 * block for consist mode and their lists respectively.
 *
 * @version 0.1 wr23.0 2014/03/04
 * @author XBUP Project (http://xbup.org)
 */
public class XBCParamDecl implements XBParamDecl, XBSerializable {

    private XBACatalog catalog;
    private XBCBlockDecl blockDecl;
    private boolean listFlag;
    private boolean joinFlag;

    /**
     * Creates a new instance of XBCParamDecl
     */
    public XBCParamDecl(XBACatalog catalog, XBCBlockDecl blockDecl) {
        this.catalog = catalog;
        this.blockDecl = blockDecl;
        listFlag = false;
        joinFlag = false;
    }

    /**
     * @return the blockDef
     */
    @Override
    public XBBlockDecl getBlockDecl() {
        return blockDecl;
    }

    /**
     * @param blockDecl the blockDef to set
     */
    public void setBlockDecl(XBBlockDecl blockDecl) {
        if (blockDecl instanceof XBCBlockDecl) {
            this.blockDecl = (XBCBlockDecl) blockDecl;
        } else {
            this.blockDecl = null;
        } // Or throw exception?
    }

    /**
     * @return the listFlag
     */
    @Override
    public boolean isListFlag() {
        return listFlag;
    }

    /**
     * @param listFlag the listFlag to set
     */
    public void setListFlag(boolean listFlag) {
        this.listFlag = listFlag;
    }

    /**
     * @return the joinFlag
     */
    @Override
    public boolean isJoinFlag() {
        return joinFlag;
    }

    /**
     * @param joinFlag the joinFlag to set
     */
    public void setJoinFlag(boolean joinFlag) {
        this.joinFlag = joinFlag;
    }

    @Override
    public XBParamType getParamType() {
        return XBDParamDecl.convertParamType(joinFlag, listFlag);
    }

    @Override
    public XBParamDecl convertToNonList() {
        XBCParamDecl memberDecl = new XBCParamDecl(catalog, blockDecl);
        memberDecl.joinFlag = joinFlag;
        return memberDecl;
    }

    @Override
    public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
        return serialType == XBSerializationType.FROM_XB
                ? Arrays.asList(new XBSerialMethod[]{new XBTSerialSequenceProviderMethod()})
                : Arrays.asList(new XBSerialMethod[]{new XBTSerialSequenceListenerMethod()});
    }

    @Override
    public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
        XBSerialSequence seq = new XBSerialSequence(new XBFixedBlockType(XBBasicBlockType.GROUP_DECLARATION));
        // Join ConsistSkip (UBNatural)
        // seq.join(new XBSequence(new XBDBlockType(new XBCPBlockDecl(xbGroupLimitBlockType)), consistSkip));

        XBTSerialSequence serial = (XBTSerialSequence) serializationHandler;
        serial.sequenceXB(seq);
    }
}
