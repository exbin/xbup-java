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

/**
 * XBUP level 1 revision definition interface.
 *
 * @version 0.2.1 2017/05/15
 * @author ExBin Project (http://exbin.org)
 */
public interface XBRevisionDef {

    /**
     * Gets list of revision parameters.
     *
     * @return list of revision parameters
     */
    @Nullable
    List<XBRevisionParam> getRevParams();

    /**
     * Returns revision group limit for given revision.
     *
     * @param revision revision index
     * @return group count
     */
    int getRevisionLimit(long revision);
}
