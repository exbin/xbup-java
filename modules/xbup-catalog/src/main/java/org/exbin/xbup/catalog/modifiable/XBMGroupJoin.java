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
package org.exbin.xbup.catalog.modifiable;

import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.catalog.base.XBCGroupJoin;
import org.exbin.xbup.core.catalog.base.XBCGroupRev;
import org.exbin.xbup.core.catalog.base.XBCSpec;

/**
 * Interface for group join entity.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBMGroupJoin extends XBCGroupJoin, XBMJoinDef {

    /**
     * Sets specification which is also owner.
     *
     * @param spec XBCGroupSpec group specification
     */
    @Override
    void setSpec(XBCSpec spec);

    /**
     * Sets target specification.
     *
     * @param groupRev group revision, never empty
     */
    void setTarget(XBCGroupRev groupRev);
}
