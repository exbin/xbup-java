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
package org.xbup.lib.framework.editor.wave.panel;

import java.awt.Color;

/**
 * Wave panel color api.
 *
 * @version 0.2.0 2016/01/23
 * @author XBUP Project (http://xbup.org)
 */
public interface WaveColorPanelApi {

    /**
     * Returns current colors used in application frame.
     *
     * @return array of 4 colors.
     */
    public Color[] getCurrentWaveColors();

    /**
     * Returns default colors used in application frame.
     *
     * @return array of 4 colors.
     */
    public Color[] getDefaultWaveColors();

    /**
     * Sets current colors used in application frame.
     *
     * @param colors
     */
    public void setCurrentWaveColors(Color[] colors);
}
