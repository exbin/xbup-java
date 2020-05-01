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

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import org.exbin.xbup.core.catalog.base.XBCRev;
import org.exbin.xbup.core.catalog.base.XBCSpec;

/**
 * Revision database entity.
 *
 * @version 0.1.21 2011/08/21
 * @author ExBin Project (http://exbin.org)
 */
@Entity(name = "XBRev")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class XBERev extends XBEItem implements XBCRev {

    private Long xbLimit;

    /**
     * Returns maximum XBIndex of specification bind
     *
     * @return the XBLimit
     */
    @Override
    public Long getXBLimit() {
        return xbLimit;
    }

    @Override
    public XBCSpec getParent() {
        return (XBCSpec) super.getParent();
    }

    public void setXBLimit(Long xbLimit) {
        this.xbLimit = xbLimit;
    }
}
