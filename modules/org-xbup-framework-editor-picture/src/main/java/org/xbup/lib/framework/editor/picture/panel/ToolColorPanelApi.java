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
package org.xbup.lib.framework.editor.picture.panel;

import java.awt.Color;

/**
 * Image panel tool color api.
 *
 * @version 0.2.0 2016/02/06
 * @author XBUP Project (http://xbup.org)
 */
public interface ToolColorPanelApi {

    /**
     * Returns tool color.
     *
     * @return color.
     */
    public Color getToolColor();

    /**
     * Returns selection color.
     *
     * @return color.
     */
    public Color getSelectionColor();

    /**
     * Sets tool color.
     *
     * @param color
     */
    public void setToolColor(Color color);

    /**
     * Sets selection color.
     *
     * @param color
     */
    public void setSelectionColor(Color color);
}
