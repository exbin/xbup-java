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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.xbup.lib.core.catalog.base.XBCXIcon;

/**
 * Item icon database entity.
 *
 * @version 0.1.23 2014/11/26
 * @author ExBin Project (http://exbin.org)
 */
@Entity(name = "XBXIcon")
public class XBEXIcon implements XBCXIcon, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "OWNER_ID")
    private XBEItem parent;
    @ManyToOne
    private XBEXIconMode mode;
    @ManyToOne
    private XBEXFile iconFile;

    public XBEXIcon() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public XBEItem getParent() {
        return parent;
    }

    public void setParent(XBEItem parent) {
        this.parent = parent;
    }

    @Override
    public XBEXIconMode getMode() {
        return mode;
    }

    public void setMode(XBEXIconMode mode) {
        this.mode = mode;
    }

    @Override
    public XBEXFile getIconFile() {
        return iconFile;
    }

    public void setIconFile(XBEXFile iconFile) {
        this.iconFile = iconFile;
    }
}
