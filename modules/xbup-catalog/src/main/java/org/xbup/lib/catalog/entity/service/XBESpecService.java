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
package org.xbup.lib.catalog.entity.service;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCFormatSpec;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.XBCSpecDef;
import org.xbup.lib.core.catalog.base.XBCSpecDefType;
import org.xbup.lib.core.catalog.base.manager.XBCSpecManager;
import org.xbup.lib.core.catalog.base.service.XBCSpecService;
import org.xbup.lib.catalog.XBECatalog;
import org.xbup.lib.catalog.entity.XBEBlockSpec;
import org.xbup.lib.catalog.entity.XBEFormatSpec;
import org.xbup.lib.catalog.entity.XBEGroupSpec;
import org.xbup.lib.catalog.entity.XBESpec;
import org.xbup.lib.catalog.entity.XBESpecDef;
import org.xbup.lib.catalog.entity.manager.XBESpecManager;

/**
 * Interface for XBESpec items service.
 *
 * @version 0.1.22 2013/01/11
 * @author XBUP Project (http://xbup.org)
 */
@Service
public class XBESpecService extends XBEDefaultService<XBESpec> implements XBCSpecService<XBESpec>, Serializable {

    @Autowired
    private XBESpecManager manager;

    public XBESpecService() {
        super();
    }

    public XBESpecService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBESpecManager(catalog);
        catalog.addCatalogManager(XBCSpecManager.class, itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }

    @Override
    public XBEBlockSpec findBlockSpecByXB(XBCNode node, long xbIndex) {
        return ((XBESpecManager)itemManager).findBlockSpecByXB(node, xbIndex);
    }

    @Override
    public XBEFormatSpec findFormatSpecByXB(XBCNode node, long xbIndex) {
        return ((XBESpecManager)itemManager).findFormatSpecByXB(node, xbIndex);
    }

    @Override
    public XBEGroupSpec findGroupSpecByXB(XBCNode node, long xbIndex) {
        return ((XBESpecManager)itemManager).findGroupSpecByXB(node, xbIndex);
    }

    @Override
    public Long findMaxBlockSpecXB(XBCNode node) {
        return ((XBESpecManager)itemManager).findMaxBlockSpecXB(node);
    }

    @Override
    public Long findMaxFormatSpecXB(XBCNode node) {
        return ((XBESpecManager)itemManager).findMaxFormatSpecXB(node);
    }

    @Override
    public Long findMaxGroupSpecXB(XBCNode node) {
        return ((XBESpecManager)itemManager).findMaxGroupSpecXB(node);
    }

    @Override
    public Long getAllBlockSpecsCount() {
        return ((XBESpecManager)itemManager).getAllBlockSpecsCount();
    }

    @Override
    public Long getAllFormatSpecsCount() {
        return ((XBESpecManager)itemManager).getAllFormatSpecsCount();
    }

    @Override
    public Long getAllGroupSpecsCount() {
        return ((XBESpecManager)itemManager).getAllGroupSpecsCount();
    }

    @Override
    public Long getAllSpecsCount() {
        return ((XBESpecManager)itemManager).getAllSpecsCount();
    }

    @Override
    public XBEBlockSpec getBlockSpec(XBCNode node, long index) {
        return ((XBESpecManager)itemManager).getBlockSpec(node, index);
    }

    @Override
    public List<XBCBlockSpec> getBlockSpecs(XBCNode node) {
        return ((XBESpecManager)itemManager).getBlockSpecs(node);
    }

    @Override
    public long getBlockSpecsCount(XBCNode node) {
        return ((XBESpecManager)itemManager).getBlockSpecsCount(node);
    }

    @Override
    public XBEFormatSpec getFormatSpec(XBCNode node, long index) {
        return ((XBESpecManager)itemManager).getFormatSpec(node, index);
    }

    @Override
    public List<XBCFormatSpec> getFormatSpecs(XBCNode node) {
        return ((XBESpecManager)itemManager).getFormatSpecs(node);
    }

    @Override
    public long getFormatSpecsCount(XBCNode node) {
        return ((XBESpecManager)itemManager).getFormatSpecsCount(node);
    }

    @Override
    public XBEGroupSpec getGroupSpec(XBCNode node, long index) {
        return ((XBESpecManager)itemManager).getGroupSpec(node, index);
    }

    @Override
    public List<XBCGroupSpec> getGroupSpecs(XBCNode node) {
        return ((XBESpecManager)itemManager).getGroupSpecs(node);
    }

    @Override
    public long getGroupSpecsCount(XBCNode node) {
        return ((XBESpecManager)itemManager).getGroupSpecsCount(node);
    }

    @Override
    public XBESpec getSpecByOrder(XBCNode node, long index) {
        return ((XBESpecManager)itemManager).getSpec(node, index);
    }

    @Override
    public Long[] getSpecXBPath(XBCSpec node) {
        return ((XBESpecManager)itemManager).getSpecXBPath(node);
    }

    @Override
    public List<XBCSpec> getSpecs(XBCNode node) {
        return ((XBESpecManager)itemManager).getSpecs(node);
    }

    @Override
    public long getSpecsCount(XBCNode node) {
        return ((XBESpecManager)itemManager).getSpecsCount(node);
    }

    @Override
    public XBESpecDef findSpecDefByXB(XBCSpec spec, long xbIndex) {
        return ((XBESpecManager)itemManager).findSpecDefByXB(spec, xbIndex);
    }

    @Override
    public Long findMaxSpecDefXB(XBCSpec spec) {
        return ((XBESpecManager)itemManager).findMaxSpecDefXB(spec);
    }

    @Override
    public XBESpecDef getSpecDefByOrder(XBCSpec spec, long index) {
        return ((XBESpecManager)itemManager).getSpecDefByOrder(spec, index);
    }

    @Override
    public List<XBCSpecDef> getSpecDefs(XBCSpec spec) {
        return ((XBESpecManager)itemManager).getSpecDefs(spec);
    }

    @Override
    public long getSpecDefsCount(XBCSpec spec) {
        return ((XBESpecManager)itemManager).getSpecDefsCount(spec);
    }

    @Override
    public long getDefsCount() {
        return ((XBESpecManager)itemManager).getDefsCount();
    }

    @Override
    public XBESpecDef getSpecDef(long itemId) {
        return (XBESpecDef) ((XBESpecManager)itemManager).getSpecDef(itemId);
    }

    @Override
    public XBCSpecDef createSpecDef(XBCSpec spec, XBCSpecDefType type) {
        return ((XBESpecManager)itemManager).createSpecDef(spec, type);
    }

    @Override
    public XBEBlockSpec createBlockSpec() {
        return ((XBESpecManager)itemManager).createBlockSpec();
    }

    @Override
    public XBCGroupSpec createGroupSpec() {
        return ((XBESpecManager)itemManager).createGroupSpec();
    }

    @Override
    public XBCFormatSpec createFormatSpec() {
        return ((XBESpecManager)itemManager).createFormatSpec();
    }
}
