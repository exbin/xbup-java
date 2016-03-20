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
package org.exbin.framework.gui.menu.api;

import javax.swing.Action;

/**
 * Interface for clipboard action set.
 *
 * @version 0.2.0 2016/03/16
 * @author ExBin Project (http://exbin.org)
 */
public interface ClipboardActions {

    /**
     * Updates state of these actions according to clipboard handler.
     */
    void updateClipboardActions();

    /**
     * Sets clipboard handler.
     *
     * @param clipboardHandler
     */
    void setClipboardActionsHandler(ClipboardActionsHandler clipboardHandler);

    /**
     * Returns cut to clipboard action.
     *
     * @return cut action
     */
    Action getCutAction();

    /**
     * Returns copy to clipboard action.
     *
     * @return copy action
     */
    Action getCopyAction();

    /**
     * Returns paste to clipboard action.
     *
     * @return paste action
     */
    Action getPasteAction();

    /**
     * Returns delete action.
     *
     * @return delete action
     */
    Action getDeleteAction();

    /**
     * Returns select all action.
     *
     * @return select all action
     */
    Action getSelectAllAction();
}
