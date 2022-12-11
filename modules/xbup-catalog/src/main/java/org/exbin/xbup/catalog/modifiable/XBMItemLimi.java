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
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.XBCItemLimi;
import org.exbin.xbup.core.catalog.base.XBCLimitSpec;

/**
 * Interface for item limitation entity.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBMItemLimi extends XBCItemLimi, XBMBase {

    /**
     * Sets block specification which is owner.
     *
     * @param owner block specification
     */
    void setOwner(XBCBlockSpec owner);

    /**
     * Sets owner which is directory.
     *
     * @param target limit specification
     */
    void setTarget(XBCLimitSpec target);

    /**
     * Sets basic indexing value.
     *
     * @param xbIndex XB Index
     */
    void setXBIndex(long xbIndex);
}
