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
package org.exbin.xbup.client.stub;

import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.client.catalog.remote.XBRItem;
import org.exbin.xbup.client.catalog.remote.XBRXFile;
import org.exbin.xbup.client.catalog.remote.XBRXIcon;
import org.exbin.xbup.client.catalog.remote.XBRXIconMode;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.core.catalog.base.XBCXIcon;
import org.exbin.xbup.core.catalog.base.XBCXIconMode;

/**
 * RPC stub class for XBRXIcon catalog items.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBPXIconStub extends XBPBaseStub<XBCXIcon> {

    public static long[] OWNER_ICON_PROCEDURE = {0, 2, 13, 0, 0};
    public static long[] MODE_ICON_PROCEDURE = {0, 2, 13, 1, 0};
    public static long[] XBINDEX_ICON_PROCEDURE = {0, 2, 13, 2, 0};
    public static long[] DEFAULTITEM_ICON_PROCEDURE = {0, 2, 13, 3, 0};
    public static long[] FILE_ICON_PROCEDURE = {0, 2, 13, 4, 0};
    public static long[] DEFAULTITEMBIG_ICON_PROCEDURE = {0, 2, 13, 5, 0};
    public static long[] DEFAULTITEMSMALL_ICON_PROCEDURE = {0, 2, 13, 6, 0};

    private final XBCatalogServiceClient client;

    public XBPXIconStub(XBCatalogServiceClient client) {
        super(client, new XBPConstructorMethod<XBCXIcon>() {
            @Override
            public XBRXIcon itemConstructor(XBCatalogServiceClient client, long itemId) {
                return new XBRXIcon(client, itemId);
            }
        }, null);
        this.client = client;
    }

    public XBRItem getParent(long iconId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(OWNER_ICON_PROCEDURE), iconId);
        return index == null ? null : new XBRItem(client, index);
    }

    public XBCXIconMode getMode(long iconId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(MODE_ICON_PROCEDURE), iconId);
        return index == null ? null : new XBRXIconMode(client, index);
    }

    public XBCXFile getIconFile(long iconId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(FILE_ICON_PROCEDURE), iconId);
        return index == null ? null : new XBRXFile(client, index);
    }

    public XBRXIcon getDefaultIcon(XBCItem item) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(DEFAULTITEM_ICON_PROCEDURE), item.getId());
        return index == null ? null : new XBRXIcon(client, index);
    }

    public XBRXIcon getDefaultBigIcon(XBCItem item) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(DEFAULTITEMBIG_ICON_PROCEDURE), item.getId());
        return index == null ? null : new XBRXIcon(client, index);
    }

    public XBRXIcon getDefaultSmallIcon(XBCItem item) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(DEFAULTITEMSMALL_ICON_PROCEDURE), item.getId());
        return index == null ? null : new XBRXIcon(client, index);
    }
}
