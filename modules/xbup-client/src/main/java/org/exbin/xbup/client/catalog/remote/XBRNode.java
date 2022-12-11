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
import javax.annotation.Nonnull;
import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCNode;

/**
 * Catalog remote node entity.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBRNode extends XBRItem implements XBCNode {

    public XBRNode(XBCatalogServiceClient client, long id) {
        super(client, id);
    }

    @Nonnull
    @Override
    public Optional<XBCNode> getParent() {
        Optional<XBCItem> item = super.getParentItem();
        return item.isPresent() ? Optional.of(new XBRNode(((XBRItem) item.get()).client, item.get().getId())) : Optional.empty();
    }
}
