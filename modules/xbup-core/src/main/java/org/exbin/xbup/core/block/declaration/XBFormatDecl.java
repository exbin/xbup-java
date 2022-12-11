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
package org.exbin.xbup.core.block.declaration;

import java.util.List;
import javax.annotation.Nullable;
import org.exbin.xbup.core.block.definition.XBFormatDef;
import org.exbin.xbup.core.serial.XBSerializable;

/**
 * Format declaration interface, either for catalog or local definition.
 *
 * @author ExBin Project (https://exbin.org)
 */
public interface XBFormatDecl extends XBSerializable {

    /**
     * Returns linked format definition.
     *
     * @return format definition
     */
    @Nullable
    XBFormatDef getFormatDef();

    /**
     * Returns list of group declarations.
     *
     * @return list of group declarations
     */
    @Nullable
    List<XBGroupDecl> getGroupDecls();

    /**
     * Returns linked revision.
     *
     * @return revision
     */
    long getRevision();
}
