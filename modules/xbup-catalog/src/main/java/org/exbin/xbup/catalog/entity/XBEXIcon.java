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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.exbin.xbup.catalog.modifiable.XBMXIcon;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.core.catalog.base.XBCXIconMode;

/**
 * Item icon database entity.
 *
 * @version 0.2.1 2020/08/14
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Entity(name = "XBXIcon")
public class XBEXIcon implements XBMXIcon, Serializable {

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
    public long getId() {
        if (id == null) {
            return 0;
        }
        return id;
    }

    @Nonnull
    @Override
    public XBEItem getParent() {
        return parent;
    }

    @Override
    public void setParent(XBCItem parent) {
        this.parent = (XBEItem) parent;
    }

    @Nonnull
    @Override
    public XBEXIconMode getMode() {
        return mode;
    }

    @Override
    public void setMode(XBCXIconMode mode) {
        this.mode = (XBEXIconMode) mode;
    }

    @Nonnull
    @Override
    public XBEXFile getIconFile() {
        return iconFile;
    }

    @Override
    public void setIconFile(XBCXFile iconFile) {
        this.iconFile = (XBEXFile) iconFile;
    }
}
