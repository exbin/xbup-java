/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.catalog.entity;

import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.exbin.xbup.catalog.modifiable.XBMXHDoc;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.core.catalog.base.XBCXLanguage;

/**
 * Item HTML documentation database entity.
 *
 * @version 0.2.1 2020/08/14
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Entity(name = "XBXHDoc")
public class XBEXHDoc implements XBMXHDoc, Serializable {

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
    public long getId() {
        if (id == null) {
            return 0;
        }
        return id;
    }

    @Nonnull
    @Override
    public XBEXFile getDocFile() {
        return docFile;
    }

    @Override
    public void setDocFile(XBCXFile docFile) {
        this.docFile = (XBEXFile) docFile;
    }

    @Nonnull
    @Override
    public XBEItem getItem() {
        return item;
    }

    @Override
    public void setItem(XBCItem item) {
        this.item = (XBEItem) item;
    }

    @Nonnull
    @Override
    public XBEXLanguage getLang() {
        return lang;
    }

    @Override
    public void setLang(XBCXLanguage lang) {
        this.lang = (XBEXLanguage) lang;
    }
}
