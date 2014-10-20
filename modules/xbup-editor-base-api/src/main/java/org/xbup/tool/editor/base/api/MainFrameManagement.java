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
     * Gets action map.
     *
     * @return the actionMap
     */
    public ActionMap getActionMap();

    /**
     * Gets cut action.
     *
     * @return the cutAction
     */
    public Action getCutAction();

    /**
     * Gets copy action.
     *
     * @return the copyAction
     */
    public Action getCopyAction();

    /**
     * Gets paste action.
     *
     * @return the pasteAction
     */
    public Action getPasteAction();

    /**
     * Gets delete action.
     *
     * @return the deleteAction
     */
    public Action getDeleteAction();

    /**
     * Gets select all action.
     *
     * @return the selectAllAction
     */
    public Action getSelectAllAction();

    /**
     * Gets undo action.
     *
     * @return the undoAction
     */
    public Action getUndoAction();

    /**
     * Gets redo action.
     *
     * @return the redoAction
     */
    public Action getRedoAction();

    /**
     * Refresh undo action status.
     */
    public void refreshUndo();

    /**
     * Gets current frame preferences.
     *
     * @return preferences
     */
    public Preferences getPreferences();

    /**
     * Gets icon for dialogs.
     *
     * @return icon image
     */
    public Image getFrameIcon();

    /**
     * Initializes menu item.
     *
     * @param item menu item to initialize
     */
    public void initMenuItem(Object item);

    /**
     * Initializes menu.
     *
     * @param menu menu item to initialize
     */
    public void initMenu(JMenu menu);

    /**
     * Gets last focus owner.
     *
     * @return component
     */
    public Component getLastFocusOwner();

    /**
     * Gets current frame.
     *
     * @return frame
     */
    public Frame getFrame();
}
