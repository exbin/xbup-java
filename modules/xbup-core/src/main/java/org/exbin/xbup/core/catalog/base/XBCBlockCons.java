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
 * Interface for type block consist entity.
 *
 * @version 0.1.21 2012/03/26
 * @author ExBin Project (http://exbin.org)
 */
public interface XBCBlockCons extends XBCConsDef {

    /**
     * Gets specification which is also owner.
     *
     * @return block specification
     */
    @Override
    XBCBlockSpec getSpec();

    /**
     * Gets target specification.
     *
     * @return block revision
     */
    @Override
    XBCBlockRev getTarget();
}
