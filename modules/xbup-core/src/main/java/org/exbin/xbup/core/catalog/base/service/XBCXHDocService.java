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
package org.exbin.xbup.core.catalog.base.service;

import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXHDoc;

/**
 * Interface for XBCXHDoc items service.
 *
 * @author ExBin Project (https://exbin.org)
 */
public interface XBCXHDocService extends XBCService<XBCXHDoc>, XBCExtension {

    /**
     * Gets documentation in default language for given item.
     *
     * @param item catalog item
     * @return hypertext documentation structure
     */
    XBCXHDoc getDocumentation(XBCItem item);

    /**
     * Gets text for documentation in default language for given item.
     *
     * @param item catalog item
     * @return hypertext documentation
     */
    String getDocumentationText(XBCItem item);

    /**
     * Gets text body part of documentation in default language for given item.
     *
     * @param item catalog item
     * @return hypertext documentation
     */
    String getDocumentationBodyText(XBCItem item);
}
