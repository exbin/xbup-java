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
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.XBCLimitSpec;
import org.exbin.xbup.core.catalog.base.XBCTran;

/**
 * Interface for catalog item transformation entity.
 *
 * @version 0.2.1 2020/08/14
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBMTran extends XBCTran, XBMBase {

    /**
     * Sets owner which is directory.
     *
     * @param owner block specification
     */
    void setOwner(XBCBlockSpec owner);

    /**
     * Sets target of the procedure.
     *
     * @param target block revision
     */
    void setTarget(XBCBlockRev target);

    /**
     * Sets limitation of the procedure.
     *
     * @param limit specification's limitation
     */
    void setLimit(XBCLimitSpec limit);

    /**
     * Sets exception of the procedure.
     *
     * @param exception block revision
     */
    void setExcept(XBCBlockRev exception);
}
