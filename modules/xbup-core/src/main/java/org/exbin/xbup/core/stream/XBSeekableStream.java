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
package org.exbin.xbup.core.stream;

import java.io.IOException;

/**
 * Interface for stream with seekable tokens.
 *
 * @version 0.2.1 2017/05/17
 * @author ExBin Project (http://exbin.org)
 */
public interface XBSeekableStream {

    /**
     * Moves position in stream to given position from the start of the stream.
     *
     * @param position target position
     * @throws IOException if input/output error
     */
    void seekXB(long position) throws IOException;

    /**
     * Returns current position in stream.
     *
     * @return current position in stream, -1 if unable to determine
     */
    long getPositionXB();

    /**
     * Returns length of stream.
     *
     * @return length of stream in bytes, -1 if unable to determine
     */
    long getStreamSizeXB();
}
