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
import org.exbin.xbup.core.catalog.base.XBCXIconMode;

/**
 * Interface for catalog icon mode entity.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBMXIconMode extends XBCXIconMode, XBMBase {

    /**
     * Sets type of icon.
     *
     * @param iconTypeId icon type
     */
    void setType(long iconTypeId);

    /**
     * Sets MIME string.
     *
     * @param mimeType MIME type
     */
    void setMIME(String mimeType);

    /**
     * Sets icon mode text caption.
     *
     * @param caption caption
     */
    void setCaption(String caption);
}
