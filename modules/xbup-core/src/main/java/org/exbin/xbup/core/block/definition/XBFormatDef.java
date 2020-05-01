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
package org.exbin.xbup.core.block.definition;

import java.util.List;
import javax.annotation.Nullable;
import org.exbin.xbup.core.serial.XBSerializable;

/**
 * XBUP level 1 format definition interface.
 *
 * @version 0.2.1 2017/05/15
 * @author ExBin Project (http://exbin.org)
 */
public interface XBFormatDef extends XBSerializable {

    /**
     * Returns count of parameters.
     *
     * @return count of parameters
     */
    long getParamsCount();

    /**
     * Gets list of format parameters.
     *
     * @return list of format parameters
     */
    @Nullable
    List<XBFormatParam> getFormatParams();

    /**
     * Returns revision definition.
     *
     * @return revision definition
     */
    @Nullable
    XBRevisionDef getRevisionDef();

    /**
     * Returns format parameter of given index.
     *
     * @param paramIndex parameter index
     * @return format parameter
     */
    @Nullable
    XBFormatParam getFormatParam(int paramIndex);
}
