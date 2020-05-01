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
package org.exbin.xbup.core.catalog.base;

/**
 * Interface for catalog item transformation entity.
 *
 * @version 0.1.21 2011/12/29
 * @author ExBin Project (http://exbin.org)
 */
public interface XBCTran extends XBCBase {

    /**
     * Gets owner which is directory.
     *
     * @return block specification
     */
    XBCBlockSpec getOwner();

    /**
     * Gets target of the procedure.
     *
     * @return block revision
     */
    XBCBlockRev getTarget();

    /**
     * Gets limitation of the procedure.
     *
     * @return specification's limitation
     */
    XBCLimitSpec getLimit();

    /**
     * Gets exception of the procedure.
     *
     * @return block revision
     */
    XBCBlockRev getExcept();
}
