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
import org.xbup.lib.xb.catalog.base.XBCItem;
import org.xbup.lib.xb.catalog.base.XBCXStri;

/**
 * Item string identification keys database entity.
 *
 * @version 0.1 wr21.0 2012/04/18
 * @author XBUP Project (http://xbup.org)
 */
@Entity(name="XBXStri")
public class XBEXStri implements XBCXStri, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    private XBEItem item;

    private String text;

    // Cached path of the parent node.
    private String nodePath;

    public XBEXStri() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public XBCItem getItem() {
        return item;
    }

    @Override
    public void setItem(XBCItem item) {
        this.item = (XBEItem)item;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getNodePath() {
        return nodePath;
    }

    @Override
    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
    }

}
