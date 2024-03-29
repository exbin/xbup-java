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
package org.exbin.xbup.core.block;

import java.io.InputStream;
import java.util.Optional;
import javax.annotation.Nonnull;

/**
 * Interface for read access to XBUP level 1 document.
 *
 * @author ExBin Project (https://exbin.org)
 */
public interface XBTDocument {

    /**
     * Returns root block of the document.
     *
     * @return root block if exits
     */
    @Nonnull
    Optional<XBTBlock> getRootBlock();

    /**
     * Returns size of the whole document if available.
     *
     * @return length of whole document in bytes or -1 if not available or is
     * infinite
     */
    long getDocumentSize();

    /**
     * Returns tail data input stream.
     *
     * @return data stream
     */
    @Nonnull
    Optional<InputStream> getTailData();

    /**
     * Returns size of the tail data if available.
     *
     * @return length of tail data in bytes or -1 if not available or is
     * infinite
     */
    long getTailDataSize();
}
