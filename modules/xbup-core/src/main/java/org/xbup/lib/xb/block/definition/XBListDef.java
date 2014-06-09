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
package org.xbup.lib.xb.block.definition;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.xbup.lib.xb.block.XBBasicBlockType;
import org.xbup.lib.xb.block.XBFixedBlockType;
import org.xbup.lib.xb.block.declaration.XBDBlockType;
import org.xbup.lib.xb.catalog.declaration.XBCPBlockDecl;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.serial.XBSerialHandler;
import org.xbup.lib.xb.serial.XBSerialMethod;
import org.xbup.lib.xb.serial.XBSerializable;
import org.xbup.lib.xb.serial.XBSerializationType;
import org.xbup.lib.xb.serial.sequence.XBSerialSequence;
import org.xbup.lib.xb.serial.sequence.XBTSerialSequence;
import org.xbup.lib.xb.serial.sequence.XBTSerialSequenceListenerMethod;
import org.xbup.lib.xb.serial.sequence.XBTSerialSequenceProviderMethod;
import org.xbup.lib.xb.ubnumber.UBNatural;
import org.xbup.lib.xb.ubnumber.type.UBNat32;

/**
 * XBUP level 1 block declaration.
 *
 * @version 0.1 wr21.0 2011/12/02
 * @author XBUP Project (http://xbup.org)
 * @deprecated will be removed in favor of XBParameter
 */
public class XBListDef implements XBSerializable {

    private UBNatural consistSkip = new UBNat32(0);

    /**
     * Creates a new instance of XBL1BlockDecl
     */
    public XBListDef() {
    }

    public int getConsistSkip() {
        return consistSkip.getInt();
    }

    public void setConsistSkip(int consistSkip) {
        this.consistSkip = new UBNat32(consistSkip);
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
                long[] xbRevisionBlockType = {1, 5};
                XBSerialSequence subSequence = new XBSerialSequence(new XBDBlockType(new XBCPBlockDecl(xbGroupLimitBlockType)), new UBNat32(consistSkip));

                XBTSerialSequence serial = (XBTSerialSequence) serializationHandler;
                serial.sequenceXB(subSequence);
            }
        });

        XBTSerialSequence serial = (XBTSerialSequence) serializationHandler;
        serial.sequenceXB(seq);
    }
}
