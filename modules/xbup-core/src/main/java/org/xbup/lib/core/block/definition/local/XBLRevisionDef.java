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
package org.xbup.lib.core.block.definition.local;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.definition.XBRevisionDef;
import org.xbup.lib.core.block.definition.XBRevisionParam;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.param.XBPSequenceSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerializable;
import org.xbup.lib.core.serial.sequence.XBListJoinSerializable;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 1 local group definition.
 *
 * @version 0.1.25 2015/02/02
 * @author XBUP Project (http://xbup.org)
 */
public class XBLRevisionDef implements XBRevisionDef, XBPSequenceSerializable {

    private final List<XBRevisionParam> revParams;

    public XBLRevisionDef() {
        revParams = new ArrayList<>();
    }

    @Override
    public List<XBRevisionParam> getRevParams() {
        return revParams;
    }

    @Override
    public int getRevisionLimit(long revision) {
        if (revision > revParams.size()) {
            revision = revParams.size() - 1;
        }

        int limit = 0;
        for (int index = 0; index <= revision; index++) {
            limit += revParams.get(index).getParamCount();
        }

        return limit;
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBFixedBlockType(XBBasicBlockType.REVISION_DEFINITION));
        serial.listJoin(new XBListJoinSerializable() {

            private int position = 0;

            @Override
            public UBNatural getSize() {
                return new UBNat32(revParams.size());
            }

            @Override
            public void setSize(UBNatural count) {
                revParams.clear();
                int size = count.getInt();
                for (int i = 0; i < size; i++) {
                    revParams.add(new XBRevisionParam());
                }
            }

            @Override
            public void reset() {
                position = 0;
            }

            @Override
            public XBSerializable next() {
                XBRevisionParam revParam = revParams.get(position);
                position++;
                return revParam;
            }
        });
        serial.end();
    }
}
