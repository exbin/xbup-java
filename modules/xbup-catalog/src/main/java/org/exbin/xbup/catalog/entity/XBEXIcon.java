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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.exbin.xbup.core.catalog.base.XBCXIcon;

/**
 * Item icon database entity.
 *
 * @version 0.1.23 2014/11/26
 * @author ExBin Project (http://exbin.org)
 */
@Entity(name = "XBXIcon")
public class XBEXIcon implements XBCXIcon, Serializable {

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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public XBEItem getParent() {
        return parent;
    }

    public void setParent(XBEItem parent) {
        this.parent = parent;
    }

    @Override
    public XBEXIconMode getMode() {
        return mode;
    }

    public void setMode(XBEXIconMode mode) {
        this.mode = mode;
    }

    @Override
    public XBEXFile getIconFile() {
        return iconFile;
    }

    public void setIconFile(XBEXFile iconFile) {
        this.iconFile = iconFile;
    }
}
