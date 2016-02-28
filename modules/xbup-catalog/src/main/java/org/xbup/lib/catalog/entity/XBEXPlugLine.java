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
package org.xbup.lib.catalog.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import org.xbup.lib.core.catalog.base.XBCXPlugLine;

/**
 * Plugin line editor database entity.
 *
 * @version 0.1.21 2011/08/21
 * @author ExBin Project (http://exbin.org)
 */
@Entity(name = "XBXPlugLine")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class XBEXPlugLine implements XBCXPlugLine, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private XBEXPlugin plugin;
    private Long lineIndex;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public XBEXPlugin getPlugin() {
        return plugin;
    }

    public void setPlugin(XBEXPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Long getLineIndex() {
        return lineIndex;
    }

    public void setLineIndex(Long lineIndex) {
        this.lineIndex = lineIndex;
    }
}
