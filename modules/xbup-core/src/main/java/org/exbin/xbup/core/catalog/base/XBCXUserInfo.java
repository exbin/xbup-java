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
package org.exbin.xbup.core.catalog.base;

import java.util.Date;

/**
 * Interface for catalog user information entity.
 *
 * @version 0.1.22 2012/09/09
 * @author ExBin Project (http://exbin.org)
 */
public interface XBCXUserInfo extends XBCBase {

    /**
     * Gets related user.
     *
     * @return the user
     */
    XBCXUser getUser();

    /**
     * Gets date of creation.
     *
     * @return the created
     */
    Date getCreated();

    /**
     * Gets date of current login.
     *
     * @return the currLogin
     */
    Date getCurrLogin();

    /**
     * Gets date of last login.
     *
     * @return the lastLogin
     */
    Date getLastLogin();

    /**
     * Gets date of last change.
     *
     * @return the updated
     */
    Date getUpdated();
}
