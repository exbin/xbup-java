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
package org.exbin.xbup.service.skeleton;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.exbin.xbup.client.stub.XBPBaseProcedureType;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.catalog.base.XBCBase;
import org.exbin.xbup.core.catalog.base.service.XBCService;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.remote.XBMultiProcedure;
import org.exbin.xbup.core.remote.XBServiceServer;
import org.exbin.xbup.core.serial.param.XBPListenerSerialHandler;
import org.exbin.xbup.core.serial.param.XBPProviderSerialHandler;
import org.exbin.xbup.core.stream.XBInput;
import org.exbin.xbup.core.stream.XBOutput;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * RPC skeleton base class for catalog items.
 *
 * @version 0.2.1 2020/08/17
 * @author ExBin Project (http://exbin.org)
 * @param <T> entity class
 */
public class XBPBaseSkeleton<T extends XBCBase> {

    private final XBCService<T> service;
    private final XBPBaseProcedureType baseTypes;

    public XBPBaseSkeleton(XBCService<T> service, XBPBaseProcedureType baseTypes) {
        this.service = service;
        this.baseTypes = baseTypes;
    }

    public void registerProcedures(XBServiceServer remoteServer) {
        if (baseTypes.getItemByIdType() != null) {
            remoteServer.addXBProcedure(baseTypes.getItemByIdType(), new XBMultiProcedure() {
                @Override
                public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                    XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                    provider.begin();
                    provider.matchType(blockType);
                    provider.end();

                    T item = service.createItem();
                    XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                    listener.process(new UBNat32(item.getId()));
                }
            });
        }

        if (baseTypes.getCreateItemType() != null) {
            remoteServer.addXBProcedure(baseTypes.getCreateItemType(), new XBMultiProcedure() {
                @Override
                public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                    XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                    provider.begin();
                    provider.matchType(blockType);
                    provider.end();

                    T item = service.createItem();
                    XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                    listener.process(new UBNat32(item.getId()));
                }
            });
        }

        if (baseTypes.getRemoveItemType() != null) {
            remoteServer.addXBProcedure(baseTypes.getRemoveItemType(), new XBMultiProcedure() {
                @Override
                public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                    XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                    provider.begin();
                    provider.matchType(blockType);
                    XBAttribute index = provider.pullAttribute();
                    provider.end();

                    Optional<T> item = service.getItem(index.getNaturalLong());
                    if (item.isPresent()) {
                        service.removeItem(item.get());
                    } else {
                        throw new XBProcessingException("Unable to remove item", XBProcessingExceptionType.UNKNOWN);
                    }
                }
            });
        }

        if (baseTypes.getAllItemsType() != null) {
            remoteServer.addXBProcedure(baseTypes.getAllItemsType(), new XBMultiProcedure() {
                @Override
                public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                    XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                    provider.begin();
                    provider.matchType(blockType);
                    provider.end();

                    List<T> items = service.getAllItems();
                    XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                    listener.begin();
                    listener.matchType();
                    listener.putAttribute(items.size());
                    for (T item : items) {
                        listener.putAttribute(item.getId());
                    }
                    listener.end();
                }
            });
        }

        if (baseTypes.getItemsCountType() != null) {
            remoteServer.addXBProcedure(baseTypes.getItemsCountType(), new XBMultiProcedure() {
                @Override
                public void execute(XBBlockType blockType, XBOutput parameters, XBInput resultInput) throws XBProcessingException, IOException {
                    XBPProviderSerialHandler provider = new XBPProviderSerialHandler(parameters);
                    provider.begin();
                    provider.matchType(blockType);
                    provider.end();

                    XBPListenerSerialHandler listener = new XBPListenerSerialHandler(resultInput);
                    listener.process(new UBNat32(service.getItemsCount()));
                }
            });
        }
    }
}
