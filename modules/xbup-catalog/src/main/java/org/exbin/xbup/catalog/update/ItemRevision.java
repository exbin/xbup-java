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
package org.exbin.xbup.catalog.update;

/**
 * Catalog item revision information record.
 *
 * @version 0.1.24 2015/01/13
 * @author ExBin Project (http://exbin.org)
 */
public class ItemRevision {

    private Long id;
    private Long xbIndex;
    private Long xbLimit;
    private String name;
    private String desc;

    public ItemRevision() {
        id = null;
        xbIndex = null;
        xbLimit = null;
    }

    public Long getXBIndex() {
        return xbIndex;
    }

    public void setXBIndex(Long xbIndex) {
        this.xbIndex = xbIndex;
    }

    public Long getXBLimit() {
        return xbLimit;
    }

    public void setXBLimit(Long xbLimit) {
        this.xbLimit = xbLimit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
