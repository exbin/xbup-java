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
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import org.exbin.xbup.catalog.modifiable.XBMXUserInfo;
import org.exbin.xbup.core.catalog.base.XBCXUser;

/**
 * User information database entity.
 *
 * @version 0.1.21 2011/08/30
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Entity(name = "XBXUserInfo")
public class XBEXUserInfo implements Serializable, XBMXUserInfo {

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
    public long getId() {
        if (id == null) {
            return 0;
        }
        return id;
    }

    @Nonnull
    @Override
    public XBEXUser getUser() {
        return user;
    }

    @Override
    public void setUser(XBCXUser user) {
        this.user = (XBEXUser) user;
    }

    @Nonnull
    @Override
    public Date getCreated() {
        return created;
    }

    @Override
    public void setCreated(Date created) {
        this.created = created;
    }

    @Nonnull
    @Override
    public Optional<Date> getUpdated() {
        return Optional.ofNullable(updated);
    }

    @Override
    public void setUpdated(@Nullable Date updated) {
        this.updated = updated;
    }

    @Nonnull
    @Override
    public Optional<Date> getLastLogin() {
        return Optional.ofNullable(lastLogin);
    }

    @Override
    public void setLastLogin(@Nullable Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Nonnull
    @Override
    public Optional<Date> getCurrLogin() {
        return Optional.ofNullable(currLogin);
    }

    @Override
    public void setCurrLogin(@Nullable Date currLogin) {
        this.currLogin = currLogin;
    }
}
