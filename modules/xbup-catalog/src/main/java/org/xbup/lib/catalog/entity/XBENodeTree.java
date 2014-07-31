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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.xbup.lib.core.catalog.base.XBCNodeTree;

/**
 * Node tree database entity.
 *
 * @version 0.1 wr23.0 2013/10/06
 * @author XBUP Project (http://xbup.org)
 */
@Entity(name="XBNodeTree")
public class XBENodeTree implements XBCNodeTree, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private XBENode node;
    @ManyToOne
    private XBENode child;

    private Integer depthLevel;

    public XBENodeTree() {}

    /**
     * Returns a hash code value for the object.
     * This implementation computes a hash code value based on the id fields
     * in this object.
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
     * Determines whether another object is equal to this Item.
     * The result is <code>true</code> if and only if the argument is not null
     * and is a Item object that has the same id field values as this object.
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

        XBENodeTree other = (XBENodeTree)object;
        return this.getId() == other.getId() || (this.getId() != null && this.id.equals(other.id));
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public XBENode getNode() {
        return node;
    }

    public void setNode(XBENode node) {
        this.node = node;
    }

    @Override
    public XBENode getChild() {
        return child;
    }

    public void setChild(XBENode child) {
        this.child = child;
    }

    @Override
    public Integer getDepthLevel() {
        return depthLevel;
    }

    public void setDepthLevel(Integer depthLevel) {
        this.depthLevel = depthLevel;
    }
}
