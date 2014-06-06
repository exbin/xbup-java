/*
 * Copyright (C) XBUP Project
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
package org.xbup.lib.xb.catalog.update;

/**
 * Path to revision plugin.
 *
 * @version 0.1 wr19.0 2010/07/17
 * @author XBUP Project (http://xbup.org)
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
