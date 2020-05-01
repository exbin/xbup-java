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
package org.exbin.xbup.core.catalog.base.service;

import java.util.List;
import org.exbin.xbup.core.catalog.base.XBCRev;
import org.exbin.xbup.core.catalog.base.XBCSpec;

/**
 * Interface for XBCRev items service.
 *
 * @version 0.1.25 2015/02/20
 * @author ExBin Project (http://exbin.org)
 * @param <T> revision class
 */
public interface XBCRevService<T extends XBCRev> extends XBCService<T> {

    /**
     * Gets revision of given XB index.
     *
     * @param spec specification
     * @param xbIndex XBIndex of given revision
     * @return revision
     */
    XBCRev findRevByXB(XBCSpec spec, long xbIndex);

    /**
     * Gets maximum revision XB index for given specification.
     *
     * @param spec specification
     * @return XB index
     */
    long findMaxRevXB(XBCSpec spec);

    /**
     * Gets revision of given order index.
     *
     * @param spec specification
     * @param index order index
     * @return revision
     */
    XBCRev getRev(XBCSpec spec, long index);

    /**
     * Gets list of all revisions.
     *
     * @param spec specification
     * @return list of revisions
     */
    List<XBCRev> getRevs(XBCSpec spec);

    /**
     * Returns count of revisions.
     *
     * @param spec specification
     * @return count of revisions
     */
    long getRevsCount(XBCSpec spec);

    /**
     * Removes specification revision with all dependencies.
     *
     * @param specDef revision to remove
     */
    void removeItemDepth(XBCRev specDef);

    /**
     * Creates new revision specific to particular specification.
     *
     * @param spec specification
     * @return new revision instance
     */
    XBCRev createRev(XBCSpec spec);

    /**
     * Gets sum of all up to defined revisions in catalog.
     *
     * @param spec parent specification
     * @param revision maximum revision xb index to sum up to
     * @return sum of revision's limits
     */
    long getRevsLimitSum(XBCSpec spec, long revision);
}
