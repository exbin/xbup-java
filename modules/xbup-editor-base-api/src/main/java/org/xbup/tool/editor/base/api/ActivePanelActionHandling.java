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
import java.awt.event.ActionEvent;
import java.util.EventListener;

/**
 * Interface for application's panel.
 *
 * @version 0.1.25 2015/04/22
 * @author XBUP Project (http://xbup.org)
 */
public interface ActivePanelActionHandling extends EventListener {

    /**
     * Refreshes action statuses depending on active panel specific needs.
     *
     * @param component
     * @return true if action was performed, false means regular action should
     * be performed instead.
     */
    public boolean updateActionStatus(Component component);

    /**
     * Refreshes action statuses as leaving active panel.
     */
    public void releaseActionStatus();

    /**
     * Performs action on given panel.
     *
     * @param eventName name of event
     * @param event
     * @return true if action was performed, false means regular action should
     * be performed instead.
     */
    public boolean performAction(String eventName, ActionEvent event);

    /**
     * Returns currently used undo handler or null if not available.
     *
     * @return undo handle
     */
    public Object getUndoHandle();
}
