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
package org.xbup.lib.plugin;

import net.xeoh.plugins.base.Plugin;

/**
 * XBUP Editor plugin - provides editing panel for XBUP data.
 *
 * @version 0.1.19 2010/08/02
 * @author XBUP Project (http://xbup.org)
 */
public interface XBPlugin extends Plugin {

    /** @return Identification catalog path of this plugin file */
    public String getPluginPath();

    /** Get count of line editors */
    public long getLineEditorsCount();

    /** Get specific line editor */
    public XBLineEditor getLineEditor(long index);

    /** Get count of panel editors */
    public long getPanelEditorsCount();

    /** Get specific panel editor */
    public XBPanelEditor getPanelEditor(long index);

    /** Get count of panel editors */
    public long getTransformationCount();

    /** Get specific panel editor */
    public XBTransformation getTransformation(long index);
}
