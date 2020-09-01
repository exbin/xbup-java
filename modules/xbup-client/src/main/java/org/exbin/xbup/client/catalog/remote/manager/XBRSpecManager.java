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
package org.exbin.xbup.client.catalog.remote.manager;

import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRBlockSpec;
import org.exbin.xbup.client.catalog.remote.XBRFormatSpec;
import org.exbin.xbup.client.catalog.remote.XBRGroupSpec;
import org.exbin.xbup.client.catalog.remote.XBRSpec;
import org.exbin.xbup.client.catalog.remote.XBRSpecDef;
import org.exbin.xbup.client.stub.XBPSpecStub;
import org.exbin.xbup.core.block.definition.XBParamType;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.XBCFormatSpec;
import org.exbin.xbup.core.catalog.base.XBCGroupSpec;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.XBCSpecDef;
import org.exbin.xbup.core.catalog.base.manager.XBCSpecManager;

/**
 * Remote manager class for XBRSpec catalog items.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBRSpecManager extends XBRDefaultManager<XBRSpec> implements XBCSpecManager<XBRSpec> {

    private final XBPSpecStub specStub;

    public XBRSpecManager(XBRCatalog catalog) {
        super(catalog);
        specStub = new XBPSpecStub(client);
        setManagerStub(specStub);
    }

    @Override
    public Long getAllSpecsCount() {
        return specStub.getAllSpecsCount();
    }

    @Override
    public Long getAllFormatSpecsCount() {
        return specStub.getAllFormatSpecsCount();
    }

    @Override
    public Long getAllGroupSpecsCount() {
        return specStub.getAllGroupSpecsCount();
    }

    @Override
    public Long getAllBlockSpecsCount() {
        return specStub.getAllBlockSpecsCount();
    }

    @Override
    public Long[] getSpecXBPath(XBCSpec node) {
        return specStub.getSpecXBPath(node);
    }

    @Override
    public List<XBCSpec> getSpecs(XBCNode node) {
        return specStub.getSpecs(node);
    }

    @Override
    public XBRSpec getSpec(XBCNode node, long index) {
        return specStub.getSpec(node, index);
    }

    @Override
    public XBRFormatSpec getFormatSpec(XBCNode node, long index) {
        return specStub.getFormatSpec(node, index);
    }

    @Override
    public List<XBCFormatSpec> getFormatSpecs(XBCNode node) {
        return specStub.getFormatSpecs(node);
    }

    @Override
    public XBRBlockSpec getBlockSpec(XBCNode node, long index) {
        return specStub.getBlockSpec(node, index);
    }

    @Override
    public List<XBCBlockSpec> getBlockSpecs(XBCNode node) {
        return specStub.getBlockSpecs(node);
    }

    @Override
    public XBRGroupSpec getGroupSpec(XBCNode node, long index) {
        return specStub.getGroupSpec(node, index);
    }

    @Override
    public List<XBCGroupSpec> getGroupSpecs(XBCNode node) {
        return specStub.getGroupSpecs(node);
    }

    @Override
    public XBRBlockSpec findBlockSpecByXB(XBCNode node, long xbIndex) {
        return specStub.findBlockSpecByXB(node, xbIndex);
    }

    @Override
    public Long findMaxBlockSpecXB(XBCNode node) {
        return specStub.findMaxBlockSpecXB(node);
    }

    @Override
    public XBRGroupSpec findGroupSpecByXB(XBCNode node, long xbIndex) {
        return specStub.findGroupSpecByXB(node, xbIndex);
    }

    @Override
    public Long findMaxGroupSpecXB(XBCNode node) {
        return specStub.findMaxGroupSpecXB(node);
    }

    @Override
    public XBRFormatSpec findFormatSpecByXB(XBCNode node, long xbIndex) {
        return specStub.findFormatSpecByXB(node, xbIndex);
    }

    @Override
    public Long findMaxFormatSpecXB(XBCNode node) {
        return specStub.findMaxFormatSpecXB(node);
    }

    @Override
    public long getFormatSpecsCount(XBCNode node) {
        return specStub.getFormatSpecsCount(node);
    }

    @Override
    public long getGroupSpecsCount(XBCNode node) {
        return specStub.getGroupSpecsCount(node);
    }

    @Override
    public long getBlockSpecsCount(XBCNode node) {
        return specStub.getBlockSpecsCount(node);
    }

    @Override
    public long getSpecsCount(XBCNode node) {
        return specStub.getSpecsCount(node);
    }

    @Override
    public long getDefsCount() {
        return specStub.getDefsCount();
    }

    @Override
    public XBRSpecDef getSpecDefByOrder(XBCSpec spec, long index) {
        return specStub.getSpecDefByOrder(spec, index);
    }

    @Override
    public XBRSpecDef findSpecDefByXB(XBCSpec spec, long xbIndex) {
        return specStub.findSpecDefByXB(spec, xbIndex);
    }

    @Override
    public List<XBCSpecDef> getSpecDefs(XBCSpec spec) {
        return specStub.getSpecDefs(spec);
    }

    @Override
    public long getSpecDefsCount(XBCSpec spec) {
        return specStub.getSpecDefsCount(spec);
    }

    @Override
    public XBCSpecDef getSpecDef(long itemId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBCSpecDef createSpecDef(XBCSpec spec, XBParamType type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBCBlockSpec createBlockSpec() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBCGroupSpec createGroupSpec() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBCFormatSpec createFormatSpec() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persistSpecDef(XBCSpecDef specDef) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeSpecDef(XBCSpecDef specDef) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
