/*
 * Copyright (C) ExBin Project
 *
 * This application or library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This application or library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along this application.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.exbin.xbup.catalog.update;

/**
 * Catalog plugin information record.
 *
 * @version 0.1.19 2010/07/17
 * @author ExBin Project (http://exbin.org)
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
