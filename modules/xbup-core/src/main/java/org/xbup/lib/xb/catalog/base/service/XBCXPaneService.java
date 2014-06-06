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
package org.xbup.lib.xb.catalog.base.service;

import java.util.List;
import org.xbup.lib.xb.catalog.base.XBCBlockRev;
import org.xbup.lib.xb.catalog.base.XBCXBlockPane;
import org.xbup.lib.xb.catalog.base.XBCXPlugPane;
import org.xbup.lib.xb.catalog.base.XBCXPlugin;
import org.xbup.lib.xb.catalog.base.XBCExtension;

/**
 * Interface for XBCXBlockPane items service.
 *
 * @version 0.1 wr21.0 2011/12/31
 * @author XBUP Project (http://xbup.org)
 */
public interface XBCXPaneService<T extends XBCXBlockPane> extends XBCService<T>, XBCExtension {

    /** Get list of all binds */
    public List<XBCXBlockPane> getPanes(XBCBlockRev rev);

    /** Get count of panel editors for given revision */
    public long getPanesCount(XBCBlockRev rev);

    /** Get panel editor for given priority */
    public XBCXBlockPane findPaneByPR(XBCBlockRev rev, long priority);

    public XBCXBlockPane findById(long id);

    public XBCXPlugPane findPlugPaneById(long id);

    public Long getAllPanesCount();

    /** Get list of all plugin panels */
    public List<XBCXPlugPane> getPlugPanes(XBCXPlugin plugin);

    /** Get count of panel editors for given plugin */
    public long getPlugPanesCount(XBCXPlugin plugin);

    public XBCXPlugPane getPlugPane(XBCXPlugin plugin, long pane);

    public Long getAllPlugPanesCount();
}
