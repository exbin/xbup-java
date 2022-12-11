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

import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import org.exbin.xbup.catalog.modifiable.XBMBlockJoin;
import org.exbin.xbup.core.block.definition.XBParamType;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.XBCSpec;

/**
 * Block join database entity.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
@Entity(name = "XBBlockJoin")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class XBEBlockJoin extends XBEJoinDef implements XBMBlockJoin {

    public XBEBlockJoin() {
    }

    @Nonnull
    @Override
    public XBCBlockSpec getSpec() {
        return (XBCBlockSpec) super.getSpec();
    }

    @Override
    public void setSpec(XBCSpec spec) {
        if (!(spec instanceof XBEBlockSpec)) {
            throw new IllegalArgumentException();
        }
        super.setSpec(spec);
    }

    @Nonnull
    @Override
    public Optional<XBCBlockRev> getTarget() {
        return super.getTargetRev().map(t -> (XBCBlockRev) t);
    }

    @Override
    public void setTarget(@Nullable XBCBlockRev blockRev) {
        if (blockRev != null && !(blockRev instanceof XBEBlockRev)) {
            throw new IllegalArgumentException();
        }
        super.setTargetRev(blockRev);
    }

    @Nonnull
    @Override
    public XBParamType getType() {
        return XBParamType.JOIN;
    }
}
