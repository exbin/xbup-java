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
package org.exbin.xbup.core.block.declaration;

import javax.annotation.Nullable;
import org.exbin.xbup.core.block.definition.XBBlockDef;
import org.exbin.xbup.core.serial.XBSerializable;

/**
 * Block declaration interface, either for catalog or local definition.
 *
 * @version 0.2.1 2017/05/10
 * @author ExBin Project (http://exbin.org)
 */
public interface XBBlockDecl extends XBSerializable {

    /**
     * Returns linked block definition.
     *
     * @return block definition
     */
    @Nullable
    XBBlockDef getBlockDef();

    /**
     * Returns linked revision.
     *
     * @return revision
     */
    long getRevision();
}
