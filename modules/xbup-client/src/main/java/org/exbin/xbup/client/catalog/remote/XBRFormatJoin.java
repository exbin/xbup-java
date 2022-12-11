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
import org.exbin.xbup.core.catalog.base.XBCFormatJoin;
import org.exbin.xbup.core.catalog.base.XBCRev;

/**
 * Catalog remote format join specification definition entity.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBRFormatJoin extends XBRJoinDef implements XBCFormatJoin {

    public XBRFormatJoin(XBCatalogServiceClient client, long id) {
        super(client, id);
    }

    @Override
    public XBRFormatRev getTarget() {
        Optional<XBCRev> item = super.getTargetRev();
        if (!item.isPresent()) {
            throw new IllegalStateException();
        }
        return new XBRFormatRev(((XBRRev) item.get()).client, item.get().getId());
    }

    @Override
    public XBRFormatSpec getSpec() {
        XBRSpec item = super.getSpec();
        return new XBRFormatSpec(item.client, item.getId());
    }

    @Override
    public XBParamType getType() {
        return XBParamType.JOIN;
    }
}
