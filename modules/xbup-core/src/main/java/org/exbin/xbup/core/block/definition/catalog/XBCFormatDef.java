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
import org.exbin.xbup.core.block.declaration.catalog.XBCFormatDecl;
import org.exbin.xbup.core.block.declaration.catalog.XBCGroupDecl;
import org.exbin.xbup.core.block.definition.XBFormatDef;
import org.exbin.xbup.core.block.definition.XBFormatParam;
import org.exbin.xbup.core.block.definition.XBFormatParamConsist;
import org.exbin.xbup.core.block.definition.XBFormatParamJoin;
import org.exbin.xbup.core.block.definition.XBRevisionDef;
import org.exbin.xbup.core.catalog.XBCatalog;
import org.exbin.xbup.core.catalog.base.XBCFormatRev;
import org.exbin.xbup.core.catalog.base.XBCFormatSpec;
import org.exbin.xbup.core.catalog.base.XBCGroupRev;
import org.exbin.xbup.core.catalog.base.XBCSpecDef;
import org.exbin.xbup.core.catalog.base.service.XBCSpecService;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.serial.XBSerializable;
import org.exbin.xbup.core.serial.param.XBPInputSerialHandler;
import org.exbin.xbup.core.serial.param.XBPOutputSerialHandler;
import org.exbin.xbup.core.serial.param.XBPSequenceSerialHandler;
import org.exbin.xbup.core.serial.param.XBPSequenceSerializable;
import org.exbin.xbup.core.serial.param.XBPSerializable;
import org.exbin.xbup.core.serial.sequence.XBListConsistSerializable;
import org.exbin.xbup.core.ubnumber.UBENatural;
import org.exbin.xbup.core.ubnumber.type.UBENat32;

/**
 * XBUP level 1 format definition.
 *
 * @version 0.2.1 2017/05/27
 * @author ExBin Project (http://exbin.org)
 */
public class XBCFormatDef implements XBFormatDef, XBPSequenceSerializable {

    private final XBCatalog catalog;
    private final XBCFormatSpec formatSpec;

    public XBCFormatDef(XBCatalog catalog, XBCFormatSpec formatSpec) {
        this.catalog = catalog;
        this.formatSpec = formatSpec;
    }

    @Override
    public List<XBFormatParam> getFormatParams() {
        XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
        List<XBFormatParam> resultList = new ArrayList<>();
        List<XBCSpecDef> specDefs = specService.getSpecDefs(formatSpec);
        for (XBCSpecDef specDef : specDefs) {
            resultList.add(convertParam(specDef));
        }

        return resultList;
    }

    @Override
    public XBRevisionDef getRevisionDef() {
        return new XBCRevisionDef(catalog, formatSpec);
    }

    @Override
    public XBFormatParam getFormatParam(int paramIndex) {
        XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
        XBCSpecDef specDef = specService.findSpecDefByXB(formatSpec, paramIndex);
        if (specDef == null) {
            return null;
        }

        return convertParam(specDef);
    }

    @Override
    public long getParamsCount() {
        XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
        return specService.findMaxSpecDefXB(formatSpec);
    }

    public XBFormatParam convertParam(XBCSpecDef specDef) {
        switch (specDef.getType()) {
            case CONSIST: {
                return new XBFormatParamConsist(new XBCGroupDecl((XBCGroupRev) specDef.getTarget(), catalog));
            }
            case JOIN: {
                return new XBFormatParamJoin(new XBCFormatDecl((XBCFormatRev) specDef.getTarget(), catalog));
            }
        }

        throw new IllegalStateException("Unexpected specification definition type");
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBFixedBlockType(XBBasicBlockType.FORMAT_DEFINITION));
        serial.join(new XBCRevisionDef(catalog, formatSpec));
        serial.listConsist(new XBListConsistSerializable() {

            private int position = 0;

            @Override
            public UBENatural getSize() {
                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                return new UBENat32(specService.findMaxSpecDefXB(formatSpec));
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
                        XBFormatParam param = getFormatParam(position);
                        if (param instanceof XBFormatParamConsist) {
                            serializationHandler.append((XBFormatParamConsist) param);
                        } else if (param instanceof XBFormatParamJoin) {
                            serializationHandler.append((XBFormatParamJoin) param);
                        } else {
                            throw new IllegalStateException("Illegal format parameter " + position);
                        }
                    }
                };
            }
        });
        serial.end();
    }
}
