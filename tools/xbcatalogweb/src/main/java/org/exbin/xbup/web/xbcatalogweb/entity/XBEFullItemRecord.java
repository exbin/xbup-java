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
package org.exbin.xbup.web.xbcatalogweb.entity;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.exbin.xbup.catalog.entity.XBEXDesc;
import org.exbin.xbup.catalog.entity.XBEXFile;
import org.exbin.xbup.catalog.entity.XBEXHDoc;
import org.exbin.xbup.catalog.entity.XBEXLanguage;
import org.exbin.xbup.catalog.entity.XBEXName;
import org.exbin.xbup.web.xbcatalogweb.base.XBCDefinitionRecord;
import org.exbin.xbup.web.xbcatalogweb.base.XBCFullItemRecord;

/**
 * Full item record entity.
 *
 * @version 0.1.24 2015/01/30
 * @author ExBin Project (http://exbin.org)
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
            hdocFile.setFilename(getStri() == null ? getId().toString() : getStri().getText() + ".html");
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

    public List<XBCDefinitionRecord> getDefinitions() {
        // TODO
        return new ArrayList<XBCDefinitionRecord>();
    }

    public String getHdocBodyText() {
        String text = getHdocText();
        if (text.isEmpty()) {
            return text;
        }

        int bodyPos = text.indexOf("<body");
        int startPos = text.indexOf(">", bodyPos + 5);
        
        int endPos = text.lastIndexOf("</body>");
        
        return text.substring(startPos + 1, endPos);
    }
}
