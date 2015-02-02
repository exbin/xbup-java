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
package org.xbup.lib.core.block.definition.catalog;

import java.io.IOException;
import java.util.List;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.definition.XBRevisionDef;
import org.xbup.lib.core.block.definition.XBRevisionParam;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.service.XBCRevService;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.param.XBPInputSerialHandler;
import org.xbup.lib.core.serial.param.XBPOutputSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerializable;
import org.xbup.lib.core.serial.param.XBPSerializable;
import org.xbup.lib.core.serial.sequence.XBListConsistSerializable;
import org.xbup.lib.core.ubnumber.UBENatural;
import org.xbup.lib.core.ubnumber.type.UBENat32;

/**
 * XBUP level 1 revision definition.
 *
 * @version 0.1.25 2015/02/02
 * @author XBUP Project (http://xbup.org)
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getRevisionLimit(long revision) {
        XBCRevService revService = (XBCRevService) catalog.getCatalogService(XBCRevService.class);
        return (int) revService.getRevsLimitSum(spec, revision);
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBFixedBlockType(XBBasicBlockType.REVISION_DEFINITION));
        serial.listJoin(new XBListConsistSerializable() {

            private int position = 0;

            @Override
            public UBENatural getSize() {
                XBCRevService revService = (XBCRevService) catalog.getCatalogService(XBCRevService.class);
                return new UBENat32(revService.getRevsCount(spec));
            }

            @Override
            public void setSize(UBENatural count) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void reset() {
                position = 0;
            }

            @Override
            public XBSerializable next() {
                return new XBPSerializable() {

                    @Override
                    public void serializeFromXB(XBPInputSerialHandler serializationHandler) throws XBProcessingException, IOException {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public void serializeToXB(XBPOutputSerialHandler serializationHandler) throws XBProcessingException, IOException {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                };
            }
        });
        serial.end();
    }
}
