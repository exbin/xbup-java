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

import java.io.InputStream;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.parser.basic.XBTListener;
import org.exbin.xbup.core.parser.token.XBAttribute;

/**
 * TODO: Filter primary document content - process/skip all basic blocks and
 * extensions.
 *
 * @version 0.1.23 2013/11/06
 * @author ExBin Project (http://exbin.org)
 */
public class XBTRootDocumentFilter implements XBTListener {

    public XBTRootDocumentFilter() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void typeXBT(XBBlockType type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void attribXBT(XBAttribute value) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void endXBT() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void dataXBT(InputStream data) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
