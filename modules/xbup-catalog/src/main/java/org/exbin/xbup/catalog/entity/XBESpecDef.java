/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.catalog.entity;

import java.io.Serializable;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import org.exbin.xbup.catalog.modifiable.XBMSpecDef;
import org.exbin.xbup.core.block.definition.XBParamType;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCRev;
import org.exbin.xbup.core.catalog.base.XBCSpec;

/**
 * Specification definition database entity.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
@Entity(name = "XBSpecDef")
@Inheritance(strategy = InheritanceType.JOINED)
public class XBESpecDef extends XBEItem implements Serializable, XBMSpecDef {

    @ManyToOne
    private XBERev target;

    public XBESpecDef() {
    }

    @Nonnull
    @Override
    public Optional<XBCRev> getTargetRev() {
        return Optional.ofNullable(target);
    }

    @Override
    public void setTargetRev(@Nullable XBCRev target) {
        this.target = (XBERev) target;
    }

    @Nonnull
    @Override
    public XBCSpec getSpec() {
        Optional<XBCItem> parentItem = getParentItem();
        if (!parentItem.isPresent()) {
            throw new IllegalStateException();
        }

        return (XBCSpec) parentItem.get();
    }

    @Override
    public void setSpec(XBCSpec spec) {
        super.setParentItem(spec);
    }

    @Nonnull
    @Override
    public XBParamType getType() {
        throw new IllegalStateException();
    }

    @Override
    public void setType(XBParamType type) {
        throw new IllegalStateException();
    }
}
