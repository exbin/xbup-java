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
package org.exbin.xbup.core.catalog.base;

/**
 * Interface for catalog node tree entity.
 *
 * @version 0.1.24 2014/09/07
 * @author ExBin Project (http://exbin.org)
 */
public interface XBCNodeTree {

    /**
     * Gets index.
     *
     * @return the index
     */
    Long getId();

    /**
     * Gets node.
     *
     * @return the node
     */
    XBCNode getOwner();

    /**
     * Gets node child.
     *
     * @return the node
     */
    XBCNode getNode();

    /**
     * Gets depth level.
     *
     * @return the depth
     */
    Integer getDepthLevel();

    /**
     * Gets root record for this node.
     *
     * @return root record
     */
    XBCRoot getRoot();
}
