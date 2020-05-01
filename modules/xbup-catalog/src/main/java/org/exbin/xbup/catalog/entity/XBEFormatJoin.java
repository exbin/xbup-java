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
package org.exbin.xbup.catalog.entity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import org.exbin.xbup.core.catalog.base.XBCFormatJoin;
import org.exbin.xbup.core.catalog.base.XBCFormatRev;
import org.exbin.xbup.core.catalog.base.XBCFormatSpec;

/**
 * Format join database entity.
 *
 * @version 0.1.22 2012/12/31
 * @author ExBin Project (http://exbin.org)
 */
@Entity(name = "XBFormatJoin")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class XBEFormatJoin extends XBEJoinDef implements XBCFormatJoin {

    /**
     * Creates a new instance of XBEBindBlock
     */
    public XBEFormatJoin() {
    }

    @Override
    public XBCFormatSpec getSpec() {
        return (XBCFormatSpec) super.getSpec();
    }

    @Override
    public XBCFormatRev getTarget() {
        return (XBCFormatRev) super.getTarget();
    }

    public void setSpec(XBEFormatSpec spec) {
        super.setCatalogItem(spec);
    }

    public void setTarget(XBEFormatRev target) {
        super.setTarget(target);
    }
}
