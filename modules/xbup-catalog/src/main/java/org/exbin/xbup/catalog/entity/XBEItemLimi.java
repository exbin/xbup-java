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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.XBCItemLimi;
import org.exbin.xbup.core.catalog.base.XBCLimitSpec;

/**
 * Item limitations database entity.
 *
 * @version 0.1.21 2011/08/21
 * @author ExBin Project (http://exbin.org)
 */
@Entity(name = "XBItemLimi")
public class XBEItemLimi implements XBCItemLimi, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private XBEBlockSpec owner;
    @ManyToOne
    private XBELimitSpec target;

    private Long xbIndex;

    public XBEItemLimi() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public XBCBlockSpec getOwner() {
        return (XBCBlockSpec) owner;
    }

    @Override
    public XBCLimitSpec getTarget() {
        return target;
    }

    @Override
    public Long getXBIndex() {
        return xbIndex;
    }

    public void setOwner(XBEBlockSpec owner) {
        this.owner = owner;
    }

    public void setTarget(XBELimitSpec target) {
        this.target = target;
    }

    public void setXbIndex(Long xbIndex) {
        this.xbIndex = xbIndex;
    }
}
