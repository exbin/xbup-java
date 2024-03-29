/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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
import org.exbin.xbup.catalog.modifiable.XBMXName;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXLanguage;

/**
 * Item name database entity.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
@Entity(name = "XBXName")
public class XBEXName implements XBMXName, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private XBEItem item;

    @ManyToOne
    private XBEXLanguage lang;

    private String text;

    public XBEXName() {
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
    public XBCItem getItem() {
        return item;
    }

    @Nonnull
    @Override
    public String getText() {
        return text;
    }

    @Nonnull
    @Override
    public XBCXLanguage getLang() {
        return lang;
    }

    @Override
    public void setItem(XBCItem item) {
        this.item = (XBEItem) item;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void setLang(XBCXLanguage lang) {
        this.lang = (XBEXLanguage) lang;
    }
}
