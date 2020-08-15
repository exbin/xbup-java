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
package org.exbin.xbup.core.parser.basic.convert;

import java.io.IOException;
import java.io.InputStream;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.parser.XBParsingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.XBListener;
import org.exbin.xbup.core.parser.token.XBAttribute;

/**
 * Level 0 counting listener reporting one skipped block.
 *
 * @version 0.1.25 2015/02/06
 * @author ExBin Project (http://exbin.org)
 */
public class XBSkipBlockListener implements XBListener {

    private boolean isSkipped = false;
    private long level = 0;

    public XBSkipBlockListener() {
        isSkipped = true;
    }

    @Override
    public void beginXB(XBBlockTerminationMode terminationMode) throws XBParsingException, IOException {
        if (isSkipped) {
            throw new XBParsingException("Unexpected token after block skip", XBProcessingExceptionType.WRITING_AFTER_END);
        }

        level++;
    }

    @Override
    public void attribXB(XBAttribute value) throws XBParsingException, IOException {
        if (isSkipped) {
            throw new XBParsingException("Unexpected token after block skip", XBProcessingExceptionType.WRITING_AFTER_END);
        }
    }

    @Override
    public void dataXB(InputStream data) throws XBParsingException, IOException {
        if (isSkipped) {
            throw new XBParsingException("Unexpected token after block skip", XBProcessingExceptionType.WRITING_AFTER_END);
        }
    }

    @Override
    public void endXB() throws XBParsingException, IOException {
        if (isSkipped) {
            throw new XBParsingException("Unexpected token after block skip", XBProcessingExceptionType.WRITING_AFTER_END);
        }

        if (level == 0) {
            isSkipped = true;
        } else {
            level--;
        }
    }

    public boolean isSkipped() {
        return isSkipped;
    }
}
