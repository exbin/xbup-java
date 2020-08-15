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
import java.util.Date;
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
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import org.exbin.xbup.catalog.modifiable.XBMRoot;
import org.exbin.xbup.core.catalog.base.XBCNode;

/**
 * Root node database entity.
 *
 * @version 0.2.1 2020/08/14
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Entity(name = "XBRoot")
@Inheritance(strategy = InheritanceType.JOINED)
public class XBERoot implements XBMRoot, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToOne
    private XBENode node;

    private String url;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastUpdate;

    public XBERoot() {
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns a hash code value for the object.
     *
     * This implementation computes a hash code value based on the id fields in
     * this object.
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
     * Determines whether another object is equal to this Item.
     *
     * The result is <code>true</code> if and only if the argument is not null
     * and is a Item object that has the same id field values as this object.
     *
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(@Nullable Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XBERoot)) {
            return false;
        }

        XBERoot other = (XBERoot) object;
        return this.id == other.id || (this.id != null && this.id.equals(other.id));
    }

    /**
     * Returns a string representation of the object.
     *
     * This implementation constructs that representation based on the id
     * fields.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "org.exbin.xbup.catalog.entity.Item[id=" + id + "]";
    }

    @Nonnull
    @Override
    public XBENode getNode() {
        return node;
    }

    @Override
    public void setNode(XBCNode node) {
        this.node = (XBENode) node;
    }

    @Nonnull
    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Nonnull
    @Override
    public Optional<Date> getLastUpdate() {
        return Optional.ofNullable(lastUpdate);
    }

    @Override
    public void setLastUpdate(@Nullable Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
