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
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCGroupCons;
import org.exbin.xbup.core.catalog.base.XBCGroupSpec;

/**
 * Group consist database entity.
 *
 * @version 0.1.22 2012/12/31
 * @author ExBin Project (http://exbin.org)
 */
@Entity(name = "XBGroupCons")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class XBEGroupCons extends XBEConsDef implements XBCGroupCons {

    public XBEGroupCons() {
    }

    @Override
    public XBCGroupSpec getSpec() {
        return (XBCGroupSpec) super.getSpec();
    }

    @Override
    public XBCBlockRev getTarget() {
        return (XBCBlockRev) super.getTarget();
    }

    public void setSpec(XBEGroupSpec spec) {
        super.setCatalogItem(spec);
    }

    public void setTarget(XBEBlockRev target) {
        super.setTarget(target);
    }
}
