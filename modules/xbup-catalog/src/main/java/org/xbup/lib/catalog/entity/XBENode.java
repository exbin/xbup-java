/*
 * Copyright (C) ExBin Project
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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import org.xbup.lib.core.catalog.base.XBCNode;

/**
 * Node database entity.
 *
 * @version 0.1.24 2014/09/07
 * @author ExBin Project (http://exbin.org)
 */
@Entity(name = "XBNode")
@Inheritance(strategy = InheritanceType.JOINED)
public class XBENode extends XBEItem implements XBCNode, Serializable {

    private Long cNameOrder;
    private Long cStriOrder;

    public XBENode() {
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
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XBENode)) {
            return false;
        }
        XBENode other = (XBENode) object;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * Returns a string representation of the object. This implementation
     * constructs that representation based on the id fields.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "org.xbup.catalog.entity.Node[id=" + id + "]";
    }

    @Override
    public XBCNode getParent() {
        return (XBCNode) super.getParent();
    }

    public void setOwner(XBENode node) {
        super.setParent(node);
    }

    public Long getcNameOrder() {
        return cNameOrder;
    }

    public void setcNameOrder(Long cNameOrder) {
        this.cNameOrder = cNameOrder;
    }

    public Long getcStriOrder() {
        return cStriOrder;
    }

    public void setcStriOrder(Long cStriOrder) {
        this.cStriOrder = cStriOrder;
    }
}
