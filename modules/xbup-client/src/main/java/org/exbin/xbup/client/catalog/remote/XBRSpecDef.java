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
package org.exbin.xbup.client.catalog.remote;

import java.util.Optional;
import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.client.stub.XBPSpecStub;
import org.exbin.xbup.core.block.definition.XBParamType;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCRev;
import org.exbin.xbup.core.catalog.base.XBCSpecDef;

/**
 * Catalog remote specification definition entity.
 *
 * @version 0.2.1 2020/08/14
 * @author ExBin Project (http://exbin.org)
 */
public class XBRSpecDef extends XBRItem implements XBCSpecDef {

    private final XBPSpecStub specStub;

    public XBRSpecDef(XBCatalogServiceClient client, long id) {
        super(client, id);
        specStub = new XBPSpecStub(client);
    }

    @Override
    public XBRSpec getSpec() {
        Optional<XBCItem> parent = super.getParentItem();
        if (parent.isPresent()) {
            throw new IllegalStateException();
        }
        return new XBRSpec(client, parent.get().getId());
    }

    @Override
    public Optional<XBCRev> getTargetRev() {
        return Optional.ofNullable(specStub.getTarget(getId()));
    }

    @Override
    public XBParamType getType() {
        return null;
    }
}
