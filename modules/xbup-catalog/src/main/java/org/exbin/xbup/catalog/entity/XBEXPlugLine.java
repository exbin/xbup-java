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
import org.exbin.xbup.catalog.modifiable.XBMXPlugLine;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;

/**
 * Plugin line editor database entity.
 *
 * @version 0.2.1 2020/08/14
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Entity(name = "XBXPlugLine")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class XBEXPlugLine implements XBMXPlugLine, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private XBEXPlugin plugin;
    private Long lineIndex;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Nonnull
    @Override
    public XBEXPlugin getPlugin() {
        return plugin;
    }

    @Override
    public void setPlugin(XBCXPlugin plugin) {
        this.plugin = (XBEXPlugin) plugin;
    }

    @Override
    public long getLineIndex() {
        return lineIndex;
    }

    @Override
    public void setLineIndex(long lineIndex) {
        this.lineIndex = lineIndex;
    }
}
