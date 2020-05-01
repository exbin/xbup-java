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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import org.exbin.xbup.core.catalog.base.XBCXUserInfo;

/**
 * User information database entity.
 *
 * @version 0.1.21 2011/08/30
 * @author ExBin Project (http://exbin.org)
 */
@Entity(name = "XBXUserInfo")
public class XBEXUserInfo implements Serializable, XBCXUserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private XBEXUser user;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date created;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date updated;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date lastLogin;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date currLogin;

    public XBEXUserInfo() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public XBEXUser getUser() {
        return user;
    }

    public void setUser(XBEXUser user) {
        this.user = user;
    }

    @Override
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Override
    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public Date getCurrLogin() {
        return currLogin;
    }

    public void setCurrLogin(Date currLogin) {
        this.currLogin = currLogin;
    }
}
