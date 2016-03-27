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
package org.exbin.framework.gui.undo.api;

import org.exbin.framework.api.XBModuleRepositoryUtils;
import org.exbin.xbup.operation.undo.XBUndoHandler;
import org.exbin.framework.api.XBApplicationModule;

/**
 * Interface for XBUP framework undo/redo module.
 *
 * @version 0.2.0 2016/03/20
 * @author ExBin Project (http://exbin.org)
 */
public interface GuiUndoModuleApi extends XBApplicationModule {

    public static String MODULE_ID = XBModuleRepositoryUtils.getModuleIdByApi(GuiUndoModuleApi.class);

    /**
     * Returns undo handler.
     *
     * @return undo handler
     */
    XBUndoHandler getUndoHandler();

    /**
     * Sets current undo handler.
     *
     * @param undoHandler
     */
    void setUndoHandler(XBUndoHandler undoHandler);

    /**
     * Registers undo/redo operations to main frame menu.
     */
    void registerMainMenu();

    /**
     * Registers undo/redo operations to main frame menu.
     */
    void registerUndoManagerInMainMenu();

    /**
     * Registers undo/redo operations to main frame tool bar.
     */
    void registerMainToolBar();

    /**
     * Updates enablement of undo and redo operations.
     */
    void updateUndoStatus();

    /**
     * Opens undo manager dialog.
     */
    void openUndoManager();

    /**
     * Creates new instance of the undo actions set.
     *
     * @param undoActionsHandler clipboard handler
     * @return undo actions set
     */
    UndoActions createUndoActions(UndoActionsHandler undoActionsHandler);
}
