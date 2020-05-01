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
package org.exbin.xbup.core.block;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Basic interface for editable XBUP level 1 document.
 *
 * @version 0.2.1 2017/05/09
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBTEditableDocument extends XBTDocument {

    /**
     * Sets root block of the document.
     *
     * @param block the block to use as root block for this document
     */
    void setRootBlock(@Nullable XBTBlock block);

    /**
     * Sets tail data.
     *
     * @param source data stream
     * @throws java.io.IOException if input/output error
     */
    void setTailData(@Nullable InputStream source) throws IOException;

    /**
     * Clears all data in this document.
     */
    void clear();

    /**
     * Finds node using depth-first traversal index.
     *
     * @param index order of the block in document
     * @return block if found else null
     */
    @Nonnull
    Optional<XBTBlock> findBlockByIndex(long index);

    /**
     * Creates new instance of block as next child for given block.
     *
     * @param parent block which would be set as parent block, null for root
     * node
     * @return newly created instance of this document
     */
    @Nonnull
    XBTBlock createNewBlock(@Nullable XBTBlock parent);
}
