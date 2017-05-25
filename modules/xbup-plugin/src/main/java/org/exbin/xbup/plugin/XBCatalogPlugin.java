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
package org.exbin.xbup.plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * XBUP Editor plugin - provides editing panel for XBUP data.
 *
 * @version 0.2.1 2017/05/25
 * @author ExBin Project (http://exbin.org)
 */
public interface XBCatalogPlugin {

    /**
     * Gets catalog path of this plugin.
     *
     * @return Identification catalog path of this plugin file
     */
    @Nonnull
    String getPluginPath();

    /**
     * Gets count of line editors.
     *
     * @return count of line editor
     */
    long getLineEditorsCount();

    /**
     * Gets specific line editor.
     *
     * @param index line editor index
     * @return line editor
     */
    @Nullable
    XBLineEditor getLineEditor(long index);

    /**
     * Gets count of panel editors.
     *
     * @return count of panel editors
     */
    long getPanelEditorsCount();

    /**
     * Gets specific panel editor.
     *
     * @param index panel editor index
     * @return panel editor
     */
    @Nullable
    XBPanelEditor getPanelEditor(long index);

    /**
     * Gets count of transformations.
     *
     * @return transformation
     */
    long getTransformationCount();

    /**
     * Gets specific transformation.
     *
     * @param index transformation index
     * @return transformation
     */
    @Nullable
    XBTransformation getTransformation(long index);
}
