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
package org.exbin.xbup.parser_command;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.block.XBTDocument;

/**
 * XBUP level 1 command reader interface.
 *
 * @version 0.2.1 2017/05/24
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBTCommandReader extends XBTDocument, Closeable {

    /**
     * Opens input byte-stream.
     *
     * @param stream input stream
     * @throws java.io.IOException exception on input/output error
     */
    void open(InputStream stream) throws IOException;

    /**
     * Closes input stream.
     *
     * @throws IOException exception on input/output error
     */
    @Override
    void close() throws IOException;

    /**
     * Resets parser.
     *
     * @throws IOException exception on input/output error
     */
    void resetParser() throws IOException;

    /**
     * Returns block handler for given path in document.
     *
     * @param blockPath path to block in document
     * @return block handler
     */
    @Nonnull
    Optional<XBTBlock> getBlock(long[] blockPath);
}
