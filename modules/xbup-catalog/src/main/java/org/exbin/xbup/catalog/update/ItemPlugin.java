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
package org.exbin.xbup.catalog.update;

/**
 * Catalog plugin information record.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class ItemPlugin {

    private Long id;
    private Long plugin;
    private Long nodeId;
    private Long fileId;
    private Long fileNode;
    private String filename;

    public ItemPlugin() {
        id = null;
        plugin = null;
        nodeId = null;
        fileId = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlugin() {
        return plugin;
    }

    public void setPlugin(Long plugin) {
        this.plugin = plugin;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getFileNode() {
        return fileNode;
    }

    public void setFileNode(Long fileNode) {
        this.fileNode = fileNode;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
