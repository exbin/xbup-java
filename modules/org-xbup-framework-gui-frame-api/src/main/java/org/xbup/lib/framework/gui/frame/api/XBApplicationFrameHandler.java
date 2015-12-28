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
package org.xbup.lib.framework.gui.frame.api;

import java.awt.Component;
import java.awt.Frame;
import org.xbup.lib.framework.gui.XBBaseApplication;

/**
 * Interface for editor frame.
 *
 * @version 0.2.0 2015/12/14
 * @author XBUP Project (http://xbup.org)
 */
public interface XBApplicationFrameHandler {

    /**
     * Gets current frame.
     *
     * @return frame
     */
    public Frame getFrame();

    /**
     * Sets toolbar visibility.
     *
     * @param toolBarVisible
     * @param captionsVisible
     * @param statusBarVisible
     */
    public void setToolBarsVisibility(boolean toolBarVisible, boolean captionsVisible, boolean statusBarVisible);

    /**
     * Sets base appplication handler to be used as source of configuration.
     *
     * @param app base application handler
     */
    public void setApplication(XBBaseApplication app);

    /**
     * Sets content of central area of the frame.
     *
     * @param component component to use
     */
    public void setMainPanel(Component component);
}
