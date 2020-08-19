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
package org.exbin.xbup.catalog.modifiable;

import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCNodeTree;
import org.exbin.xbup.core.catalog.base.XBCRoot;

/**
 * Interface for catalog node tree entity.
 *
 * @version 0.2.1 2020/08/14
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBMNodeTree extends XBCNodeTree {

    /**
     * Sets index.
     *
     * @param id the index
     */
    void setId(long id);

    /**
     * Sets node.
     *
     * @param owner the node
     */
    void setOwner(XBCNode owner);

    /**
     * Sets node child.
     *
     * @param node the node
     */
    void setNode(XBCNode node);

    /**
     * Sets depth level.
     *
     * @param depthLevel depth level
     */
    void setDepthLevel(int depthLevel);

    /**
     * Sets root record for this node.
     *
     * @param root root record
     */
    void setRoot(XBCRoot root);
}