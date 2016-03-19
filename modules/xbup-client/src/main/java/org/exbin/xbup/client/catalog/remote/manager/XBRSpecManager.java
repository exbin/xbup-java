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
package org.exbin.xbup.client.catalog.remote.manager;

import java.util.List;
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
 * @version 0.1.25 2015/02/21
 * @author ExBin Project (http://exbin.org)
 */
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
}
