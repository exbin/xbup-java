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
package org.xbup.lib.xbcatalog.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.xbup.lib.xb.catalog.base.XBCNode;
import org.xbup.lib.xb.catalog.base.XBCXFile;
import org.xbup.lib.xb.catalog.base.XBCXPlugin;

/**
 * Plugin database entity.
 *
 * @version 0.1 wr21.0 2011/08/21
 * @author XBUP Project (http://xbup.org)
 */
@Entity(name="XBXPlugin")
public class XBEXPlugin implements XBCXPlugin, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    private XBENode owner;
    @ManyToOne
    private XBEXFile pluginFile;
    private Long pluginIndex;

    public XBEXPlugin() {
    }

    @Override
    public XBCNode getOwner() {
        return (XBCNode) owner;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOwner(XBENode owner) {
        this.owner = owner;
    }

    @Override
    public XBCXFile getPluginFile() {
        return pluginFile;
    }

    public void setPluginFile(XBEXFile pluginFile) {
        this.pluginFile = pluginFile;
    }

    @Override
    public Long getPluginIndex() {
        return pluginIndex;
    }

    public void setPluginIndex(Long pluginIndex) {
        this.pluginIndex = pluginIndex;
    }
}
