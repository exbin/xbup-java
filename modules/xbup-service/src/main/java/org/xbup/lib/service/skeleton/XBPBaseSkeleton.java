/*
 * Copyright (C) ExBin Project
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
package org.xbup.lib.service.skeleton;

import org.xbup.lib.client.stub.XBPBaseProcedureType;
import java.io.IOException;
import java.util.List;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBTEmptyBlock;
import org.xbup.lib.core.catalog.base.XBCBase;
import org.xbup.lib.core.catalog.base.service.XBCService;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.remote.XBMultiProcedure;
import org.xbup.lib.core.remote.XBServiceServer;
import org.xbup.lib.core.serial.param.XBPListenerSerialHandler;
import org.xbup.lib.core.serial.param.XBPProviderSerialHandler;
import org.xbup.lib.core.stream.XBInput;
import org.xbup.lib.core.stream.XBOutput;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC skeleton base class for catalog items.
 *
 * @version 0.1.25 2015/03/18
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
                    listener.process(item == null ? XBTEmptyBlock.getEmptyBlock() : new UBNat32(item.getId()));
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

                    T item = service.getItem(index.getNaturalLong());
                    if (item != null) {
                        service.removeItem(item);
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
