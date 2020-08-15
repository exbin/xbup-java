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
import org.exbin.xbup.catalog.modifiable.XBMXPlugin;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCXFile;

/**
 * Plugin database entity.
 *
 * @version 0.2.1 2020/08/14
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Entity(name = "XBXPlugin")
public class XBEXPlugin implements XBMXPlugin, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private XBENode owner;
    @ManyToOne
    private XBEXFile pluginFile;
    private Long pluginIndex;

    public XBEXPlugin() {
    }

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
    public XBCNode getOwner() {
        return (XBCNode) owner;
    }

    @Override
    public void setOwner(XBCNode owner) {
        this.owner = (XBENode) owner;
    }

    @Nonnull
    @Override
    public XBCXFile getPluginFile() {
        return pluginFile;
    }

    @Override
    public void setPluginFile(XBCXFile pluginFile) {
        this.pluginFile = (XBEXFile) pluginFile;
    }

    @Override
    public long getPluginIndex() {
        return pluginIndex;
    }

    @Override
    public void setPluginIndex(long pluginIndex) {
        this.pluginIndex = pluginIndex;
    }
}
