/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.client.catalog.remote;

import java.util.Optional;
import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.core.block.definition.XBParamType;
import org.exbin.xbup.core.catalog.base.XBCBlockListCons;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCRev;

/**
 * Catalog remote block list consist specification definition entity.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBRBlockListCons extends XBRSpecDef implements XBCBlockListCons {

    public XBRBlockListCons(XBCatalogServiceClient client, long id) {
        super(client, id);
    }

    @Override
    public Optional<XBCBlockRev> getTarget() {
        Optional<XBCRev> item = super.getTargetRev();
        return item.isPresent() ? Optional.of(new XBRBlockRev(((XBRRev) item.get()).client, item.get().getId())) : Optional.empty();
    }

    @Override
    public XBRBlockSpec getSpec() {
        XBRSpec item = super.getSpec();
        if (item == null) {
            return null;
        }
        return new XBRBlockSpec(item.client, item.getId());
    }

    @Override
    public XBParamType getType() {
        return XBParamType.LIST_CONSIST;
    }
}
