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
package org.exbin.framework.editor.text.panel;

import java.awt.Font;

/**
 * Text font panel frame interface.
 *
 * @version 0.2.0 2016/01/23
 * @author ExBin Project (http://exbin.org)
 */
public interface TextFontPanelApi {

    /**
     * Returns current font used in application frame.
     *
     * @return font
     */
    public Font getCurrentFont();

    /**
     * Returns default colors used in application frame.
     *
     * @return font
     */
    public Font getDefaultFont();

    /**
     * Sets current colors used in application frame.
     *
     * @param font
     */
    public void setCurrentFont(Font font);
}
