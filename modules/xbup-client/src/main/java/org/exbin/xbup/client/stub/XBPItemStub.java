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

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.client.catalog.remote.XBRItem;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.remote.XBCallHandler;
import org.exbin.xbup.core.serial.param.XBPListenerSerialHandler;
import org.exbin.xbup.core.serial.param.XBPProviderSerialHandler;
import org.exbin.xbup.core.type.XBString;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * RPC stub class for XBRItem catalog items.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBPItemStub extends XBPBaseStub<XBCItem> {

    public static long[] OWNER_ITEM_PROCEDURE = {0, 2, 3, 0, 0};
    public static long[] XBINDEX_ITEM_PROCEDURE = {0, 2, 3, 1, 0};
    public static long[] ITEMSCOUNT_ITEM_PROCEDURE = {0, 2, 3, 2, 0};
    public static long[] FIND_ALL_PAGED_ITEM_PROCEDURE = {0, 2, 3, 3, 0};

    private final XBCatalogServiceClient client;

    public XBPItemStub(XBCatalogServiceClient client) {
        super(client, XBRItem::new, new XBPBaseProcedureType(null, null, null, null, new XBDeclBlockType(ITEMSCOUNT_ITEM_PROCEDURE)));
        this.client = client;
    }

    @Nonnull
    public Optional<XBCItem> getParent(Long itemId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(OWNER_ITEM_PROCEDURE), itemId);
        return index == null ? Optional.empty() : Optional.of(constructItem(index));
    }

    public Long getXBIndex(Long itemId) {
        return XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(XBINDEX_ITEM_PROCEDURE), itemId);
    }

    @Nullable
    public Integer findAllPagedCount(String filterCondition, String specType) {
        XBCallHandler procedureCall = client.procedureCall();
        try {
            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(FIND_ALL_PAGED_ITEM_PROCEDURE));
            serialInput.putAppend(new XBString(filterCondition));
            serialInput.putAppend(new XBString(specType));
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            if (!serialOutput.pullIfEmptyBlock()) {
                UBNat32 result = new UBNat32();
                serialOutput.process(result);
                return result.getInt();
            }
            procedureCall.execute();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
