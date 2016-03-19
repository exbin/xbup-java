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
package org.exbin.xbup.core.catalog.base.manager;

import java.util.List;
import org.exbin.xbup.core.catalog.base.XBCRev;
import org.exbin.xbup.core.catalog.base.XBCSpec;

/**
 * Interface for XBCRev catalog manager.
 *
 * @version 0.1.25 2015/02/20
 * @author ExBin Project (http://exbin.org)
 * @param <T> revision entity
 */
public interface XBCRevManager<T extends XBCRev> extends XBCCatalogManager<T> {

    /**
     * Gets revision of given index.
     *
     * @param spec parent specification
     * @param xbIndex XBIndex of given revision
     * @return revision
     */
    public XBCRev findRevByXB(XBCSpec spec, long xbIndex);

    /**
     * Gets maximum revision XB index for given specification.
     *
     * @param spec specification
     * @return XB index
     */
    public Long findMaxRevXB(XBCSpec spec);

    /**
     * Gets revision of given index.
     *
     * @param spec parent specification
     * @param index order index
     * @return revision
     */
    public XBCRev getRev(XBCSpec spec, long index);

    /**
     * Gets list of all revisions.
     *
     * @param spec parent specification
     * @return list of revisions
     */
    public List<XBCRev> getRevs(XBCSpec spec);

    /**
     * Returns count of revisions.
     *
     * @param spec parent specification
     * @return count of revisions
     */
    public long getRevsCount(XBCSpec spec);

    /**
     * Gets sum of all up to defined revisions in catalog.
     *
     * @param spec parent specification
     * @param revision maximum revision xb index to sum up to
     * @return sum of revision's limits
     */
    public long getRevsLimitSum(XBCSpec spec, long revision);
}
