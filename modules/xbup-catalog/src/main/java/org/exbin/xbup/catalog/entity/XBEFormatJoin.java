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
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import org.exbin.xbup.core.block.definition.XBParamType;
import org.exbin.xbup.core.catalog.base.XBCFormatJoin;
import org.exbin.xbup.core.catalog.base.XBCFormatRev;
import org.exbin.xbup.core.catalog.base.XBCFormatSpec;
import org.exbin.xbup.core.catalog.base.XBCRev;
import org.exbin.xbup.core.catalog.base.XBCSpec;

/**
 * Format join database entity.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
@Entity(name = "XBFormatJoin")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class XBEFormatJoin extends XBEJoinDef implements XBCFormatJoin {

    public XBEFormatJoin() {
    }

    @Nonnull
    @Override
    public XBCFormatSpec getSpec() {
        return (XBCFormatSpec) super.getSpec();
    }

    @Override
    public void setSpec(XBCSpec spec) {
        if (!(spec instanceof XBEFormatSpec)) {
            throw new IllegalArgumentException();
        }
        super.setSpec(spec);
    }

    @Nonnull
    @Override
    public XBCFormatRev getTarget() {
        Optional<XBCRev> targetRev = super.getTargetRev();
        if (!targetRev.isPresent()) {
            throw new IllegalStateException();
        }
        return (XBCFormatRev) targetRev.get();
    }

    public void setTarget(XBCFormatRev target) {
        super.setTargetRev(target);
    }

    @Nonnull
    @Override
    public XBParamType getType() {
        return XBParamType.JOIN;
    }
}
