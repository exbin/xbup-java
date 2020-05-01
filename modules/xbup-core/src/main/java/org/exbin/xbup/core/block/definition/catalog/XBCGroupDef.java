/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.block.definition.catalog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.exbin.xbup.core.block.XBBasicBlockType;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.block.declaration.catalog.XBCBlockDecl;
import org.exbin.xbup.core.block.declaration.catalog.XBCGroupDecl;
import org.exbin.xbup.core.block.definition.XBFormatParamJoin;
import org.exbin.xbup.core.block.definition.XBGroupDef;
import org.exbin.xbup.core.block.definition.XBGroupParam;
import org.exbin.xbup.core.block.definition.XBGroupParamConsist;
import org.exbin.xbup.core.block.definition.XBGroupParamJoin;
import org.exbin.xbup.core.block.definition.XBRevisionDef;
import org.exbin.xbup.core.catalog.XBCatalog;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCGroupRev;
import org.exbin.xbup.core.catalog.base.XBCGroupSpec;
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
 * XBUP level 1 group definition.
 *
 * @version 0.2.0 2017/01/07
 * @author ExBin Project (http://exbin.org)
 */
public class XBCGroupDef implements XBGroupDef, XBPSequenceSerializable {

    private final XBCatalog catalog;
    private final XBCGroupSpec groupSpec;

    public XBCGroupDef(XBCatalog catalog, XBCGroupSpec groupSpec) {
        this.catalog = catalog;
        this.groupSpec = groupSpec;
    }

    @Override
    public List<XBGroupParam> getGroupParams() {
        XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
        List<XBGroupParam> resultList = new ArrayList<>();
        List<XBCSpecDef> specDefs = specService.getSpecDefs(groupSpec);
        for (XBCSpecDef specDef : specDefs) {
            resultList.add(convertParam(specDef));
        }

        return resultList;
    }

    @Override
    public XBRevisionDef getRevisionDef() {
        return new XBCRevisionDef(catalog, groupSpec);
    }

    @Override
    public XBGroupParam getGroupParam(int paramIndex) {
        XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
        XBCSpecDef specDef = specService.findSpecDefByXB(groupSpec, paramIndex);
        if (specDef == null) {
            return null;
        }

        return convertParam(specDef);
    }

    @Override
    public long getParamsCount() {
        XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
        return specService.findMaxSpecDefXB(groupSpec);
    }

    public XBGroupParam convertParam(XBCSpecDef specDef) {
        switch (specDef.getType()) {
            case CONSIST: {
                return new XBGroupParamConsist(new XBCBlockDecl((XBCBlockRev) specDef.getTarget(), catalog));
            }
            case JOIN: {
                return new XBGroupParamJoin(new XBCGroupDecl((XBCGroupRev) specDef.getTarget(), catalog));
            }
        }

        throw new IllegalStateException("Unexpected specification definition type");
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBFixedBlockType(XBBasicBlockType.GROUP_DEFINITION));
        serial.join(new XBCRevisionDef(catalog, groupSpec));
        serial.listConsist(new XBListConsistSerializable() {

            private int position = 0;

            @Override
            public UBENatural getSize() {
                XBCSpecService specService = catalog.getCatalogService(XBCSpecService.class);
                return new UBENat32(specService.findMaxSpecDefXB(groupSpec));
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
                        XBGroupParam param = getGroupParam(position);
                        if (param instanceof XBGroupParamConsist) {
                            serializationHandler.append((XBGroupParamConsist) param);
                        } else if (param instanceof XBFormatParamJoin) {
                            serializationHandler.append((XBGroupParamJoin) param);
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
