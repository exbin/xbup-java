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
package org.exbin.xbup.client.stub;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.client.catalog.remote.XBRRoot;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.base.XBCRoot;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.remote.XBCallHandler;
import org.exbin.xbup.core.serial.param.XBPListenerSerialHandler;
import org.exbin.xbup.core.serial.param.XBPProviderSerialHandler;
import org.exbin.xbup.core.type.XBDateTime;

/**
 * RPC stub class for XBRRoot catalog items.
 *
 * @version 0.2.1 2020/08/18
 * @author ExBin Project (http://exbin.org)
 */
public class XBPRootStub extends XBPBaseStub<XBRRoot> {

    public static long[] ROOT_PROCEDURE = {0, 2, 4, 29, 0};
    public static long[] CATALOG_ROOT_PROCEDURE = {0, 2, 4, 30, 0};
    public static long[] LASTUPDATE_ROOT_PROCEDURE = {0, 2, 4, 31, 0};

    private final XBCatalogServiceClient client;

    public XBPRootStub(XBCatalogServiceClient client) {
        super(client, new XBPConstructorMethod<XBRRoot>() {
            @Override
            public XBRRoot itemConstructor(XBCatalogServiceClient client, long itemId) {
                return new XBRRoot(client, itemId);
            }
        }, new XBPBaseProcedureType(null, null, null, null, null));
        this.client = client;
    }

    @Nonnull
    public Optional<Date> getRootLastUpdate(long rootId) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(LASTUPDATE_ROOT_PROCEDURE));
            serialInput.putAttribute(rootId);
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            if (!serialOutput.pullIfEmptyBlock()) {
                XBDateTime dateTime = new XBDateTime();
                serialOutput.process(dateTime);
                return Optional.of(dateTime.getValue());
            }
            procedureCall.execute();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Optional.empty();
    }

    public XBCRoot getMainRoot() {
        Long index = XBPStubUtils.voidToLongMethod(client.procedureCall(), new XBDeclBlockType(CATALOG_ROOT_PROCEDURE));
        return index == null ? null : new XBRRoot(client, index);
    }

    public XBCRoot getRoot(long rootId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(ROOT_PROCEDURE), rootId);
        return index == null ? null : new XBRRoot(client, index);
    }

    @Nonnull
    public Optional<Date> getMainLastUpdate() {
        return getRootLastUpdate(getMainRoot().getId());
    }
}
