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
package org.exbin.xbup.core.block.definition;

import java.util.List;
import javax.annotation.Nullable;
import org.exbin.xbup.core.serial.XBSerializable;

/**
 * XBUP level 1 block definition interface.
 *
 * @author ExBin Project (https://exbin.org)
 */
public interface XBBlockDef extends XBSerializable {

    /**
     * Gets count of parameter's declarations.
     *
     * @return count of declarations
     */
    long getParamsCount();

    /**
     * Returns block parameter of given index.
     *
     * @param paramIndex parameter index
     * @return block parameter
     */
    @Nullable
    XBBlockParam getBlockParam(int paramIndex);

    /**
     * Gets list of parameters in order of appearance.
     *
     * @return list of parameters
     */
    @Nullable
    List<XBBlockParam> getBlockParams();

    /**
     * Gets revision definition.
     *
     * @return revision definition
     */
    @Nullable
    XBRevisionDef getRevisionDef();
}
