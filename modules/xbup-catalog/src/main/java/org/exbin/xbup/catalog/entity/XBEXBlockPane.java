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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import org.exbin.xbup.core.catalog.base.XBCXBlockPane;

/**
 * Block panel editor database entity.
 *
 * @version 0.1.21 2011/08/21
 * @author ExBin Project (http://exbin.org)
 */
@Entity(name = "XBXBlockPane")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class XBEXBlockPane implements XBCXBlockPane, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private XBEBlockRev blockRev;
    @ManyToOne
    private XBEXPlugPane pane;
    private Long priority;

    public XBEXBlockPane() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public XBEBlockRev getBlockRev() {
        return blockRev;
    }

    public void setBlockRev(XBEBlockRev blockRev) {
        this.blockRev = blockRev;
    }

    @Override
    public XBEXPlugPane getPane() {
        return pane;
    }

    public void setPane(XBEXPlugPane pane) {
        this.pane = pane;
    }

    @Override
    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }
}
