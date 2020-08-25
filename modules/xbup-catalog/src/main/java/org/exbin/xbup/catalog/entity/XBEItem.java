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
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.exbin.xbup.catalog.modifiable.XBMItem;
import org.exbin.xbup.core.catalog.base.XBCItem;

/**
 * Shared entity for various catalog items.
 *
 * @version 0.2.1 2020/08/13
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Entity(name = "XBItem")
@Inheritance(strategy = InheritanceType.JOINED)
public class XBEItem implements XBMItem, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    private Long xbIndex;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "OWNER_ID")
    private XBEItem parent;

    public XBEItem() {
    }

    @Override
    public long getId() {
        if (id == null) {
            return 0;
        }
        return id;
    }

    /**
     * Returns a hash code value for the object. This implementation computes a
     * hash code value based on the id fields in this object.
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    /**
     * Determines whether another object is equal to this Item. The result is
     * <code>true</code> if and only if the argument is not null and is a Item
     * object that has the same id field values as this object.
     *
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(@Nullable Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XBEItem)) {
            return false;
        }
        XBEItem other = (XBEItem) object;
        return Objects.equals(this.id, other.id) || (this.id != null && this.id.equals(other.id));
    }

    /**
     * Returns a string representation of the object. This implementation
     * constructs that representation based on the id fields.
     *
     * @return a string representation of the object
     */
    @Nonnull
    @Override
    public String toString() {
        return "org.exbin.xbup.catalog.entity.Item[id=" + id + "]";
    }

    @Override
    public long getXBIndex() {
        return xbIndex == null ? 0 : xbIndex;
    }

    @Override
    public void setXBIndex(long xbIndex) {
        this.xbIndex = xbIndex;
    }

    @Nonnull
    @Override
    public Optional<XBCItem> getParentItem() {
        return Optional.ofNullable(parent);
    }

    @Override
    public void setParentItem(@Nullable XBCItem parent) {
        this.parent = (XBEItem) parent;
    }
}
