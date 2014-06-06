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
import java.util.Arrays;
import java.util.List;
import org.xbup.lib.xb.block.XBBasicBlockType;
import org.xbup.lib.xb.block.XBFixedBlockType;
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
 * Without declaration (blockDef) it means single atribute for join and data
 * block for consist mode and their lists respectively.
 *
 * @version 0.1 wr23.0 2014/03/04
 * @author XBUP Project (http://xbup.org)
 */
public class XBDParamDecl implements XBParamDecl, XBSerializable {

    private XBBlockDecl blockDecl;
    private boolean listFlag;
    private boolean joinFlag;

    /**
     * Creates a new instance of XBDParamDecl.
     * 
     * @param blockDecl block declaration
     */
    public XBDParamDecl(XBBlockDecl blockDecl) {
        this.blockDecl = blockDecl;
    }

    @Override
    public boolean isListFlag() {
        return listFlag;
    }

    public void setListFlag(boolean listFlag) {
        this.listFlag = listFlag;
    }

    @Override
    public boolean isJoinFlag() {
        return joinFlag;
    }

    public void setJoinFlag(boolean joinFlag) {
        this.joinFlag = joinFlag;
    }

    /**
     * Convert flags into parameter type.
     *
     * @param joinFlag join flag
     * @param listFlag list flag
     * @return parameter type
     */
    public static XBParamType convertParamType(boolean joinFlag, boolean listFlag) {
        if (joinFlag) {
            if (listFlag) { return XBParamType.LIST_JOIN; } else {
                return XBParamType.JOIN;
            }
        } else {
            if (listFlag) { return XBParamType.LIST_CONSIST; } else {
                return XBParamType.CONSIST;
            }
        }
    }

    @Override
    public XBParamType getParamType() {
        return convertParamType(joinFlag, listFlag);
    }

    @Override
    public XBParamDecl convertToNonList() {
        XBDParamDecl memberDecl = new XBDParamDecl(blockDecl);
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
        long[] xbGroupLimitBlockType = {1,5};
        long[] xbRevisionBlockType = {1,5};
        XBSerialSequence seq = new XBSerialSequence(new XBFixedBlockType(XBBasicBlockType.GROUP_DECLARATION));
        // Join ConsistSkip (UBNatural)
        // seq.join(new XBSequence(new XBDBlockType(new XBCPBlockDecl(xbGroupLimitBlockType)), consistSkip));

        XBTSerialSequence serial = (XBTSerialSequence) serializationHandler;
        serial.sequenceXB(seq);
    }

    @Override
    public XBBlockDecl getBlockDecl() {
        return blockDecl;
    }

    public void setBlockDecl(XBBlockDecl blockDecl) {
        this.blockDecl = blockDecl;
    }
}
