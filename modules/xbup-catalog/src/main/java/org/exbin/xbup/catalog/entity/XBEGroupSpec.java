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
package org.exbin.xbup.catalog.entity;

import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import org.exbin.xbup.catalog.modifiable.XBMGroupSpec;
import org.exbin.xbup.core.catalog.base.XBCNode;

/**
 * Group specification database entity.
 *
 * @author ExBin Project (https://exbin.org)
 */
@Entity(name = "XBGroupSpec")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class XBEGroupSpec extends XBESpec implements XBMGroupSpec, Serializable {

    public XBEGroupSpec() {
    }

    @Nonnull
    @Override
    public XBCNode getParent() {
        return super.getParent();
    }
}
