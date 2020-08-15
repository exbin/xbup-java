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
import org.exbin.xbup.core.catalog.base.XBCRev;
import org.exbin.xbup.core.catalog.base.XBCSpec;

/**
 * Interface for catalog revision entity.
 *
 * @version 0.2.1 2020/08/13
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBMRev extends XBCRev, XBMItem {

    /**
     * Sets relevant specification.
     *
     * @param spec specification
     */
    void setParent(XBCSpec spec);

    /**
     * Sets basic indexing value.
     *
     * @param xbIndex the XB Index
     */
    @Override
    void setXBIndex(long xbIndex);

    /**
     * Sets maximum XBIndex of specification bind
     *
     * @param xbLimit the maximum XB Index
     */
    void setXBLimit(long xbLimit);
}
