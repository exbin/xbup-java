/*
 * Copyright (C) XBUP Project
 *
 * This application or library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This application or library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along this application.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.xbup.lib.catalog.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.xbup.lib.core.catalog.base.XBCNodeTree;

/**
 * Node tree database entity.
 *
 * @version 0.1.24 2014/12/10
 * @author XBUP Project (http://xbup.org)
 */
@Entity(name = "XBNodeTree")
public class XBENodeTree implements XBCNodeTree, Serializable {

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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
        hash += (this.getId() != null ? this.getId().hashCode() : 0);
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
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XBENodeTree)) {
            return false;
        }

        XBENodeTree other = (XBENodeTree) object;
        return Objects.equals(this.getId(), other.getId()) || (this.getId() != null && this.id.equals(other.id));
    }

    @Override
    public XBENode getNode() {
        return node;
    }

    public void setNode(XBENode node) {
        this.node = node;
    }

    @Override
    public XBENode getOwner() {
        return owner;
    }

    public void setOwner(XBENode owner) {
        this.owner = owner;
    }

    @Override
    public XBERoot getRoot() {
        return root;
    }

    public void setRoot(XBERoot root) {
        this.root = root;
    }

    @Override
    public Integer getDepthLevel() {
        return depthLevel;
    }

    public void setDepthLevel(Integer depthLevel) {
        this.depthLevel = depthLevel;
    }
    
    public static GenerationType getGenerationType() {
        return GenerationType.AUTO;
    }
}
