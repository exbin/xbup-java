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
package org.xbup.lib.core.catalog.base.service;

import java.util.List;
import org.xbup.lib.core.catalog.base.XBCRev;
import org.xbup.lib.core.catalog.base.XBCSpec;

/**
 * Interface for XBCRev items service.
 *
 * @version 0.1.24 2014/10/19
 * @author XBUP Project (http://xbup.org)
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
    public XBCRev findRevByXB(XBCSpec spec, long xbIndex);

    /**
     * Gets Revision of given index.
     *
     * @param id
     * @return revision
     */
    public XBCRev findRevById(long id);

    /**
     * Gets revision of given order index.
     *
     * @param spec specification
     * @param index order index
     * @return revision
     */
    public XBCRev getRev(XBCSpec spec, long index);

    /**
     * Gets list of all revisions.
     *
     * @param spec specification
     * @return list of revisions
     */
    public List<XBCRev> getRevs(XBCSpec spec);

    /**
     * Returns count of revisions.
     *
     * @param spec specification
     * @return count of revisions
     */
    public long getRevsCount(XBCSpec spec);

    /**
     * Gets count of all revisions in catalog.
     *
     * @return count of revisions
     */
    public Long getAllRevisionsCount();
}
