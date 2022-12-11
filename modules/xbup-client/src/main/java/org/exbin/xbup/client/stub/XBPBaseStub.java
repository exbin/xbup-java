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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.core.catalog.base.XBCBase;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.remote.XBCallHandler;
import org.exbin.xbup.core.serial.param.XBPListenerSerialHandler;
import org.exbin.xbup.core.serial.param.XBPProviderSerialHandler;

/**
 * RPC stub base class for catalog items.
 *
 * @author ExBin Project (https://exbin.org)
 * @param <T> entity class
 */
@ParametersAreNonnullByDefault
public class XBPBaseStub<T extends XBCBase> implements XBPManagerStub<T> {

    private final XBCatalogServiceClient client;
    private final XBPConstructorMethod<T> constructorMethod;
    private final XBPBaseProcedureType baseTypes;

    public XBPBaseStub(XBCatalogServiceClient client, XBPConstructorMethod<T> service, XBPBaseProcedureType baseTypes) {
        this.client = client;
        this.constructorMethod = service;
        this.baseTypes = baseTypes;
    }

    @Nonnull
    @Override
    public T constructItem(long itemId) {
        return constructorMethod.itemConstructor(client, itemId);
    }

    @Nonnull
    @Override
    public T createItem() {
        final Long itemId = XBPStubUtils.voidToLongMethod(client.procedureCall(), baseTypes.getCreateItemType());
        return constructorMethod.itemConstructor(client, itemId);
    }

    @Override
    public void persistItem(XBCBase item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeItem(XBCBase item) {
        XBPStubUtils.longToVoidMethod(client.procedureCall(), baseTypes.getRemoveItemType(), item.getId());
    }

    @Nonnull
    @Override
    public Optional<T> getItem(long itemId) {
        final Long foundItemId = XBPStubUtils.longToLongMethod(client.procedureCall(), baseTypes.getItemByIdType(), itemId);
        return foundItemId == null ? Optional.empty() : Optional.of(constructorMethod.itemConstructor(client, foundItemId));
    }

    @Nonnull
    @Override
    public List<T> getAllItems() {
        List<T> result = new ArrayList<>();
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(baseTypes.getItemByIdType());
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            if (!serialOutput.pullIfEmptyBlock()) {
                serialOutput.begin();
                serialOutput.matchType();
                long count = serialOutput.pullLongAttribute();
                for (int i = 0; i < count; i++) {
                    result.add(constructItem(serialOutput.pullLongAttribute()));
                }
                serialOutput.end();
                procedureCall.execute();
                return result;
            }

            procedureCall.execute();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public long getItemsCount() {
        return XBPStubUtils.voidToLongMethod(client.procedureCall(), baseTypes.getItemsCountType());
    }
}
