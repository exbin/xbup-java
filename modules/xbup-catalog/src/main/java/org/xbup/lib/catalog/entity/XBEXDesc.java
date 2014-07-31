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
package org.xbup.lib.catalog.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCXDesc;
import org.xbup.lib.core.catalog.base.XBCXLanguage;

/**
 * Item description database entity.
 *
 * @version 0.1 wr21.0 2011/11/16
 * @author XBUP Project (http://xbup.org)
 */
@Entity(name="XBXDesc")
public class XBEXDesc implements XBCXDesc, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    private XBEItem item;

    @ManyToOne
    private XBEXLanguage lang;

    private String text;

    public XBEXDesc() {
    }

    @Override
    public XBCItem getItem() {
        return item;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public XBCXLanguage getLang() {
        return lang;
    }

    @Override
    public void setItem(XBCItem item) {
        this.item = (XBEItem)item;
    }

    @Override
    public void setLang(XBCXLanguage lang) {
        this.lang = (XBEXLanguage)lang;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
