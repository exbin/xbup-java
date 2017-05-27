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
import org.exbin.xbup.core.block.declaration.catalog.XBCBlockDecl;
import org.exbin.xbup.core.block.definition.XBBlockDef;
import org.exbin.xbup.core.block.definition.XBBlockParam;
import org.exbin.xbup.core.block.definition.XBBlockParamConsist;
import org.exbin.xbup.core.block.definition.XBBlockParamJoin;
import org.exbin.xbup.core.block.definition.XBBlockParamListConsist;
import org.exbin.xbup.core.block.definition.XBBlockParamListJoin;
import org.exbin.xbup.core.block.definition.XBRevisionDef;
import org.exbin.xbup.core.catalog.XBCatalog;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
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
 * XBUP level 1 block definition.
 *
 * @version 0.2.0 2017/01/07
 * @author ExBin Project (http://exbin.org)
 */
public class XBCBlockDef implements XBBlockDef, XBPSequenceSerializable {

    private final XBCatalog catalog;
    private final XBCBlockSpec blockSpec;

    public XBCBlockDef(XBCatalog catalog, XBCBlockSpec blockSpec) {
        this.catalog = catalog;
        this.blockSpec = blockSpec;
    }

    @Override
    public List<XBBlockParam> getBlockParams() {
        List<XBBlockParam> resultList = new ArrayList<>();
        XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
        List<XBCSpecDef> specDefs = specService.getSpecDefs(blockSpec);
        for (XBCSpecDef specDef : specDefs) {
            resultList.add(convertParam(specDef));
        }

        return resultList;
    }

    @Override
    public long getParamsCount() {
        XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
        return specService.findMaxSpecDefXB(blockSpec);
    }

    @Override
    public XBRevisionDef getRevisionDef() {
        return new XBCRevisionDef(catalog, blockSpec);
    }

    @Override
    public XBBlockParam getBlockParam(int paramIndex) {
        XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
        XBCSpecDef specDef = specService.findSpecDefByXB(blockSpec, paramIndex);
        if (specDef == null) {
            return null;
        }

        return convertParam(specDef);
    }

    public XBBlockParam convertParam(XBCSpecDef specDef) {
        if (specDef == null) {
            return null;
        }

        switch (specDef.getType()) {
            case CONSIST: {
                return new XBBlockParamConsist(new XBCBlockDecl((XBCBlockRev) specDef.getTarget(), catalog));
            }
            case JOIN: {
                return new XBBlockParamJoin(new XBCBlockDecl((XBCBlockRev) specDef.getTarget(), catalog));
            }
            case LIST_CONSIST: {
                return new XBBlockParamListConsist(new XBCBlockDecl((XBCBlockRev) specDef.getTarget(), catalog));
            }
            case LIST_JOIN: {
                return new XBBlockParamListJoin(new XBCBlockDecl((XBCBlockRev) specDef.getTarget(), catalog));
            }
        }

        throw new IllegalStateException("Unexpected specification definition type");
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBFixedBlockType(XBBasicBlockType.BLOCK_DEFINITION));
        serial.join(new XBCRevisionDef(catalog, blockSpec));
        serial.listConsist(new XBListConsistSerializable() {

            private int position = 0;

            @Override
            public UBENatural getSize() {
                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                Long maxSpecDefXB = specService.findMaxSpecDefXB(blockSpec);
                return new UBENat32(maxSpecDefXB == null ? 0 : maxSpecDefXB);
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
                        XBBlockParam param = getBlockParam(position);
                        if (param instanceof XBBlockParamConsist) {
                            serializationHandler.append((XBBlockParamConsist) param);
                        } else if (param instanceof XBBlockParamJoin) {
                            serializationHandler.append((XBBlockParamJoin) param);
                        } else if (param instanceof XBBlockParamListConsist) {
                            serializationHandler.append((XBBlockParamListConsist) param);
                        } else if (param instanceof XBBlockParamListJoin) {
                            serializationHandler.append((XBBlockParamListJoin) param);
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
