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
package org.exbin.xbup.core.parser.basic;

import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.stream.XBOutput;

/**
 * XBUP protocol level 0 data matching provider.
 *
 * Execution is receiver side controlled (pull).
 *
 * @version 0.2.1 2017/05/12
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBMatchingProvider extends XBOutput {

    /**
     * Matches block begin.
     *
     * @return termination mode
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    @Nonnull
    XBBlockTerminationMode matchBeginXB() throws XBProcessingException, IOException;

    /**
     * Matches block begin with exact termination mode.
     *
     * @param terminationMode termination mode
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    void matchBeginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException;

    /**
     * Matches block attribute.
     *
     * @return attribute value
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    @Nonnull
    XBAttribute matchAttribXB() throws XBProcessingException, IOException;

    /**
     * Matches block attribute with exact value.
     *
     * @param value attribute value
     * @throws XBProcessingException if processing error if unable to parse
     * attribute value
     * @throws IOException if input/output error
     */
    void matchAttribXB(XBAttribute value) throws XBProcessingException, IOException;

    /**
     * Matches block data.
     *
     * You have to process data before processing next event.
     *
     * @return data
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    @Nonnull
    InputStream matchDataXB() throws XBProcessingException, IOException;

    /**
     * Matches block data comparing it to specified data.
     *
     * @param data data to compare with
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    void matchDataXB(InputStream data) throws XBProcessingException, IOException;

    /**
     * Matches block end.
     *
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    void matchEndXB() throws XBProcessingException, IOException;
}
