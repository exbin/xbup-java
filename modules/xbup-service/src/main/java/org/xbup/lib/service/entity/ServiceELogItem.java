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
package org.xbup.lib.service.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import org.xbup.lib.service.base.ServiceLogItem;

/**
 * Service log item entity.
 *
 * @version 0.2.0 2016/02/20
 * @author ExBin Project (http://exbin.org)
 */
@Entity(name = "ServiceLogtem")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ServiceELogItem implements ServiceLogItem, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date created;

    @Lob
    private byte[] requestData;

    @Lob
    private byte[] responseData;

    public ServiceELogItem() {
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
        if (!(object instanceof ServiceELogItem)) {
            return false;
        }
        ServiceELogItem other = (ServiceELogItem) object;
        return Objects.equals(this.id, other.id) || (this.id != null && this.id.equals(other.id));
    }

    /**
     * Returns a string representation of the object. This implementation
     * constructs that representation based on the id fields.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "org.xbup.lib.service.entity.ServiceLogItem[id=" + id + "]";
    }

    @Override
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public byte[] getRequestData() {
        return requestData;
    }

    public void setRequestData(byte[] requestData) {
        this.requestData = requestData;
    }

    public byte[] getResponseData() {
        return responseData;
    }

    public void setResponseData(byte[] responseData) {
        this.responseData = responseData;
    }
}
