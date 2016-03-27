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
package org.exbin.framework.gui.component.api;

import javax.swing.Action;

/**
 * Interface for item movement action set.
 *
 * @version 0.2.0 2016/03/22
 * @author ExBin Project (http://exbin.org)
 */
public interface EditItemActions {

    /**
     * Sets edit action handler.
     *
     * @param actionsHandler
     */
    void setEditItemActionsHandler(EditItemActionsHandler actionsHandler);

    /**
     * Returns add item action.
     *
     * @return add item action
     */
    Action getAddItemAction();

    /**
     * Returns edit item action.
     *
     * @return edit item action
     */
    Action getEditItemAction();

    /**
     * Returns delete item action.
     *
     * @return delete item action
     */
    Action getDeleteItemAction();

    /**
     * Updates state of these actions according to handler.
     */
    void updateEditItemActions();
}
