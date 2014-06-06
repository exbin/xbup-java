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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.xbup.lib.xb.catalog.base.XBCXIcon;

/**
 * Item icon database entity.
 *
 * @version 0.1 wr23.0 2014/05/19
 * @author XBUP Project (http://xbup.org)
 */
@Entity(name="XBXIcon")
public class XBEXIcon implements XBCXIcon, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long xbIndex;
    @ManyToOne
    @JoinColumn(name = "OWNER_ID")
    private XBEItem parent;
    @ManyToOne
    private XBEXIconMode mode;
    @ManyToOne
    private XBEXFile iconFile;

    /** Creates a new instance of XBESpec */
    public XBEXIcon() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the xbIndex
     */
    @Override
    public Long getXBIndex() {
        return xbIndex;
    }

    /**
     * @param xbIndex the xbIndex to set
     */
    public void setXBIndex(Long xbIndex) {
        this.xbIndex = xbIndex;
    }

    /**
     * @return the parent
     */
    @Override
    public XBEItem getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(XBEItem parent) {
        this.parent = parent;
    }

    /**
     * @return the mode
     */
    @Override
    public XBEXIconMode getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(XBEXIconMode mode) {
        this.mode = mode;
    }

    /**
     * @return the iconFile
     */
    @Override
    public XBEXFile getIconFile() {
        return iconFile;
    }

    /**
     * @param iconFile the iconFile to set
     */
    public void setIconFile(XBEXFile iconFile) {
        this.iconFile = iconFile;
    }
}
