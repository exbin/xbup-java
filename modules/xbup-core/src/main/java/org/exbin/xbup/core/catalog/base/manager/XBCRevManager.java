/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.catalog.base.manager;

import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.catalog.base.XBCRev;
import org.exbin.xbup.core.catalog.base.XBCSpec;

/**
 * Interface for XBCRev catalog manager.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBCRevManager extends XBCCatalogManager<XBCRev> {

    /**
     * Gets revision of given index.
     *
     * @param spec parent specification
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
    Long findMaxRevXB(XBCSpec spec);

    /**
     * Gets revision of given index.
     *
     * @param spec parent specification
     * @param index order index
     * @return revision
     */
    XBCRev getRev(XBCSpec spec, long index);

    /**
     * Gets list of all revisions.
     *
     * @param spec parent specification
     * @return list of revisions
     */
    List<XBCRev> getRevs(XBCSpec spec);

    /**
     * Returns count of revisions.
     *
     * @param spec parent specification
     * @return count of revisions
     */
    long getRevsCount(XBCSpec spec);

    /**
     * Gets sum of all up to defined revisions in catalog.
     *
     * @param spec parent specification
     * @param revision maximum revision xb index to sum up to
     * @return sum of revision's limits
     */
    long getRevsLimitSum(XBCSpec spec, long revision);
}
