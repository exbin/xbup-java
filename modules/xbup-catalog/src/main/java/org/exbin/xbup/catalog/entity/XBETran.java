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
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.exbin.xbup.catalog.modifiable.XBMTran;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.XBCLimitSpec;

/**
 * Transformations database entity.
 *
 * @version 0.2.1 2020/08/14
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Entity(name = "XBTran")
public class XBETran implements XBMTran, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private XBEBlockSpec owner;
    @ManyToOne
    private XBEBlockRev target;
    @ManyToOne
    private XBELimitSpec limit;
    @ManyToOne
    private XBEBlockRev except;

    public XBETran() {
    }

    @Override
    public long getId() {
        if (id == null) {
            return 0;
        }
        return id;
    }

    @Nonnull
    @Override
    public XBEBlockSpec getOwner() {
        return owner;
    }

    @Override
    public void setOwner(XBCBlockSpec owner) {
        this.owner = (XBEBlockSpec) owner;
    }

    @Nonnull
    @Override
    public XBEBlockRev getTarget() {
        return target;
    }

    @Override
    public void setTarget(XBCBlockRev target) {
        this.target = (XBEBlockRev) target;
    }

    @Nonnull
    @Override
    public XBELimitSpec getLimit() {
        return limit;
    }

    @Override
    public void setLimit(XBCLimitSpec limit) {
        this.limit = (XBELimitSpec) limit;
    }

    @Nonnull
    @Override
    public XBEBlockRev getExcept() {
        return except;
    }

    @Override
    public void setExcept(XBCBlockRev except) {
        this.except = (XBEBlockRev) except;
    }
}
