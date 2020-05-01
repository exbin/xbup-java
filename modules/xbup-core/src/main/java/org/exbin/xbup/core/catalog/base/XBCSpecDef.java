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

import org.exbin.xbup.core.block.definition.XBParamType;

/**
 * Interface for catalog specification definition entity.
 *
 * @version 0.1.22 2013/01/11
 * @author ExBin Project (http://exbin.org)
 */
public interface XBCSpecDef extends XBCItem {

    /**
     * Gets specification which is also owner.
     *
     * @return specification
     */
    XBCSpec getSpec();

    /**
     * Gets target specification.
     *
     * @return revision
     */
    XBCRev getTarget();

    /**
     * Gets specification definition's type.
     *
     * @return specification definition's type
     */
    XBParamType getType();
}
