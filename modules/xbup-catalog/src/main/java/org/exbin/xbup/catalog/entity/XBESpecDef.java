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

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import org.exbin.xbup.core.block.definition.XBParamType;
import org.exbin.xbup.core.catalog.base.XBCRev;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.XBCSpecDef;

/**
 * Specification definition database entity.
 *
 * @version 0.1.22 2013/01/11
 * @author ExBin Project (http://exbin.org)
 */
@Entity(name = "XBSpecDef")
@Inheritance(strategy = InheritanceType.JOINED)
public class XBESpecDef extends XBEItem implements Serializable, XBCSpecDef {

    @ManyToOne
    private XBERev target;

    public XBESpecDef() {
    }

    @Override
    public XBCRev getTarget() {
        return target;
    }

    public void setTarget(XBERev target) {
        this.target = target;
    }

    @Override
    public XBCSpec getSpec() {
        return (XBCSpec) getParent();
    }

    public void setCatalogItem(XBESpec spec) {
        setParent(spec);
    }

    @Override
    public XBParamType getType() {
        return null;
    }
}
