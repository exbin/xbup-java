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
package org.xbup.tool.editor.base.api;

/**
 * Interface for clipboard handler for visual component / context menu.
 *
 * @version 0.1.24 2014/11/08
 * @author XBUP Project (http://xbup.org)
 */
public interface ComponentClipboardHandler {

    /**
     * Performs cut to clipboard operation.
     */
    public void cut();

    /**
     * Performs copy to clipboard operation.
     */
    public void copy();

    /**
     * Performs paste from clipboard operation.
     */
    public void paste();

    /**
     * Performs delete selection operation.
     */
    public void delete();

    /**
     * Performs select all operation. (should include focus request)
     */
    public void selectAll();

    /**
     * Returns if selection is for clipboard operation is available.
     *
     * @return true if selection is available
     */
    public boolean isSelection();

    /**
     * Returns if it is possible to change components data using clipboard
     * operations.
     *
     * @return true if component is editable
     */
    public boolean isEditable();

    /**
     * Returns true if it is possible to execute select all operation.
     *
     * @return true if can perform select all
     */
    public boolean canSelectAll();
}
