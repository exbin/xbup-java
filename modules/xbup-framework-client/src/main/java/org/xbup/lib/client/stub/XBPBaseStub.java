/*
 * Copyright (C) XBUP Project
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
package org.xbup.lib.client.stub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.client.XBCatalogServiceClient;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.catalog.base.XBCBase;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.remote.XBCallHandler;
import org.xbup.lib.core.serial.param.XBPListenerSerialHandler;
import org.xbup.lib.core.serial.param.XBPProviderSerialHandler;

/**
 * RPC stub base class for catalog items.
 *
 * @version 0.1.25 2015/03/20
 * @author XBUP Project (http://xbup.org)
 * @param <T> entity class
 */
public class XBPBaseStub<T extends XBCBase> implements XBPManagerStub<T> {

    private final XBCatalogServiceClient client;
    private final XBPConstructorMethod<T> constructorMethod;
    private final XBPBaseProcedureType baseTypes;

    public XBPBaseStub(XBCatalogServiceClient client, XBPConstructorMethod<T> service, XBPBaseProcedureType baseTypes) {
        this.client = client;
        this.constructorMethod = service;
        this.baseTypes = baseTypes;
    }

    @Override
    public T constructItem(long itemId) {
        return constructorMethod.itemConstructor(client, itemId);
    }

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

    @Override
    public T getItem(long itemId) {
        final Long foundItemId = XBPStubUtils.longToLongMethod(client.procedureCall(), baseTypes.getItemByIdType(), itemId);
        return foundItemId == null ? null : constructorMethod.itemConstructor(client, foundItemId);
    }

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
                serialOutput.matchType(new XBFixedBlockType());
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
