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
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.exbin.xbup.catalog.modifiable.XBMNodeTree;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCRoot;

/**
 * Node tree database entity.
 *
 * @version 0.2.1 2020/08/14
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Entity(name = "XBNodeTree")
public class XBENodeTree implements XBMNodeTree, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private XBENode owner;
    @ManyToOne
    @JoinColumn(nullable = false)
    private XBENode node;
    @ManyToOne
    private XBERoot root;

    @Column(nullable = false)
    private Integer depthLevel;

    public XBENodeTree() {
    }

    @Override
    public long getId() {
        return id == 0 ? 0 : id;
    }

    /**
     * Returns a hash code value for the object. This implementation computes a
     * hash code value based on the id fields in this object.
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Long.hashCode(this.getId());
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
        if (!(object instanceof XBENodeTree)) {
            return false;
        }

        XBENodeTree other = (XBENodeTree) object;
        return this.getId() == other.getId();
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
    public XBENode getOwner() {
        return owner;
    }

    @Override
    public void setOwner(XBCNode owner) {
        this.owner = (XBENode) owner;
    }

    @Nonnull
    @Override
    public XBERoot getRoot() {
        return root;
    }

    @Override
    public void setRoot(XBCRoot root) {
        this.root = (XBERoot) root;
    }

    @Override
    public int getDepthLevel() {
        return depthLevel;
    }

    @Override
    public void setDepthLevel(int depthLevel) {
        this.depthLevel = depthLevel;
    }

    @Nonnull
    public static GenerationType getGenerationType() {
        return GenerationType.AUTO;
    }
}
