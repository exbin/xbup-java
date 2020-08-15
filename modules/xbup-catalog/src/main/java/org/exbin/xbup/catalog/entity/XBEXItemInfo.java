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
import java.sql.Time;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.exbin.xbup.catalog.modifiable.XBMXItemInfo;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXUser;

/**
 * Item information database entity.
 *
 * @version 0.2.1 2020/08/14
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Entity(name = "XBItemInfo")
public class XBEXItemInfo implements XBMXItemInfo, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private XBEItem item;
    @ManyToOne
    private XBEXUser owner;
    @ManyToOne
    private XBEXUser createdByUser;
    private Time creationDate;

    public XBEXItemInfo() {
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
        if (!(object instanceof XBEXItemInfo)) {
            return false;
        }
        XBEXItemInfo other = (XBEXItemInfo) object;
        return !(!Objects.equals(this.id, other.id) && (this.id == null || !this.id.equals(other.id)));
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
        return "org.exbin.xbup.catalog.entity.Node[id=" + id + "]";
    }

    @Nonnull
    @Override
    public XBCItem getItem() {
        return item;
    }

    @Override
    public void setItem(XBCItem item) {
        this.item = (XBEItem) item;
    }

    @Nonnull
    @Override
    public XBEXUser getOwner() {
        return owner;
    }

    @Override
    public void setOwner(XBCXUser owner) {
        this.owner = (XBEXUser) owner;
    }

    @Nonnull
    @Override
    public XBEXUser getCreatedByUser() {
        return createdByUser;
    }

    @Override
    public void setCreatedByUser(XBCXUser createdByUser) {
        this.createdByUser = (XBEXUser) createdByUser;
    }

    @Nonnull
    @Override
    public Time getCreationDate() {
        return creationDate;
    }

    @Override
    public void setCreationDate(Time creationDate) {
        this.creationDate = creationDate;
    }
}
