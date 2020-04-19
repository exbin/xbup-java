/*
 * Copyright (C) ExBin Project
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
package org.exbin.xbup.core.block.definition.catalog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.exbin.xbup.core.block.XBBasicBlockType;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.block.definition.XBRevisionDef;
import org.exbin.xbup.core.block.definition.XBRevisionParam;
import org.exbin.xbup.core.catalog.XBCatalog;
import org.exbin.xbup.core.catalog.base.XBCRev;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.service.XBCRevService;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.serial.XBSerializable;
import org.exbin.xbup.core.serial.param.XBPSequenceSerialHandler;
import org.exbin.xbup.core.serial.param.XBPSequenceSerializable;
import org.exbin.xbup.core.serial.sequence.XBListJoinSerializable;
import org.exbin.xbup.core.ubnumber.UBNatural;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * XBUP level 1 revision definition.
 *
 * @version 0.2.1 2017/05/27
 * @author ExBin Project (http://exbin.org)
 */
public class XBCRevisionDef implements XBRevisionDef, XBPSequenceSerializable {

    private final XBCatalog catalog;
    private final XBCSpec spec;

    public XBCRevisionDef(XBCatalog catalog, XBCSpec spec) {
        this.catalog = catalog;
        this.spec = spec;
    }

    @Override
    public List<XBRevisionParam> getRevParams() {
        List<XBRevisionParam> result = new ArrayList<>();
        XBCRevService revService = catalog.getCatalogService(XBCRevService.class);
        for (XBCRev rev : (List<XBCRev>) revService.getRevs(spec)) {
            result.add(new XBRevisionParam(rev.getXBLimit()));
        }

        return result;
    }

    public XBRevisionParam getRevParam(int position) {
        XBCRevService revService = catalog.getCatalogService(XBCRevService.class);
        XBCRev rev = revService.getRev(spec, position);
        XBRevisionParam revisionParam = new XBRevisionParam(rev.getXBLimit());
        return revisionParam;
    }

    @Override
    public int getRevisionLimit(long revision) {
        XBCRevService revService = catalog.getCatalogService(XBCRevService.class);
        return (int) revService.getRevsLimitSum(spec, revision);
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBFixedBlockType(XBBasicBlockType.REVISION_DEFINITION));
        serial.listJoin(new XBListJoinSerializable() {

            private int position = 0;

            @Override
            public UBNatural getSize() {
                XBCRevService revService = catalog.getCatalogService(XBCRevService.class);
                return new UBNat32(revService.getRevsCount(spec));
            }

            @Override
            public void setSize(UBNatural count) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void reset() {
                position = 0;
            }

            @Override
            public XBSerializable next() {
                XBRevisionParam revParam = getRevParam(position);
                position++;
                return revParam;
            }
        });
        serial.end();
    }
}
