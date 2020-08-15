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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import org.exbin.xbup.catalog.modifiable.XBMXBlockPane;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCXPlugPane;

/**
 * Block panel editor database entity.
 *
 * @version 0.2.1 2020/08/14
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Entity(name = "XBXBlockPane")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class XBEXBlockPane implements XBMXBlockPane, Serializable {

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
    public long getId() {
        if (id == null) {
            return 0;
        }
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Nonnull
    @Override
    public XBEBlockRev getBlockRev() {
        return blockRev;
    }

    @Override
    public void setBlockRev(XBCBlockRev blockRev) {
        this.blockRev = (XBEBlockRev) blockRev;
    }

    @Nonnull
    @Override
    public XBEXPlugPane getPane() {
        return pane;
    }

    @Override
    public void setPane(XBCXPlugPane pane) {
        this.pane = (XBEXPlugPane) pane;
    }

    @Override
    public long getPriority() {
        return priority;
    }

    @Override
    public void setPriority(long priority) {
        this.priority = priority;
    }
}
