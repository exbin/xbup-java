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
package org.exbin.xbup.catalog.update;

/**
 * Path to revision structure.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class RevisionPath {

    private Long[] path;
    private Long specId;
    private Long revXBId;
    private Long bindType;
    private String name;
    private String desc;
    private String stri;

    public RevisionPath() {
        path = null;
        specId = null;
    }

    public Long[] getPath() {
        return path;
    }

    public void setPath(Long[] path) {
        this.path = path;
    }

    public Long getSpecId() {
        return specId;
    }

    public void setSpecId(Long specId) {
        this.specId = specId;
    }

    public Long getRevXBId() {
        return revXBId;
    }

    public void setRevXBId(Long revXBId) {
        this.revXBId = revXBId;
    }

    public Long getBindType() {
        return bindType;
    }

    public void setBindType(Long bindType) {
        this.bindType = bindType;
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

    public String getStri() {
        return stri;
    }

    public void setStri(String stri) {
        this.stri = stri;
    }
}
