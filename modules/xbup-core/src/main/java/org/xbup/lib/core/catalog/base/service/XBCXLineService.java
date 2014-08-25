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
import org.xbup.lib.core.catalog.base.XBCBlockRev;
import org.xbup.lib.core.catalog.base.XBCXBlockLine;
import org.xbup.lib.core.catalog.base.XBCXPlugLine;
import org.xbup.lib.core.catalog.base.XBCXPlugin;
import org.xbup.lib.core.catalog.base.XBCExtension;

/**
 * Interface for XBCXBlockLine items service.
 *
 * @version 0.1.21 2011/12/31
 * @author XBUP Project (http://xbup.org)
 */
public interface XBCXLineService<T extends XBCXBlockLine> extends XBCService<T>, XBCExtension {

    /** Get list of all block revision lines */
    public List<XBCXBlockLine> getLines(XBCBlockRev rev);

    /** Get count of line editors for given line */
    public long getLinesCount(XBCBlockRev rev);

    /** Get line editor for given priority */
    public XBCXBlockLine findLineByPR(XBCBlockRev rev, long priority);

    public XBCXBlockLine findById(long id);

    public XBCXPlugLine findPlugLineById(long id);

    public Long getAllLinesCount();

    /** Get list of all plugin lines */
    public List<XBCXPlugLine> getPlugLines(XBCXPlugin plugin);

    /** Get count of line editors for given plugin */
    public long getPlugLinesCount(XBCXPlugin plugin);

    public XBCXPlugLine getPlugLine(XBCXPlugin plugin, long lineIndex);

    public Long getAllPlugLinesCount();
}
