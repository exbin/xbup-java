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
package org.exbin.xbup.catalog.modifiable;

import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.catalog.base.XBCFormatCons;
import org.exbin.xbup.core.catalog.base.XBCGroupRev;
import org.exbin.xbup.core.catalog.base.XBCSpec;

/**
 * Interface for format consist entity.
 *
 * @version 0.2.1 2020/08/13
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBMFormatCons extends XBCFormatCons, XBMConsDef {

    /**
     * Sets specification which is also owner.
     *
     * @param spec XBCFormatSpec format specification
     */
    @Override
    void setSpec(XBCSpec spec);

    /**
     * sets target specification.
     *
     * @param groupRev group revision, never empty
     */
    void setTarget(XBCGroupRev groupRev);
}
