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
package org.exbin.xbup.catalog.modifiable;

import java.util.Date;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.catalog.base.XBCXUser;
import org.exbin.xbup.core.catalog.base.XBCXUserInfo;

/**
 * Interface for catalog user information entity.
 *
 * @version 0.2.1 2020/08/10
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBMXUserInfo extends XBCXUserInfo, XBMBase {

    /**
     * Sets related user.
     *
     * @param user user
     */
    void setUser(XBCXUser user);

    /**
     * Sets date of creation.
     *
     * @param created created
     */
    void setCreated(Date created);

    /**
     * Sets date of current login.
     *
     * @param date date
     */
    void setCurrLogin(Date date);

    /**
     * Sets date of last login.
     *
     * @param lastLoginDate date
     */
    void setLastLogin(Date lastLoginDate);

    /**
     * Sets date of last change.
     *
     * @param date date
     */
    void setUpdated(Date date);
}
