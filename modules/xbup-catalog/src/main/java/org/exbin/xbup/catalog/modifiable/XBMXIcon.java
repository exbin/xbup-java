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

import javax.annotation.Nonnull;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.core.catalog.base.XBCXIcon;
import org.exbin.xbup.core.catalog.base.XBCXIconMode;

/**
 * Interface for catalog item icon entity.
 *
 * @author ExBin Project (https://exbin.org)
 */
public interface XBMXIcon extends XBCXIcon, XBMBase {

    /**
     * Sets parent item.
     *
     * @param parent parent
     */
    void setParent(XBCItem parent);

    /**
     * Sets icon mode.
     *
     * @param mode mode
     */
    @Nonnull
    void setMode(XBCXIconMode mode);

    /**
     * Sets icon file.
     *
     * @param file file
     */
    void setIconFile(XBCXFile file);
}
