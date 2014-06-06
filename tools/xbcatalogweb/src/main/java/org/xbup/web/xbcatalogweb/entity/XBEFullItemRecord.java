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
package org.xbup.web.xbcatalogweb.entity;

import org.xbup.web.xbcatalogweb.base.XBCFullItemRecord;
import java.io.Serializable;
import java.nio.charset.Charset;
import org.xbup.lib.xbcatalog.entity.XBEXDesc;
import org.xbup.lib.xbcatalog.entity.XBEXFile;
import org.xbup.lib.xbcatalog.entity.XBEXHDoc;
import org.xbup.lib.xbcatalog.entity.XBEXLanguage;
import org.xbup.lib.xbcatalog.entity.XBEXName;

/**
 * Full item record entity.
 *
 * @version 0.1 wr23.0 2014/04/08
 * @author XBUP Project (http://xbup.org)
 */
public class XBEFullItemRecord extends XBEItemRecord implements Serializable, XBCFullItemRecord {

    private XBEXHDoc hdoc;
    private XBEXLanguage language;

    @Override
    public String getParentNode() {
        return "";
    }

    @Override
    public void setParentNode(String parentNode) {
    }

    @Override
    public String getStringId() {
        return getStri() == null ? "" : getStri().getText();
    }

    @Override
    public void setStringId(String stringId) {
    }

    @Override
    public String getDescription() {
        return getDesc() == null ? "" : getDesc().getText();
    }

    @Override
    public void setDescription(String description) {
        if (getDesc() == null) {
            XBEXDesc desc = new XBEXDesc();
            desc.setItem(getItem());
            desc.setLang(language);
            setDesc(desc);
        }
        
        getDesc().setText(description);
    }
    
    @Override
    public String getItemName() {
        return getName() == null ? "" : getName().getText();
    }
    
    @Override
    public void setItemName(String itemName) {
        if (getName() == null) {
            XBEXName name = new XBEXName();
            name.setItem(getItem());
            name.setLang(language);
            setName(name);
        }
        
        getName().setText(itemName);
    }

    @Override
    public String getHdocText() {
        return hdoc == null ? "" : (hdoc.getDocFile().getContent() == null ? "" : new String(hdoc.getDocFile().getContent()));
    }

    @Override
    public void setHdocText(String hdocText) {
        if (hdoc == null) {
            XBEXFile hdocFile = new XBEXFile();
            hdocFile.setFilename(getStri() == null ? getId().toString() : getStri().getText()+".html");
            hdoc = new XBEXHDoc();
            hdoc.setDocFile(hdocFile);
            hdoc.setLang(language);
        }

        hdoc.getDocFile().setContent(hdocText.getBytes(Charset.forName("UTF-8")));
    }

    @Override
    public XBEXLanguage getLanguage() {
        return language;
    }

    @Override
    public void setLanguage(XBEXLanguage language) {
        this.language = language;
    }

    @Override
    public XBEXHDoc getHdoc() {
        return hdoc;
    }

    @Override
    public void setHdoc(XBEXHDoc hdoc) {
        this.hdoc = hdoc;
    }
}
