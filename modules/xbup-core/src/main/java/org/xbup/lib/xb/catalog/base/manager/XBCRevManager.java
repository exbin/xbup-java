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
package org.xbup.lib.xb.catalog.base.manager;

import java.util.List;
import org.xbup.lib.xb.catalog.base.XBCRev;
import org.xbup.lib.xb.catalog.base.XBCSpec;

/**
 * Interface for XBCRev catalog manager.
 *
 * @version 0.1 wr21.0 2011/12/31
 * @author XBUP Project (http://xbup.org)
 * @param <T> revision entity
 */
public interface XBCRevManager<T extends XBCRev> extends XBCCatalogManager<T> {

    /**
     * Get revision of given index.
     *
     * @param spec parent specification
     * @param xbIndex XBIndex of given revision
     * @return revision
     */
    public XBCRev findRevByXB(XBCSpec spec, long xbIndex);

    /**
     * Get Revision of given index.
     *
     * @param id unique index
     * @return revision
     */
    public XBCRev findRevById(long id);

    /**
     * Get revision of given index.
     *
     * @param spec parent specification
     * @param index order index
     * @return revision
     */
    public XBCRev getRev(XBCSpec spec, long index);

    /**
     * Get list of all revisions.
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
     * Get count of all revisions in catalog.
     *
     * @return count of revisions
     */
    public Long getAllRevisionsCount();
}
