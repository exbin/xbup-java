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
import org.exbin.xbup.catalog.modifiable.XBMXStri;
import org.exbin.xbup.core.catalog.base.XBCItem;

/**
 * Item string identification keys database entity.
 *
 * @version 0.2.1 2020/08/10
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Entity(name = "XBXStri")
public class XBEXStri implements XBMXStri, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private XBEItem item;

    private String text;

    // Cached path of the parent node.
    private String nodePath;

    public XBEXStri() {
    }

    @Override
    public long getId() {
        return id;
    }

    @Nonnull
    @Override
    public XBCItem getItem() {
        return item;
    }

    @Override
    public void setItem(XBCItem item) {
        this.item = (XBEItem) item;
    }

    @Nonnull
    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Nonnull
    @Override
    public String getNodePath() {
        return nodePath;
    }

    @Override
    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
    }
}
