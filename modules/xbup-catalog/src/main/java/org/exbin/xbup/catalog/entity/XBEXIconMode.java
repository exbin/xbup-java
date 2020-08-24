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
import org.exbin.xbup.catalog.modifiable.XBMXIconMode;

/**
 * Item Icon mode database entity.
 *
 * @version 0.2.1 2020/08/14
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Entity(name = "XBXIconMode")
public class XBEXIconMode implements XBMXIconMode, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long type;
    private String mIME;
    private String caption;

    public XBEXIconMode() {
    }

    @Override
    public long getId() {
        if (id == null) {
            return 0;
        }
        return id;
    }

    @Override
    public long getType() {
        return type;
    }

    @Override
    public void setType(long type) {
        this.type = type;
    }

    @Nonnull
    @Override
    public String getMIME() {
        return mIME;
    }

    @Override
    public void setMIME(String mIME) {
        this.mIME = mIME;
    }

    @Nonnull
    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public void setCaption(String caption) {
        this.caption = caption;
    }
}
