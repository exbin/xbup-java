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
import org.xbup.lib.framework.gui.api.XBApplication;

/**
 * Interface for application frame.
 *
 * @version 0.2.0 2016/01/10
 * @author XBUP Project (http://xbup.org)
 */
public interface ApplicationFrameHandler {

    /**
     * Gets current frame.
     *
     * @return frame
     */
    Frame getFrame();

    /**
     * Sets tool bar visibility.
     *
     * @param toolBarVisible
     */
    void setToolBarVisible(boolean toolBarVisible);

    /**
     * Sets status bar visibility.
     *
     * @param statusBarVisible
     */
    void setStatusBarVisible(boolean statusBarVisible);

    /**
     * Sets tool bar captions visibility.
     *
     * @param captionsVisible
     */
    void setToolBarCaptionsVisible(boolean captionsVisible);

    /**
     * Sets base appplication handler to be used as source of configuration.
     *
     * @param app base application handler
     */
    void setApplication(XBBaseApplication app);

    /**
     * Sets content of central area of the frame.
     *
     * @param component component to use
     */
    void setMainPanel(Component component);

    /**
     * Loads main menu for the frame.
     *
     * @param application
     */
    void loadMainMenu(XBApplication application);

    /**
     * Shows this frame.
     */
    void show();
}
