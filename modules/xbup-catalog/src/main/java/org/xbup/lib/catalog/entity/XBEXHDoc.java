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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.xbup.lib.core.catalog.base.XBCXHDoc;

/**
 * Item HTML documentation database entity.
 *
 * @version 0.1.21 2012/01/27
 * @author ExBin Project (http://exbin.org)
 */
@Entity(name = "XBXHDoc")
public class XBEXHDoc implements XBCXHDoc, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private XBEItem item;

    @ManyToOne
    private XBEXLanguage lang;

    @OneToOne
    private XBEXFile docFile;

    public XBEXHDoc() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public XBEXFile getDocFile() {
        return docFile;
    }

    public void setDocFile(XBEXFile docFile) {
        this.docFile = docFile;
    }

    @Override
    public XBEItem getItem() {
        return item;
    }

    public void setItem(XBEItem item) {
        this.item = item;
    }

    @Override
    public XBEXLanguage getLang() {
        return lang;
    }

    public void setLang(XBEXLanguage lang) {
        this.lang = lang;
    }
}
