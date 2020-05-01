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
package org.exbin.xbup.catalog.update;

/**
 * Path to revision plugin.
 *
 * @version 0.1.19 2010/07/17
 * @author ExBin Project (http://exbin.org)
 */
public class RevisionPlug {

    private Long id;
    private Long itemId;
    private Long plugId;
    private Long plugin;
    private Long plugNode;

    public RevisionPlug() {
        id = null;
        itemId = null;
        plugId = null;
        plugin = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getPlugId() {
        return plugId;
    }

    public void setPlugId(Long plugId) {
        this.plugId = plugId;
    }

    public Long getPlugin() {
        return plugin;
    }

    public void setPlugin(Long plugin) {
        this.plugin = plugin;
    }

    public Long getPlugNode() {
        return plugNode;
    }

    public void setPlugNode(Long plugNode) {
        this.plugNode = plugNode;
    }
}
