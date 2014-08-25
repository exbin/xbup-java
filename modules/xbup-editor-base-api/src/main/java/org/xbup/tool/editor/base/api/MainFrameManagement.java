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

import java.awt.Component;
import java.awt.Frame;
import java.awt.Image;
import java.util.prefs.Preferences;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JMenu;

/**
 * Interface for application windows management.
 *
 * @version 0.1.23 2013/09/23
 * @author XBUP Project (http://xbup.org)
 */
public interface MainFrameManagement {

    /**
     * Get action map.
     *
     * @return the actionMap
     */
    public ActionMap getActionMap();

    /**
     * Get cut action.
     *
     * @return the cutAction
     */
    public Action getCutAction();

    /**
     * Get copy action.
     *
     * @return the copyAction
     */
    public Action getCopyAction();

    /**
     * Get paste action.
     *
     * @return the pasteAction
     */
    public Action getPasteAction();

    /**
     * Get delete action.
     *
     * @return the deleteAction
     */
    public Action getDeleteAction();

    /**
     * Get select all action.
     *
     * @return the selectAllAction
     */
    public Action getSelectAllAction();

    /**
     * Get undo action.
     *
     * @return the undoAction
     */
    public Action getUndoAction();

    /**
     * Get redo action.
     *
     * @return the redoAction
     */
    public Action getRedoAction();

    /**
     * Refresh undo action status.
     */
    public void refreshUndo();

    /**
     * Get current frame preferences.
     *
     * @return preferences
     */
    public Preferences getPreferences();

    /**
     * Get icon for dialogs.
     *
     * @return icon image
     */
    public Image getFrameIcon();

    /**
     * Initialize menu item.
     *
     * @param item menu item to initialize
     */
    public void initMenuItem(Object item);

    /**
     * Initialize menu.
     *
     * @param menu menu item to initialize
     */
    public void initMenu(JMenu menu);

    /**
     * Get last focus owner.
     *
     * @return component
     */
    public Component getLastFocusOwner();

    /**
     * Get current frame.
     *
     * @return frame
     */
    public Frame getFrame();
}
