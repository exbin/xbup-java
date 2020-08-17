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
import org.exbin.xbup.catalog.modifiable.XBMXPlugUi;
import org.exbin.xbup.core.catalog.base.XBCXPlugUiType;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;

/**
 * UI editor database entity.
 *
 * @version 0.2.1 2020/08/17
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Entity(name = "XBXPlugUi")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class XBEXPlugUi implements XBMXPlugUi, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private XBEXPlugin plugin;
    @ManyToOne
    private XBEXPlugUiType uiType;
    private Long methodIndex;
    private String name;

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
    public XBEXPlugin getPlugin() {
        return plugin;
    }

    @Override
    public void setPlugin(XBCXPlugin plugin) {
        this.plugin = (XBEXPlugin) plugin;
    }

    @Override
    public XBCXPlugUiType getUiType() {
        return uiType;
    }

    @Override
    public void setUiType(XBCXPlugUiType uiType) {
        this.uiType = (XBEXPlugUiType) uiType;
    }

    @Override
    public long getMethodIndex() {
        return methodIndex;
    }

    @Override
    public void setMethodIndex(long methodIndex) {
        this.methodIndex = methodIndex;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Nonnull
    @Override
    public String getName() {
        return name == null ? "" : name;
    }
}
